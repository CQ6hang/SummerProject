package cqu.liuhang.summerproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class SearchActivity extends BaseActivity {

    private Button search;

    private EditText searchInput;

    private ListView history;

    private RequestQueue queue;

    public static Staff staff;

    final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    private List<Map<String, Object>> searchHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        queue = Volley.newRequestQueue(this);
        search = (Button) findViewById(R.id.activity_search_bt_search);
        history = (ListView) findViewById(R.id.activity_search_history);
        searchInput = (EditText) findViewById(R.id.search_input);
        searchHistory = new ArrayList<>();
        final MyBaseAdapter adapter = new MyBaseAdapter(this, searchHistory, queue);
        history.setAdapter(adapter);
        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                bundle.putInt("flag", 5);
                Intent intent = new Intent(SearchActivity.this, StaffActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchInput.getText().toString().isEmpty()) {
                    Toast.makeText(SearchActivity.this, "搜索不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("search", response);
                            if (!response.equals("null")) {
                                Gson gson = new Gson();
                                staff = gson.fromJson(response, Staff.class);
                                changeData(searchHistory);
                                adapter.notifyDataSetChanged();
                            } else {
                                searchHistory.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SearchActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("rq", "search");
                            map.put("search", searchInput.getText().toString());
                            map.put("userid", LoginActivity.user != null ? "" + LoginActivity.user.getUser_id() : "" + RegisterActivity.user.getUser_id());
                            return map;
                        }
                    };
                    queue.add(request);
                }
            }
        });
    }

    private void changeData(List<Map<String, Object>> searchHistory) {
        searchHistory.clear();
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
            searchHistory.add(map);
        }
    }
}
