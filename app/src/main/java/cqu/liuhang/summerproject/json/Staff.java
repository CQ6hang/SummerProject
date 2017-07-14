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
         * staff_id : 1561
         * seller_id : 1561
         * staff_name : 裤子
         * staff_pic : ["/123.jpg","/12.jpg","/555.jpg"]
         * staff_price : 156.00
         * staff_detail : 这还是一个裤子，刘航的裤子这还是一个裤子，刘航的裤子这还是一个裤子，刘航的裤子这还是一个裤子，刘航的裤子这还是一个裤子，刘航的裤子这还是一个裤子，刘航的裤子
         * staff_date : 2017-07-08
         * collectnum : 99
         * isCollect : true
         */

        private String staff_id;
        private String seller_id;
        private String staff_name;
        private String staff_price;
        private String staff_detail;
        private String staff_date;
        private String collectnum;
        private boolean isCollect;
        private List<String> staff_pic;

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

        public String getStaff_date() {
            return staff_date;
        }

        public void setStaff_date(String staff_date) {
            this.staff_date = staff_date;
        }

        public String getCollectnum() {
            return collectnum;
        }

        public void setCollectnum(String collectnum) {
            this.collectnum = collectnum;
        }

        public boolean isIsCollect() {
            return isCollect;
        }

        public void setIsCollect(boolean isCollect) {
            this.isCollect = isCollect;
        }

        public List<String> getStaff_pic() {
            return staff_pic;
        }

        public void setStaff_pic(List<String> staff_pic) {
            this.staff_pic = staff_pic;
        }
    }
}
