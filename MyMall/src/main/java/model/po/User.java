package model.po;

import lombok.Data;

/**
 * @author yzw
 * @since 2022/06/09
 */

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String recipient;
    private String address;
    private String phone;
}
