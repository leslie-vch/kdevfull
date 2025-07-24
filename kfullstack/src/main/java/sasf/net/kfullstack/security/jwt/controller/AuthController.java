package sasf.net.kfullstack.security.jwt.controller;

import lombok.RequiredArgsConstructor;
import sasf.net.kfullstack.security.jwt.dto.request.AuthRequest;
import sasf.net.kfullstack.security.jwt.dto.request.RegisterRequest;
import sasf.net.kfullstack.security.jwt.dto.response.AuthResponse;
import sasf.net.kfullstack.security.jwt.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@SecurityRequirement(name = "")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
