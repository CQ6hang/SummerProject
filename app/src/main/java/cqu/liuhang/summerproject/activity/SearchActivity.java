package cqu.liuhang.summerproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cqu.liuhang.summerproject.R;

public class SearchActivity extends BaseActivity {

    private Button cancel;

    private ListView history;

    private List<Map<String, Object>> mapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cancel = (Button) findViewById(R.id.activity_search_bt_cancel);
        history = (ListView) findViewById(R.id.activity_search_history);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        history.setAdapter(new SimpleAdapter(this, getData(), R.layout.listview_searchhistory,
                new String[]{"search", "del"}, new int[]{R.id.searchItem, R.id.delHistory}));
    }

    private List<Map<String, Object>> getData() {
        mapList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("search", "潘谊同款飞机杯");
            map.put("del", R.mipmap.ic_launcher_round);
            mapList.add(map);
        }

        return mapList;
    }


}
