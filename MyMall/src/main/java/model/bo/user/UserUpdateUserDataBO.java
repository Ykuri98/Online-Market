package model.bo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/13
 */

@NoArgsConstructor
@Data
public class UserUpdateUserDataBO {

    private String id;
    private String nickname;
    private String recipient;
    private String address;
    private String phone;

}
