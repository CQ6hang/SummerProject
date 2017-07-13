package cqu.liuhang.summerproject.json;

import java.util.List;

/**
 * Created by LiuHang on 2017/7/12.
 **/

public class UserComment {

    private List<CommentListBean> commentList;

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class CommentListBean {
        /**
         * headiamge : /headimage/112.jpg
         * user_name : 刘航
         * user_id : 3
         * content : 可以的
         * comment_date : 2017-07-10 14:52:20.0
         */

        private String headiamge;
        private String user_name;
        private String user_id;
        private String content;
        private String comment_date;

        public String getHeadiamge() {
            return headiamge;
        }

        public void setHeadiamge(String headiamge) {
            this.headiamge = headiamge;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getComment_date() {
            return comment_date;
        }

        public void setComment_date(String comment_date) {
            this.comment_date = comment_date;
        }
    }
}
