package sasf.net.kfullstack.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;


@Configuration
public class OpenApiConfig {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);

    @Value("${springdoc.swagger.prod.uri}")
    private String prodUri;
    @Value("${springdoc.swagger.prod.dev}")
    private String devUri;
    @Value("${springdoc.swagger.email}")
    private String email;
    @Value("${springdoc.swagger.name}")
    private String name;
    @Value("${springdoc.swagger.pageurl}")
    private String pageurl;
    @Value("${springdoc.swagger.tittle}")
    private String tittle;
    @Value("${springdoc.swagger.description}")
    private String description;
    @Value("${springdoc.swagger.version}")
    private String version;

    @Bean
    OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUri);
        devServer.setDescription("Servidor de desarrollo.");

        Server prodServer = new Server();
        prodServer.setUrl(prodUri);
        prodServer.setDescription("Servidor de producction");

        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setName(name);
        contact.setUrl(pageurl);

        Info info = new Info()
                .title(tittle)
                .version(version)
                .contact(contact)
                .description(description);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @PostConstruct
    public void init() {
        logger.info("OpenAPI configuration initialized with the following values:");
        logger.info("prodUri: {}", prodUri);
        logger.info("devUri: {}", devUri);
        logger.info("email: {}", email);
        logger.info("name: {}", name);
        logger.info("pageurl: {}", pageurl);
        logger.info("tittle: {}", tittle);
        logger.info("description: {}", description);
        logger.info("version: {}", version);
        logger.info("Url Swagger: {}", devUri + "/swagger-ui/index.html");
    }
}
