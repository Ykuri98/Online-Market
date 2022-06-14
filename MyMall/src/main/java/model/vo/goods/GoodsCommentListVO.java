package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/11
 */
@NoArgsConstructor
@Data
public class GoodsCommentListVO {

        private Double score;
        private Integer id;
        private String specName;
        private String comment;
        private String time;
        private Integer userId;

        private String nickname;
        private GoodsCommentUserVO user = new GoodsCommentUserVO();

        public void setNickname(String nickname) {
                user.setNickname(nickname);
        }
}
