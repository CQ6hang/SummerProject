package cqu.liuhang.summerproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.adapter.CommentAdapter;
import cqu.liuhang.summerproject.adapter.MyBaseAdapter;
import cqu.liuhang.summerproject.json.User;
import cqu.liuhang.summerproject.json.UserComment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LiuHang on 2017/7/9.
 **/

public class SellerInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton back;

    private TextView title;

    private List<Map<String, Object>> mapList;

    private ListView listView;

    private RequestQueue queue;

    private User seller;

    private CircleImageView headPic;

    private TextView sellerName;

    private ImageView sellerSex;

    private TextView phone;

    private TextView address;

    private final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    private String imageUrl = "http://192.168.191.1:8080/WebDemo/image";

    private UserComment userComment;

    private Button locate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_info);

        queue = Volley.newRequestQueue(this);
        mapList = new ArrayList<>();
        final CommentAdapter adapter = new CommentAdapter(this, mapList, queue);

        Bundle bundle = getIntent().getExtras();
        final String sellerID = bundle.getString("sellerID");

        back = (ImageButton) findViewById(R.id.activity_topbar3_ib_back);
        title = (TextView) findViewById(R.id.activity_topbar3_tv_title);
        title.setText("卖家信息");
        listView = (ListView) findViewById(R.id.seller_comment);
        listView.setAdapter(adapter);
        back.setOnClickListener(this);
        headPic = (CircleImageView) findViewById(R.id.head_pict);
        sellerName = (TextView) findViewById(R.id.sellername);
        sellerSex = (ImageView) findViewById(R.id.seller_sex);
        phone = (TextView) findViewById(R.id.seller_phone);
        address = (TextView) findViewById(R.id.seller_address);
        locate = (Button) findViewById(R.id.positionpic);
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerInfoActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("tag", "response:" + response);
                Gson gson = new Gson();
                seller = gson.fromJson(response, User.class);
                sellerName.setText(seller.getUser_name());
                sellerSex.setImageResource(seller.getSex().equals("男") ? R.drawable.male : R.drawable.female);
                phone.setText(seller.getQq());
                address.setText(seller.getAddress());

                String newURL;
                newURL = imageUrl.concat(seller.getHeadimage());
                ImageLoader loader = new ImageLoader(queue, new MyBaseAdapter.BitmapCache());
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(headPic, R.drawable.welcome2, R.drawable.welcome3);
                loader.get(newURL, listener);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellerInfoActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("rq", "seller");
                map.put("seller", sellerID);
                return map;
            }
        };
        queue.add(request);

        StringRequest requestComment = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("tag", "response:" + response);
                if (!response.equals("null")) {
                    Gson gson = new Gson();
                    userComment = gson.fromJson(response, UserComment.class);
                    changeData(mapList);
                    adapter.notifyDataSetChanged();
                } else {
                    mapList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellerInfoActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("rq", "comment");
                map.put("comment", sellerID);
                return map;
            }
        };
        queue.add(requestComment);

    }

    private void changeData(List<Map<String, Object>> mapList) {
        mapList.clear();
//        boolean flag = true;
        int cnt = userComment.getCommentList().size();
//        if (cnt % 2 == 1) {
//            flag = false;
//        }
        for (int i = 0; i < cnt; i++) {
            Map<String, Object> map = new HashMap<>();
//            Log.d("tag", staff.getStaffList().get(i).getStaff_pic().get(0));
            map.put("headPic", userComment.getCommentList().get(i).getHeadiamge());
            map.put("name", userComment.getCommentList().get(i).getUser_name());
            map.put("date", userComment.getCommentList().get(i).getComment_date());
            map.put("comment", userComment.getCommentList().get(i).getContent());
            mapList.add(map);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_topbar3_ib_back:
                finish();
                break;
            default:
        }
    }

//    public List<Map<String, Object>> getData() {
//        mapList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("headPic", "null");
//            map.put("name", "评论员" + i);
//            map.put("date", "2017/7/11");
//            map.put("comment", "还可以" + i);
//            mapList.add(map);
//        }
//        return mapList;
//    }
}
