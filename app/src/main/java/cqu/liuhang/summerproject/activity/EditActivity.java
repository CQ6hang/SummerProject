package cqu.liuhang.summerproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.adapter.MyBaseAdapter;
import cqu.liuhang.summerproject.json.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton back;

    private TextView title;

    private User user;

    private ProgressBar progressBar;

    private Button editButton;

    private CircleImageView headPic;

    private EditText name;

    private EditText sex;

    private EditText age;

    private TextView phone;

    private EditText address;

    private RequestQueue queue;

    private TextView progress;

    final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    final String imageurl = "http://192.168.191.1:8080/WebDemo/image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        queue = Volley.newRequestQueue(this);

        user = LoginActivity.user != null ? LoginActivity.user : RegisterActivity.user;

        back = (ImageButton) findViewById(R.id.activity_topbar3_ib_back);
        title = (TextView) findViewById(R.id.activity_topbar3_tv_title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editButton = (Button) findViewById(R.id.activity_edit_bt_edit);
        progress = (TextView) findViewById(R.id.edit_progress);
        headPic = (CircleImageView) findViewById(R.id.edit_headPic);
        name = (EditText) findViewById(R.id.edit_name);
        sex = (EditText) findViewById(R.id.edit_sex);
        age = (EditText) findViewById(R.id.edit_age);
        phone = (TextView) findViewById(R.id.edit_phone);
        address = (EditText) findViewById(R.id.edit_address);

        back.setOnClickListener(this);
        editButton.setOnClickListener(this);
        title.setText("修改信息");

        String newURL;
        newURL = imageurl.concat(user.getHeadimage());
//            Log.d("URL", newURL);
        ImageLoader loader = new ImageLoader(queue, new MyBaseAdapter.BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(headPic, R.drawable.welcome2, R.drawable.welcome3);
        loader.get(newURL, listener);

        name.setText(user.getUser_name());
        sex.setText(user.getSex());
        age.setText(user.getAge() + "");
        phone.setText(user.getPhone());
        address.setText(user.getAddress());
        progressBar.setProgress(100);
        progress.setText("完整度 " + 100);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_topbar3_ib_back:
                finish();
                break;
            case R.id.activity_edit_bt_edit:
                user.setUser_name(name.getText().toString());
                user.setAddress(address.getText().toString());
                user.setAge(Integer.getInteger(age.getText().toString()));
                user.setSex(sex.getText().toString());
//                user.setHeadimage();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("true")) {
                            Toast.makeText(EditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
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
                        map.put("rq", "edit");
                        Gson gson = new Gson();
                        map.put("edit", gson.toJson(user));
                        return map;
                    }
                };
                queue.add(request);
                break;
            default:
        }
    }
}
