package model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yzw
 * @since 2022/06/10
 */
@Data
@NoArgsConstructor
public class Msg {

    private Integer id;
    private Integer userId;
    private Integer goodsId;
    private String username;
    private String goodsName;
    private String content;
    private String replyContent;
    private Integer state;
    private Date createTime;
    private Date replyTime;
}
