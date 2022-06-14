package model.bo.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/08
 */

@NoArgsConstructor
@Data
public class AdminInfoBO {

    private Integer id;
    private String nickname;
    private String email;
    private String pwd;

}
