package model.vo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */

@NoArgsConstructor
@Data
public class UserLoginVO {

    private Integer code;
    private String token;
    private String name;

}
