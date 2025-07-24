package sasf.net.kfullstack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sasf.net.kfullstack.error.custom.UnauthorizedException;
import sasf.net.kfullstack.model.ProjectEntity;
import sasf.net.kfullstack.model.TaskEntity;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.model.dto.request.TaskRequest;
import sasf.net.kfullstack.model.dto.response.TaskResponse;
import sasf.net.kfullstack.model.dto.response.UserResponse;
import sasf.net.kfullstack.model.util.RoleEnum;
import sasf.net.kfullstack.model.util.TaskStatusEnum;
import sasf.net.kfullstack.repository.ProjectRepository;
import sasf.net.kfullstack.repository.TaskRepository;
import sasf.net.kfullstack.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    private UserEntity getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Usuario autenticado: {}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Usuario no encontrado: {}", username);
            return new UnauthorizedException("Usuario no encontrado en el token");
        });
    }

    public TaskResponse create(TaskRequest request) {
        UserEntity user = getAuthenticatedUser();
        log.info("Creando tarea '{}' para el proyecto ID {} por el usuario '{}'", request.getTitle(),
                request.getProjectId(), user.getUsername());

        ProjectEntity project = projectRepository.findById(request.getProjectId())
                .filter(p -> p.getOwner().getId().equals(user.getId()))
                .orElseThrow(() -> {
                    log.warn("Proyecto ID {} no encontrado o no pertenece al usuario '{}'", request.getProjectId(),
                            user.getUsername());
                    return new RuntimeException("Proyecto no encontrado o no pertenece al usuario");
                });

        TaskEntity task = TaskEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatusEnum.PENDING)
                .dueDate(request.getDueDate())
                .assignedTo(user)
                .project(project)
                .build();

        taskRepository.save(task);
        log.debug("Tarea creada con ID {}", task.getId());
        return toMapperResponse(task);
    }

    public List<TaskResponse> getMyTasks() {
        UserEntity user = getAuthenticatedUser();
        log.info("Listando tareas asignadas al usuario '{}'", user.getUsername());

        return taskRepository.findByAssignedTo(user)
                .stream()
                .map(this::toMapperResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByProjectFiltered(Long projectId, String status, Sort.Direction sortDirection) {
        UserEntity user = getAuthenticatedUser();
        log.info("Listando tareas del proyecto {} con estado '{}' para el usuario '{}'", projectId, status,
                user.getUsername());

        ProjectEntity project = projectRepository.findById(projectId)
                .filter(p -> p.getOwner().getId().equals(user.getId()))
                .orElseThrow(() -> {
                    log.warn("Proyecto ID {} no encontrado o no pertenece al usuario '{}'", projectId,
                            user.getUsername());
                    return new RuntimeException("Proyecto no encontrado o no autorizado");
                });

        TaskStatusEnum statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = TaskStatusEnum.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Estado inválido: " + status);
            }
        }

        Sort.Direction direction = (sortDirection != null) ? sortDirection : Sort.Direction.ASC;

        List<TaskEntity> tasks;

        if (statusEnum != null) {
            tasks = taskRepository.findByProjectAndStatus(project, statusEnum, Sort.by(direction, "dueDate"));
        } else {
            tasks = taskRepository.findByProject(project, Sort.by(direction, "dueDate"));
        }

        return tasks.stream()
                .map(this::toMapperResponse)
                .collect(Collectors.toList());
    }

    public Optional<TaskResponse> update(Long id, TaskRequest request) {
        UserEntity user = getAuthenticatedUser();
        log.info("Actualizando tarea ID {} del usuario '{}'", id, user.getUsername());

        return taskRepository.findById(id)
                
                .map(task -> {
                    log.debug("Tarea encontrada, actualizando campos");
                    task.setTitle(request.getTitle());
                    task.setDescription(request.getDescription());
                    task.setDueDate(request.getDueDate());
                    task.setStatus(request.getStatus());
                    task.setAssignedTo(request.getAssignedToId() != null
                            ? userRepository.findById(request.getAssignedToId())
                                    .orElseThrow(() -> new RuntimeException("Usuario asignado no encontrado"))
                            : user);
                    return toMapperResponse(taskRepository.save(task));
                });
    }

    public boolean delete(Long id) {
        UserEntity user = getAuthenticatedUser();
        log.info("Eliminando tarea ID {} del usuario '{}'", id, user.getUsername());

        return taskRepository.findById(id)
                .filter(t -> t.getAssignedTo().getId().equals(user.getId()))
                .map(task -> {
                    taskRepository.delete(task);
                    log.debug("Tarea ID {} eliminada exitosamente", id);
                    return true;
                }).orElseGet(() -> {
                    log.warn("Tarea ID {} no encontrada o no pertenece al usuario '{}'", id, user.getUsername());
                    return false;
                });
    }

    private TaskResponse toMapperResponse(TaskEntity task) {
        TaskResponse dto = new TaskResponse();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setProjectId(task.getProject().getId());
        dto.setAssignedTo(task.getAssignedTo() != null
                ? new UserResponse(task.getAssignedTo().getId(), task.getAssignedTo().getUsername(),
                        task.getAssignedTo().getEmail(),
                        task.getAssignedTo().getRole() != null
                                ? RoleEnum.valueOf(task.getAssignedTo().getRole().name())
                                : null)
                : null);
        return dto;
    }
}
