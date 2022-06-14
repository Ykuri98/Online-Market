package model.bo.order;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/10
 */

@NoArgsConstructor
@Data
public class OrdersByPageBO {

    private Integer state;
    private Integer currentPage;
    private Integer pagesize;
    private String moneyLimit1;
    private String moneyLimit2;
    private String goods;
    private String address;
    private String name;
    private String id;

}
