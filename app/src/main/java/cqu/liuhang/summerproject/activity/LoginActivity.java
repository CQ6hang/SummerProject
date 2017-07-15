package cqu.liuhang.summerproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.json.User;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone;

    private EditText pwd;

    private Button login;

    private ImageButton back;

    private TextView title;

    private Button jump;

    private String phoneNum;

    private String password;

    public static User user;

    private boolean flag = true;

    private Button qq;

    private RequestQueue queue;

    final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        queue = Volley.newRequestQueue(this);
        title.setText("");
        jump.setText("注册");
        back.setVisibility(View.INVISIBLE);
        jump.setOnClickListener(this);
        login.setOnClickListener(this);
        qq.setOnClickListener(this);
    }

    private void findView() {
        phone = (EditText) findViewById(R.id.activity_login_et_phone);
        pwd = (EditText) findViewById(R.id.activity_login_et_pwd);
        login = (Button) findViewById(R.id.activity_login_bt_login);
        back = (ImageButton) findViewById(R.id.activity_topbar_ib_back);
        title = (TextView) findViewById(R.id.activity_topbar_tv_title);
        jump = (Button) findViewById(R.id.activity_topbar_bt_jump);
        qq = (Button) findViewById(R.id.qq);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_topbar_bt_jump:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.qq:
                otherLogin();
                break;
            case R.id.activity_login_bt_login:
                if (flag) {
                    phoneNum = phone.getText().toString();
                    password = pwd.getText().toString();
                    if (phoneNum.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "手机号或密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        login.setText("登陆中...");
                        sendLoginRequest();
                        flag = false;
                    }
                }
                break;
            default:
        }
    }

    private void otherLogin() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(false);  //设置false表示使用SSO授权方式
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d("tag", "授权成功");
                final String openId = platform.getDb().getUserId();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("false")) {
                            Toast.makeText(LoginActivity.this, "QQ登录失败，请重试", Toast.LENGTH_SHORT).show();
                        } else {
                            Gson gson = new Gson();
                            user = gson.fromJson(response, User.class);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("rq", "loginqq");
                        map.put("token", openId);
                        return map;
                    }
                };
                queue.add(request);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("tag", "授权失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调

        qq.authorize();//单独授权
        qq.showUser(null);//授权并获取用户信息
    }

    private void sendLoginRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("false")) {
                    Toast.makeText(LoginActivity.this, "手机号或密码错误", Toast.LENGTH_SHORT).show();
                    pwd.setText("");
                    login.setText("登陆");
                } else {
                    login.setText("登陆");
                    Gson gson = new Gson();
                    user = gson.fromJson(response, User.class);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                flag = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "连接不到服务器", Toast.LENGTH_SHORT).show();
                login.setText("登陆");
                flag = true;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("rq", "login");
                map.put("login", phoneNum + "+" + password);
                return map;
            }
        };
        queue.add(stringRequest);

    }
}
