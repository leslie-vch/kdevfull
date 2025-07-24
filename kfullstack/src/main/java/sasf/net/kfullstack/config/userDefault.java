package sasf.net.kfullstack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import sasf.net.kfullstack.model.util.RoleEnum;
import sasf.net.kfullstack.repository.UserRepository;
import sasf.net.kfullstack.security.jwt.dto.request.RegisterRequest;
import sasf.net.kfullstack.service.UserService;

@Slf4j
@Configuration
public class userDefault {

    @Autowired
    private UserService userService;

    @Bean
    public void defaultUser() {
        if (!(userService.findByUsername("admin").isPresent())) {
            log.info("Creando usuario admin por defecto");
            userService.createUser(new RegisterRequest(
                    "admin",
                    "admin@example.com",
                    "admin123",
                    RoleEnum.ADMIN));
            log.info("Usuario admin creado por defecto");
        }

    }

}
