package cqu.liuhang.summerproject.json;

import java.util.List;

/**
 * Created by LiuHang on 2017/7/5.
 **/

public class Staff {

    private List<StaffListBean> staffList;

    public List<StaffListBean> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffListBean> staffList) {
        this.staffList = staffList;
    }

    public static class StaffListBean {
        /**
         * staff_id : 154
         * seller_id : 1515
         * staff_name : arrr
         * staff_pic : rrrrr
         * staff_price : 153.30
         * staff_detail : rrqw
         */

        private String staff_id;
        private String seller_id;
        private String staff_name;
        private List<String> staff_pic;
        private String staff_price;
        private String staff_detail;
        private String staff_data;
        private String collectnum;

        public String getCollectnum() {
            return collectnum;
        }

        public void setCollectnum(String collectnum) {
            this.collectnum = collectnum;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public void setStaff_id(String staff_id) {
            this.staff_id = staff_id;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getStaff_name() {
            return staff_name;
        }

        public void setStaff_name(String staff_name) {
            this.staff_name = staff_name;
        }

        public List<String> getStaff_pic() {
            return staff_pic;
        }

        public void setStaff_pic(List<String> staff_pic) {
            this.staff_pic = staff_pic;
        }

        public String getStaff_price() {
            return staff_price;
        }

        public void setStaff_price(String staff_price) {
            this.staff_price = staff_price;
        }

        public String getStaff_detail() {
            return staff_detail;
        }

        public void setStaff_detail(String staff_detail) {
            this.staff_detail = staff_detail;
        }

        public String getStaff_data() {
            return staff_data;
        }

        public void setStaff_data(String staff_data) {
            this.staff_data = staff_data;
        }

        @Override
        public String toString() {
            return "StaffListBean{" +
                    "staff_id='" + staff_id + '\'' +
                    ", seller_id='" + seller_id + '\'' +
                    ", staff_name='" + staff_name + '\'' +
                    ", staff_pic='" + staff_pic + '\'' +
                    ", staff_price='" + staff_price + '\'' +
                    ", staff_detail='" + staff_detail + '\'' +
                    ", staff_data='" + staff_data + '\'' +
                    ", collectnum='" + collectnum + '\'' +
                    '}';
        }
    }
}
