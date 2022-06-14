package model.vo.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/08
 */

@NoArgsConstructor
@Data
public class AdminInfoVO {

    private Integer id;
    private String email;
    private String nickname;
    private String pwd;

}
