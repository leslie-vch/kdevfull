package sasf.net.kfullstack.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sasf.net.kfullstack.model.util.TaskStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    private LocalDate dueDate;

    private LocalDateTime createdAt;

    private String createdBy;

    private String updatedBy;

    
    @ManyToOne
    @JoinColumn(name = "assigned_to", nullable = false)
    private UserEntity assignedTo;

    // Relación con el proyecto
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        createdBy = "system-create";
        if (status == null) status = TaskStatusEnum.PENDING;
    }

    @PostPersist
    public void onUpdate() {
        updatedBy = "system-update";
    }


}
