package sasf.net.kfullstack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.model.dto.response.UserResponse;
import sasf.net.kfullstack.repository.UserRepository;
import sasf.net.kfullstack.security.jwt.dto.request.RegisterRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() {
    
        RegisterRequest request = new RegisterRequest();
        request.setUsername("les");
        request.setEmail("les@sasf.net");
        request.setPassword("123456");
        request.setRole(sasf.net.kfullstack.model.util.RoleEnum.USER);

        when(userRepository.existsByUsername("les")).thenReturn(false);
        when(userRepository.existsByEmail("les@sasf.net")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encrypted_pass");

        UserEntity savedUser = UserEntity.builder()
                .id(1L)
                .username("les")
                .email("les@sasf.net")
                .password("encrypted_pass")
                .role(request.getRole())
                .build();

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("les", response.getUsername());
        assertEquals("les@sasf.net", response.getEmail());
        assertEquals(request.getRole(), response.getRole());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldThrowIfUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("les");
        request.setEmail("les@sasf.net");

        when(userRepository.existsByUsername("les")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(request));
        assertEquals("Nombre de usuario ya existe", ex.getMessage());
    }

    @Test
    void shouldThrowIfEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("les");
        request.setEmail("les@sasf.net");

        when(userRepository.existsByUsername("les")).thenReturn(false);
        when(userRepository.existsByEmail("les@sasf.net")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(request));
        assertEquals("Correo electrónico ya existe", ex.getMessage());
    }

    @Test
    void shouldReturnUserById() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("les")
                .email("les@sasf.net")
                .role(sasf.net.kfullstack.model.util.RoleEnum.ADMIN)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponse> response = userService.findById(1L);

        assertTrue(response.isPresent());
        assertEquals("les", response.get().getUsername());
    }
}
