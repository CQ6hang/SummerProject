package cqu.liuhang.summerproject.json;

/**
 * Created by LiuHang on 2017/7/15.
 **/

public class UserLocation {

    /**
     * longitude : panyi
     * dimension : asd
     * accuracy : 11111
     * userid : 1
     */

    private String longitude;
    private String dimension;
    private String accuracy;
    private String userid;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
