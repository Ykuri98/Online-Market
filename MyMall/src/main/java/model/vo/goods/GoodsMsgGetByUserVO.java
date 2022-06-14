package model.vo.goods;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzw
 * @since 2022/06/11
 */

@NoArgsConstructor
@Data
public class GoodsMsgGetByUserVO {

        private Integer id;
        private String content;
        private String asker;
        private String time;

        private String replyContent;
        private String replyTime;
        private GoodsMsgGetByUserReplyVO reply = new GoodsMsgGetByUserReplyVO();

        public void setReplyContent(String replyContent) {
                reply.setContent(replyContent);
        }

        public void setReplyTime(String replyTime) {
                reply.setTime(replyTime);
        }
}
