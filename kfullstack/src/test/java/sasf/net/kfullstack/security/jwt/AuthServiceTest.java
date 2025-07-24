package sasf.net.kfullstack.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.model.util.RoleEnum;
import sasf.net.kfullstack.repository.UserRepository;
import sasf.net.kfullstack.security.jwt.dto.request.AuthRequest;
import sasf.net.kfullstack.security.jwt.dto.request.RegisterRequest;
import sasf.net.kfullstack.security.jwt.dto.response.AuthResponse;
import sasf.net.kfullstack.security.jwt.service.AuthService;
import sasf.net.kfullstack.security.jwt.util.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLoginSuccessfully() {
        AuthRequest request = new AuthRequest();
        request.setUsername("les");
        request.setPassword("123");

        UserEntity user = UserEntity.builder()
                .id(1L).username("les").password("encoded").role(RoleEnum.ADMIN).build();

        when(userRepository.findByUsername("les")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("mocked-jwt");

        AuthResponse response = authService.login(request);

        assertEquals("mocked-jwt", response.getToken());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("les");
        request.setEmail("les@sasf.net");
        request.setPassword("123");
        request.setRole(RoleEnum.USER);

        when(userRepository.existsByUsername("les")).thenReturn(false);
        when(userRepository.existsByEmail("les@sasf.net")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        when(jwtUtil.generateToken(any())).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);
        assertEquals("jwt-token", response.getToken());
    }
}
