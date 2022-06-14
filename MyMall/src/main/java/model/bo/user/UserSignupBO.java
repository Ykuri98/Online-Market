package model.bo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */

@NoArgsConstructor
@Data
public class UserSignupBO {

    private String email;
    private String nickname;
    private String pwd;
    private String recipient;
    private String address;
    private String phone;
}
