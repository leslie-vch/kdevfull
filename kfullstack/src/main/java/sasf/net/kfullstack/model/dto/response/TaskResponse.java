package sasf.net.kfullstack.model.dto.response;

import lombok.Data;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.model.util.TaskStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatusEnum status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private Long projectId;
    private UserResponse assignedTo;
}
