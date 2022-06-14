package model.vo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */
@NoArgsConstructor
@Data
public class OrderSpecVO {

    private Integer id;
    private String specName;
    private Double unitPrice;

}
