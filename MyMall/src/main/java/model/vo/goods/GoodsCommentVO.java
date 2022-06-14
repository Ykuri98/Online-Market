package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/11
 */

@NoArgsConstructor
@Data
public class GoodsCommentVO {

        private List<GoodsCommentListVO> commentList;
        private String rate;

}
