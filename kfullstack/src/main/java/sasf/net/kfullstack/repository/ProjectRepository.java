
package sasf.net.kfullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sasf.net.kfullstack.model.ProjectEntity;
import sasf.net.kfullstack.model.UserEntity;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByOwner(UserEntity owner);
    Optional<ProjectEntity> findByIdAndOwner(Long id, UserEntity owner);
}
