package model.bo.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/08
 */

@NoArgsConstructor
@Data
public class AdminPwdBO {

    private String adminToken;
    private String oldPwd;
    private String newPwd;
    private String confirmPwd;

}
