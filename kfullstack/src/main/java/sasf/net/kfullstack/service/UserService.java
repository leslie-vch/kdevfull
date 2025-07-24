package sasf.net.kfullstack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.model.dto.response.UserResponse;
import sasf.net.kfullstack.model.util.RoleEnum;
import sasf.net.kfullstack.repository.UserRepository;
import sasf.net.kfullstack.security.jwt.dto.request.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAllUsers() {
        log.info("Listando todos los usuarios");
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> findById(Long id) {
        log.info("Buscando usuario por ID: {}", id);
        return userRepository.findById(id)
                .map(this::mapToDto);
    }

    public Optional<UserResponse> findByUsername(String username) {
        log.info("Buscando usuario por username: {}", username);
        return userRepository.findByUsername(username)
                .map(this::mapToDto);
    }

    public List<UserResponse> createRandomUsers() {
        log.info("Creando 5 usuarios aleatorios");

        List<UserEntity> newUsers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);

            UserEntity user = UserEntity.builder()
                    .username("user" + uniqueId)
                    .email("user" + uniqueId + "@example.com")
                    .password(passwordEncoder.encode("password" + i))
                    .role(RoleEnum.USER)
                    .build();

            newUsers.add(user);
        }

        List<UserEntity> saved = userRepository.saveAll(newUsers);
        return saved.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserResponse createUser(RegisterRequest request) {
        log.info("Creando nuevo usuario con username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Nombre de usuario '{}' ya existe", request.getUsername());
            throw new RuntimeException("Nombre de usuario ya existe");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Correo electrónico '{}' ya existe", request.getEmail());
            throw new RuntimeException("Correo electrónico ya existe");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
        log.debug("Usuario creado exitosamente con ID: {}", user.getId());

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    private UserResponse mapToDto(UserEntity user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
