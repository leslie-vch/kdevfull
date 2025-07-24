package sasf.net.kfullstack.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import sasf.net.kfullstack.model.util.RoleEnum;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private RoleEnum role;
}
