package model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/08
 */

@NoArgsConstructor
@Data
public class Result {

    private Integer code;
    private String message;
    private Object data;

}
