package sasf.net.kfullstack.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import sasf.net.kfullstack.model.util.TaskStatusEnum;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "El título de la tarea es obligatorio.")
    private String title;

    @NotBlank(message = "La descripción de la tarea es obligatoria.")
    private String description;

    @NotNull(message = "El estado de la tarea es obligatorio.")
    private TaskStatusEnum status;

    @NotNull(message = "El ID del proyecto asociado es obligatorio.")
    private Long projectId;

    @FutureOrPresent(message = "La fecha de vencimiento debe ser hoy o en el futuro.")
    private LocalDate dueDate;

    @NotNull(message = "El ID del usuario asignado es obligatorio.")
    private Long assignedToId;
}

