package model.po;

import lombok.Data;

/**
 * @author yzw
 * @since 2022/06/08
 */

@Data
public class Admin {

    private Integer id;
    private String username;
    private String password;
    private String nickname;

}
