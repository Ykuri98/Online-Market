package model.bo.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/08
 */

@NoArgsConstructor
@Data
public class AdminLoginBO {

    private String email;
    private String pwd;

}
