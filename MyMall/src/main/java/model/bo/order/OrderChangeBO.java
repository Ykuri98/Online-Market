package model.bo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */

@NoArgsConstructor
@Data
public class OrderChangeBO {

    private String id;
    private Integer state;
    private Integer spec;
    private Integer num;

}
