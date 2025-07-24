package sasf.net.kfullstack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sasf.net.kfullstack.model.util.RoleEnum;
import sasf.net.kfullstack.model.util.TaskStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectEntity> projects;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEntity> tasks;

    private String createdBy;

    private String updatedBy;

    @PrePersist
    public void onCreate() {
        createdBy = "system-create";
    }

    @PostPersist
    public void onUpdate() {
        updatedBy = "system-update";
    }
}
