package sasf.net.kfullstack.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectRequest {

    @NotBlank(message = "El nombre del proyecto es obligatorio.")
    private String name;

    private String description;
}
