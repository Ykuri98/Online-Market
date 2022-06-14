package model.vo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/09
 */

@NoArgsConstructor
@Data
public class UserInfoVO {

    private Integer id;
    private String email;
    private String nickname;
    private String pwd;
    private String recipient;
    private String address;
    private String phone;
    private Integer code;

}
