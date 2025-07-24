package sasf.net.kfullstack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import sasf.net.kfullstack.error.custom.UnauthorizedException;
import sasf.net.kfullstack.model.ProjectEntity;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.model.dto.request.ProjectRequest;
import sasf.net.kfullstack.model.dto.response.ProjectResponse;
import sasf.net.kfullstack.repository.ProjectRepository;
import sasf.net.kfullstack.repository.UserRepository;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private UserEntity getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Obteniendo usuario autenticado: {}", username);
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Usuario no encontrado: {}", username);
            return new UnauthorizedException("Usuario no encontrado en el token");
        });
    }

    public ProjectResponse create(ProjectRequest request) {
        UserEntity user = getAuthenticatedUser();
        log.info("Creando proyecto '{}' para usuario '{}'", request.getName(), user.getUsername());

        ProjectEntity project = ProjectEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(user)
                .build();

        projectRepository.save(project);
        log.debug("Proyecto guardado con ID: {}", project.getId());

        return toMapperResponse(project);
    }

    public List<ProjectResponse> getMyProjects() {
        UserEntity user = getAuthenticatedUser();
        log.info("Listando proyectos para usuario '{}'", user.getUsername());

        return projectRepository.findByOwner(user)
                .stream()
                .map(this::toMapperResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse getSelectMyProyect(Long id) {
        UserEntity user = getAuthenticatedUser();
        log.info("Obteniendo proyecto con ID {} para usuario '{}'", id, user.getUsername());

        return projectRepository.findByIdAndOwner(id, user)
                .map(this::toMapperResponse)
                .orElseThrow(() -> {
                    log.error("Proyecto con ID {} no encontrado o no pertenece al usuario", id);
                    return new RuntimeException("Proyecto no encontrado o no autorizado");
                });

    }

    public Optional<ProjectResponse> update(Long id, ProjectRequest request) {
        UserEntity user = getAuthenticatedUser();
        log.info("Actualizando proyecto con ID {} para usuario '{}'", id, user.getUsername());

        return projectRepository.findById(id)
                .filter(p -> p.getOwner().getId().equals(user.getId()))
                .map(project -> {
                    log.debug("Proyecto encontrado. Actualizando...");
                    project.setName(request.getName());
                    project.setDescription(request.getDescription());
                    return toMapperResponse(projectRepository.save(project));
                });
    }

    public boolean delete(Long id) {
        UserEntity user = getAuthenticatedUser();
        log.info("Eliminando proyecto con ID {} para usuario '{}'", id, user.getUsername());

        return projectRepository.findById(id)
                .filter(p -> p.getOwner().getId().equals(user.getId()))
                .map(project -> {
                    projectRepository.delete(project);
                    log.debug("Proyecto con ID {} eliminado exitosamente", id);
                    return true;
                }).orElseGet(() -> {
                    log.warn("No se encontró proyecto con ID {} para eliminar o no pertenece al usuario", id);
                    return false;
                });
    }

    private ProjectResponse toMapperResponse(ProjectEntity p) {
        ProjectResponse dto = new ProjectResponse();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }
}
