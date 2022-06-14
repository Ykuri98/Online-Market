package model.bo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/13
 */

@NoArgsConstructor
@Data
public class UserUpdatePwd {
    private Integer id;
    private String oldPwd;
    private String newPwd;
    private String confirmPwd;
}
