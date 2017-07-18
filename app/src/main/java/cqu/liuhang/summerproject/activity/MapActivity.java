package cqu.liuhang.summerproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.json.UserLocation;

public class MapActivity extends BaseActivity {

    private final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";
    MapView mMapView = null;
    BaiduMap mBaiduMap;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        queue = Volley.newRequestQueue(this);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("hello", response);
                if (!response.equals("false")) {
                    Gson gson = new Gson();
                    UserLocation location = gson.fromJson(response, UserLocation.class);

                    // 构造定位数据
                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(Float.valueOf(location.getAccuracy()))
                            .direction(100).latitude(Double.valueOf(location.getDimension()))
                            .longitude(Double.valueOf(location.getLongitude())).build();
                    // 设置定位数据
                    mBaiduMap.setMyLocationData(locData);

                    LatLng point = new LatLng(Double.valueOf(location.getDimension()),
                            Double.valueOf(location.getLongitude()));
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(point)
                            .zoom(18)
                            .build();

                    // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
                    BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.locate);
                    MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
                    mBaiduMap.setMyLocationConfiguration(config);

                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    mBaiduMap.setMapStatus(mMapStatusUpdate);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapActivity.this, "定位失败,请检查网络", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("rq", "getbatadd");
                map.put("userid", SellerInfoActivity.seller.getUser_id() + "");
                return map;
            }
        };
        queue.add(request);

        // 当不需要定位图层时关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}

