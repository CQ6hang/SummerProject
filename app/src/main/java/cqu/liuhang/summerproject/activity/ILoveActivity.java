package cqu.liuhang.summerproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import cqu.liuhang.summerproject.adapter.MyBaseAdapter;
import cqu.liuhang.summerproject.json.Staff;

/**
 * Created by LiuHang on 2017/7/11.
 **/

public class ILoveActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private ImageButton back;

    private ListView listView;

    private List<Map<String, Object>> mapList;

    public static Staff staff;

    private TextView hint;

    private Button refresh;

    MyBaseAdapter adapter;

    final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilove);
        queue = Volley.newRequestQueue(this);
        title = (TextView) findViewById(R.id.activity_topbar3_tv_title);
        title.setText("我的收藏");
        listView = (ListView) findViewById(R.id.activity_ilove_listview);

        LayoutInflater inflater = LayoutInflater.from(this);
        View footer = inflater.inflate(R.layout.listview_refresh_footer, null);
        refresh = (Button) footer.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        hint = (TextView) findViewById(R.id.love_noDataHint);
        back = (ImageButton) findViewById(R.id.activity_topbar3_ib_back);
        back.setOnClickListener(this);
        mapList = new ArrayList<>();
        adapter = new MyBaseAdapter(this, mapList, queue);
        listView.setAdapter(adapter);
        listView.addFooterView(footer);
        listView.setFooterDividersEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                bundle.putInt("flag", 4);
                Intent intent = new Intent(ILoveActivity.this, StaffActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        loadData();
    }

    private void loadData() {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("staff", response);
                if (!response.equals("null")) {
                    Gson gson = new Gson();
                    staff = gson.fromJson(response, Staff.class);
                    changeData(mapList);
                    hint.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ILoveActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("rq", "collectstaff");
                map.put("userid", LoginActivity.user != null ? "" + LoginActivity.user.getUser_id() : "" + RegisterActivity.user.getUser_id());
                return map;
            }
        };
        queue.add(request);
    }

    private void changeData(List<Map<String, Object>> mapList) {
        mapList.clear();
//        boolean flag = true;
        int cnt = staff.getStaffList().size();
//        if (cnt % 2 == 1) {
//            flag = false;
//        }
        Log.d("staff", "" + cnt);
        for (int i = 0; i < cnt; i++) {
            Map<String, Object> map = new HashMap<>();
//            Log.d("tag", staff.getStaffList().get(i).getStaff_pic().get(0));
            map.put("pic", staff.getStaffList().get(i).getStaff_pic().get(0));
            map.put("detail", staff.getStaffList().get(i).getStaff_detail());
            map.put("price", staff.getStaffList().get(i).getStaff_price());
            map.put("loveCnt", staff.getStaffList().get(i).getCollectnum());
            map.put("name", staff.getStaffList().get(i).getStaff_name());
            mapList.add(map);
        }
    }

//    public List<Map<String, Object>> getData() {
//        mapList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("pic", "null");
//            map.put("detail", "商品详情" + i);
//            map.put("price", "9999.99");
//            map.put("loveCnt", "0");
//            map.put("name", "商品" + i);
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
