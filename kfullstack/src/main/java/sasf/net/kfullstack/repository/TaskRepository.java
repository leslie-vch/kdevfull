
package sasf.net.kfullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;

import sasf.net.kfullstack.model.ProjectEntity;
import sasf.net.kfullstack.model.TaskEntity;
import sasf.net.kfullstack.model.UserEntity;
import sasf.net.kfullstack.model.dto.response.TaskResponse;
import sasf.net.kfullstack.model.util.TaskStatusEnum;
import org.springframework.data.domain.Sort;


import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByAssignedTo(UserEntity user);

    List<TaskEntity> findByProject(ProjectEntity project, Sort sort);

    List<TaskEntity> findByProjectAndStatus(ProjectEntity project, TaskStatusEnum status, Sort sort);

}
