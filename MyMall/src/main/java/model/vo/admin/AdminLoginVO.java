package model.vo.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/08
 */

@NoArgsConstructor
@Data
public class AdminLoginVO {

    private String token;
    private String name;

}