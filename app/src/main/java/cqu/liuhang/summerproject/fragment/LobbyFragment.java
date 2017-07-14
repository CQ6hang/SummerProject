package cqu.liuhang.summerproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cqu.liuhang.summerproject.activity.LoginActivity;
import cqu.liuhang.summerproject.activity.RegisterActivity;
import cqu.liuhang.summerproject.util.GlideImageLoader;
import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.activity.SearchActivity;
import cqu.liuhang.summerproject.activity.StaffActivity;
import cqu.liuhang.summerproject.adapter.MyBaseAdapter;
import cqu.liuhang.summerproject.json.Staff;

/**
 * Created by LiuHang on 2017/7/2.
 **/

public class LobbyFragment extends Fragment {

    private ListView itemList;

    private Button search;

    private RequestQueue queue;

    public static Staff staff;

    private static List<Map<String, Object>> mapList;

    private Banner banner;

    final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    List<Integer> imagesList;

    private TextView hint;

    private MyBaseAdapter adapter;

    private Button refresh;

//    final String pic = "http://192.168.191.1:8080/WebDemo/image/123.jpg";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lobby, container, false);
        itemList = (ListView) view.findViewById(R.id.fragment_lobby_lv_item);
        search = (Button) view.findViewById(R.id.fragment_lobby_lo_search);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());

        mapList = new ArrayList<>();
        adapter = new MyBaseAdapter(getActivity(), mapList, queue);

        // 向listView添加header
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.listview_ads_header, null);
        banner = (Banner) view.findViewById(R.id.banner);
        hint = (TextView) view.findViewById(R.id.lobby_noDataHint);

        View footer = inflater.inflate(R.layout.listview_refresh_footer, null);
        refresh = (Button) footer.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });


        banner.setImageLoader(new GlideImageLoader());
        imagesList = new ArrayList<>();
        imagesList.add(R.drawable.ads1);
        imagesList.add(R.drawable.ads2);
        imagesList.add(R.drawable.ads3);
        banner.setImages(imagesList);
        banner.start();
        itemList.addHeaderView(view);
        itemList.addFooterView(footer);
        itemList.setFooterDividersEnabled(false);
        itemList.setHeaderDividersEnabled(false);

        itemList.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                bundle.putInt("flag", 1);
                Intent intent = new Intent(getActivity(), StaffActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        loadData();

//        ImageRequest imageRequest = new ImageRequest(pic, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap response) {
//                ads.setImageBitmap(response);
//            }
//        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "无法连接到服务器", Toast.LENGTH_SHORT).show();
//            }
//        });
//        queue.add(imageRequest);
    }

    private void loadData() {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("staff", response);
                Gson gson = new Gson();
                staff = gson.fromJson(response, Staff.class);
                changeData(mapList);
                hint.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "无法连接到服务器", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("rq", "staff");
                map.put("userid", LoginActivity.user != null ? "" + LoginActivity.user.getUser_id() : "" + RegisterActivity.user.getUser_id());
                return map;
            }
        };
        queue.add(request);
    }

    public void changeData(List<Map<String, Object>> maps) {
        maps.clear();
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
            maps.add(map);
        }
    }

//    public List<Map<String, Object>> getData() {
//        mapList = new ArrayList<>();
//
//        for (int i = 0; i < 20; i++) {
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
}
