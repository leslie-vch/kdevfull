package sasf.net.kfullstack.security.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.repository.UserRepository;
import sasf.net.kfullstack.security.jwt.dto.request.AuthRequest;
import sasf.net.kfullstack.security.jwt.dto.request.RegisterRequest;
import sasf.net.kfullstack.security.jwt.dto.response.AuthResponse;
import sasf.net.kfullstack.security.jwt.util.JwtUtil;

import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(AuthRequest request) {
        log.info("Intentando autenticar al usuario: {}", request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse register(RegisterRequest request) {
        log.info("Registrando nuevo usuario: {}", request.getUsername());
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Nombre de usuario ya existe");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole()) // Solo el ADMIN puede asignar roles
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }
}
