package cqu.liuhang.summerproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.adapter.CommentAdapter;
import cqu.liuhang.summerproject.json.UserComment;

/**
 * Created by LiuHang on 2017/7/10.
 **/

public class MyCommentActivity extends BaseActivity implements View.OnClickListener {

    TextView title;

    ListView listView;

    ImageButton back;

    List<Map<String, Object>> mapList;

    private UserComment userComment;

    private final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        queue = Volley.newRequestQueue(this);

        title = (TextView) findViewById(R.id.activity_topbar3_tv_title);
        title.setText("我的留言");
        back = (ImageButton) findViewById(R.id.activity_topbar3_ib_back);
        back.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.activity_mymessage_listview);
        mapList = new ArrayList<>();
        final CommentAdapter adapter = new CommentAdapter(this, mapList, queue);
        listView.setAdapter(adapter);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                Toast.makeText(MyCommentActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("rq", "comment");
                map.put("comment", LoginActivity.user != null ? "" + LoginActivity.user.getUser_id() : "" + RegisterActivity.user.getUser_id());
                return map;
            }
        };
        queue.add(request);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_topbar3_ib_back:
                finish();
                break;

            default:
        }
    }
}
