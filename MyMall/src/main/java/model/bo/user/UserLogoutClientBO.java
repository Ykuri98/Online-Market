package model.bo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/13
 */

@Data
@NoArgsConstructor
public class UserLogoutClientBO {
    private String token;
}
