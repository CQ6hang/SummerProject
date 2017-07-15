package cqu.liuhang.summerproject.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.adapter.MyBaseAdapter;
import cqu.liuhang.summerproject.fragment.InfoFragment;
import cqu.liuhang.summerproject.json.User;
import cqu.liuhang.summerproject.json.UserLocation;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton back;

    private TextView title;

    private User user;

    private ProgressBar progressBar;

    private Button editButton;

    private CircleImageView headPic;

    private EditText name;

    private RadioButton girl;

    private RadioButton boy;

    private EditText age;

    private TextView phone;

    private TextView address;

    private RequestQueue queue;

    private TextView progress;

    private int cnt = 6;

    public LocationClient mLocationClient = null;

    public BDLocationListener myListener = new MyLocationListener();

    private Button getAddress;

    Handler handler;

    String myLocation;

    String latitude;

    String lontitude;

    String radius;

    final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    final String imageurl = "http://192.168.191.1:8080/WebDemo/image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        queue = Volley.newRequestQueue(this);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        initLocation();

        user = LoginActivity.user != null ? LoginActivity.user : RegisterActivity.user;

        back = (ImageButton) findViewById(R.id.activity_topbar3_ib_back);
        title = (TextView) findViewById(R.id.activity_topbar3_tv_title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editButton = (Button) findViewById(R.id.activity_edit_bt_edit);
        getAddress = (Button) findViewById(R.id.getAddress);
        progress = (TextView) findViewById(R.id.edit_progress);
        headPic = (CircleImageView) findViewById(R.id.edit_headPic);
        name = (EditText) findViewById(R.id.edit_name);
        boy = (RadioButton) findViewById(R.id.boy);
        girl = (RadioButton) findViewById(R.id.girl);
        age = (EditText) findViewById(R.id.edit_age);
        phone = (TextView) findViewById(R.id.edit_phone);
        address = (TextView) findViewById(R.id.edit_address);


        back.setOnClickListener(this);
        editButton.setOnClickListener(this);
        title.setText("修改信息");
        getAddress.setOnClickListener(this);

        String newURL;
        newURL = imageurl.concat(user.getHeadimage());
//            Log.d("URL", newURL);
        ImageLoader loader = new ImageLoader(queue, new MyBaseAdapter.BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(headPic, R.drawable.welcome2, R.drawable.welcome3);
        loader.get(newURL, listener);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    address.setText(myLocation);
                    StringRequest sendLocation = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("null")) {
                                Toast.makeText(EditActivity.this, "定位信息上传失败，请重新定位", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EditActivity.this, "定位信息上传失败，请重新定位", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("rq", "setbatadd");
                            UserLocation userLocation = new UserLocation();
                            userLocation.setAccuracy(radius);
                            userLocation.setDimension(latitude);
                            userLocation.setLongitude(lontitude);
                            userLocation.setUserid(LoginActivity.user != null ? "" + LoginActivity.user.getUser_id() : "" + RegisterActivity.user.getUser_id());
                            map.put("batadd", new Gson().toJson(userLocation));
                            return map;
                        }
                    };
                    queue.add(sendLocation);
                }
            }
        };

        if (user.getUser_name().equals("")) {
            cnt--;
        } else {
            name.setText(user.getUser_name());
        }

        if (user.getSex().equals("男")) {
            boy.setChecked(true);
        } else {
            girl.setChecked(true);
        }

        if (user.getAge().equals("-1")) {
            cnt--;
        } else {
            age.setText(user.getAge());
        }

        phone.setText(user.getPhone());

        if (user.getAddress().equals("")) {
            cnt--;
        } else {
            address.setText(user.getAddress());
        }
        Log.d("tag", "" + cnt);
        progressBar.setProgress(cnt * 100 / 6);
        progress.setText("完整度 " + cnt * 100 / 6);

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(0);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(false);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息
            latitude = "" + location.getLatitude();

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息
            lontitude = "" + location.getLongitude();

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度
            radius = "" + location.getRadius();

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                myLocation = location.getAddrStr();
                Message gps = new Message();
                gps.what = 1;
                handler.sendMessage(gps);
                mLocationClient.stop();
                Toast.makeText(EditActivity.this, "定位成功", Toast.LENGTH_SHORT).show();

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息
                myLocation = location.getAddrStr();
                Message gps = new Message();
                gps.what = 1;
                handler.sendMessage(gps);
                mLocationClient.stop();
                Toast.makeText(EditActivity.this, "定位成功", Toast.LENGTH_SHORT).show();

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                Toast.makeText(EditActivity.this, "定位权限获取失败", Toast.LENGTH_SHORT).show();
                mLocationClient.stop();

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                Toast.makeText(EditActivity.this, "网络状态不佳", Toast.LENGTH_SHORT).show();
                mLocationClient.stop();

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            Log.i("BaiduLocationApiDem", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_topbar3_ib_back:
                finish();
                break;
            case R.id.getAddress:
                mLocationClient.start();
//                mLocationClient.stop();
                break;
            case R.id.activity_edit_bt_edit:
                final User temp = user;
                temp.setUser_name(name.getText().toString());
                temp.setAddress(address.getText().toString());
                temp.setAge(age.getText().toString());
                temp.setSex(boy.isChecked() ? "男" : "女");
//                user.setHeadimage();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("true")) {
                            Toast.makeText(EditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            user = temp;
                            finish();
                            InfoFragment.refresh();
                        } else {
                            Toast.makeText(EditActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("rq", "modify");
                        Gson gson = new Gson();
                        map.put("modify", gson.toJson(temp));
                        return map;
                    }
                };
                queue.add(request);
                break;
            default:
        }
    }
}
