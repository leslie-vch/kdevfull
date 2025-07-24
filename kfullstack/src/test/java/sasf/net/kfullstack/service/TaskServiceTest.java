package sasf.net.kfullstack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sasf.net.kfullstack.model.*;
import sasf.net.kfullstack.model.dto.request.TaskRequest;
import sasf.net.kfullstack.model.dto.response.TaskResponse;
import sasf.net.kfullstack.model.util.TaskStatusEnum;
import sasf.net.kfullstack.repository.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @InjectMocks
    private TaskService taskService;

    private UserEntity user;
    private ProjectEntity project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = UserEntity.builder().id(1L).username("les").build();
        project = ProjectEntity.builder().id(10L).owner(user).build();
        when(userRepository.findByUsername("les")).thenReturn(Optional.of(user));
        org.springframework.security.core.context.SecurityContextHolder
                .getContext().setAuthentication(
                        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("les", null,
                                java.util.Collections.emptyList()));
    }

    @Test
    void shouldCreateTask() {
        TaskRequest req = new TaskRequest();
        req.setTitle("Tarea 1");
        req.setDescription("desc");
        req.setProjectId(10L);
        req.setStatus(TaskStatusEnum.PENDING);
        req.setDueDate(LocalDate.now());

        when(projectRepository.findById(10L)).thenReturn(Optional.of(project));

        TaskEntity task = TaskEntity.builder().id(1L).title("Tarea 1").project(project).assignedTo(user).build();
        when(taskRepository.save(any())).thenReturn(task);

        TaskResponse response = taskService.create(req);

        assertEquals("Tarea 1", response.getTitle());
        assertEquals(10L, response.getProjectId());
    }

    @Test
    void shouldReturnMyTasks() {
        TaskEntity t1 = TaskEntity.builder().id(1L).title("T1").assignedTo(user).project(project).build();
        TaskEntity t2 = TaskEntity.builder().id(2L).title("T2").assignedTo(user).project(project).build();

        when(taskRepository.findByAssignedTo(user)).thenReturn(List.of(t1, t2));

        List<TaskResponse> list = taskService.getMyTasks();
        assertEquals(2, list.size());
    }

    @Test
    void shouldUpdateOwnedTask() {
        ProjectEntity project = ProjectEntity.builder().id(10L).owner(user).build(); 
                                                                                     

        TaskEntity task = TaskEntity.builder()
                .id(1L)
                .assignedTo(user)
                .title("Old")
                .project(project)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        TaskRequest req = new TaskRequest();
        req.setTitle("Updated");

        Optional<TaskResponse> updated = taskService.update(1L, req);
        assertTrue(updated.isPresent());
        assertEquals("Updated", updated.get().getTitle());
    }

    @Test
    void shouldDeleteOwnedTask() {
        TaskEntity task = TaskEntity.builder().id(5L).assignedTo(user).build();
        when(taskRepository.findById(5L)).thenReturn(Optional.of(task));

        boolean deleted = taskService.delete(5L);
        assertTrue(deleted);
        verify(taskRepository).delete(task);
    }
}
