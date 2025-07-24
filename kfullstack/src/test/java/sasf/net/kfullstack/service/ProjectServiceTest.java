package sasf.net.kfullstack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sasf.net.kfullstack.model.ProjectEntity;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.model.dto.request.ProjectRequest;
import sasf.net.kfullstack.model.dto.response.ProjectResponse;
import sasf.net.kfullstack.repository.ProjectRepository;
import sasf.net.kfullstack.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = UserEntity.builder().id(1L).username("ken").build();
        when(userRepository.findByUsername("ken")).thenReturn(Optional.of(user));
       
        org.springframework.security.core.context.SecurityContextHolder
                .getContext().setAuthentication(
                        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("ken", null,
                                java.util.Collections.emptyList()));
    }

    @Test
    void shouldCreateProject() {
        ProjectRequest req = new ProjectRequest();
        req.setName("Proyecto 1");
        req.setDescription("desc");

        ProjectEntity saved = ProjectEntity.builder()
                .id(10L)
                .name("Proyecto 1")
                .description("desc")
                .owner(user)
                .build();

        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(saved);

        ProjectResponse res = projectService.create(req);

        assertEquals("Proyecto 1", res.getName());
        verify(projectRepository).save(any(ProjectEntity.class));
    }

    @Test
    void shouldListProjectsOfUser() {
        ProjectEntity p1 = ProjectEntity.builder().id(1L).name("A").owner(user).build();
        ProjectEntity p2 = ProjectEntity.builder().id(2L).name("B").owner(user).build();

        when(projectRepository.findByOwner(user)).thenReturn(List.of(p1, p2));

        List<ProjectResponse> list = projectService.getMyProjects();

        assertEquals(2, list.size());
    }

    @Test
    void shouldUpdateProjectIfOwned() {
        ProjectEntity project = ProjectEntity.builder()
                .id(1L).name("Old").description("x").owner(user).build();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProjectRequest req = new ProjectRequest();
        req.setName("New Name");
        req.setDescription("Updated");

        Optional<ProjectResponse> updated = projectService.update(1L, req);

        assertTrue(updated.isPresent());
        assertEquals("New Name", updated.get().getName());
    }

    @Test
    void shouldDeleteProjectIfOwned() {
        ProjectEntity project = ProjectEntity.builder().id(1L).owner(user).build();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        boolean deleted = projectService.delete(1L);
        assertTrue(deleted);
        verify(projectRepository).delete(project);
    }

    @Test
    void shouldNotDeleteProjectIfNotOwned() {
        UserEntity other = UserEntity.builder().id(99L).build();
        ProjectEntity project = ProjectEntity.builder().id(1L).owner(other).build();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        boolean deleted = projectService.delete(1L);
        assertFalse(deleted);
        verify(projectRepository, never()).delete(any());
    }
}
