package cqu.liuhang.summerproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.json.User;

/**
 * Created by LiuHang on 2017/6/30.
 **/

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;

    private Button jump;

    private ImageButton back;

    private EditText phone;

    private EditText pwd;

    final String AppKey = "1f34ed43e03f8";

    final String AppSecret = "ffd7f8896e01d45e6801f725c28c7c42";

    private EditText checkCode;

    private Button check;

    private Button login;

    private boolean flag = true;

    private Handler handler;

    private RequestQueue queue;

    public static User user;

    private boolean buttonFlag = true;

    final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();
        queue = Volley.newRequestQueue(this);
        title.setText("注册");
        jump.setText("");
        back.setOnClickListener(this);
        check.setOnClickListener(this);
        login.setOnClickListener(this);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 如果操作成功
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 校验验证码，返回校验的手机和国家代码
                        Log.d("tag", "验证成功");
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("tag", response);
                                if (response.equals("exsist")) {
                                    Toast.makeText(RegisterActivity.this, "账号已存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    Gson gson = new Gson();
                                    user = gson.fromJson(response, User.class);
                                    buttonFlag = true;
                                    login.setText("绑定手机登陆");
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RegisterActivity.this, "连接不到服务器", Toast.LENGTH_SHORT).show();
                                buttonFlag = true;
                                login.setText("绑定手机登陆");
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("rq", "register");
                                map.put("register", phone.getText().toString() + "+" + pwd.getText().toString());
//                            map.put("register", pwd.getText().toString());
                                return map;
                            }
                        };
                        queue.add(request);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        // 获取验证码成功，true为智能验证，false为普通下发短信
                        Log.d("tag", "验证码已发送");
//                        Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 如果操作失败
                    if (flag) {
                        Toast.makeText(RegisterActivity.this, "验证码获取失败", Toast.LENGTH_SHORT).show();
                        phone.requestFocus();
                    } else {
                        ((Throwable) data).printStackTrace();
                        Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        buttonFlag = true;
                        login.setText("绑定手机登陆");
                    }
                }
            }
        };

        /*
        第三方短信验证
         */
        MobSDK.init(this, AppKey, AppSecret);
        EventHandler eventHandler = new EventHandler() {    // 操作回调

            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);

    }

    private void findView() {
        title = (TextView) findViewById(R.id.activity_topbar_tv_title);
        jump = (Button) findViewById(R.id.activity_topbar_bt_jump);
        phone = (EditText) findViewById(R.id.activity_register_et_phone);
        pwd = (EditText) findViewById(R.id.activity_register_et_pwd);
        back = (ImageButton) findViewById(R.id.activity_topbar_ib_back);
        check = (Button) findViewById(R.id.activity_register_bt_check);
        login = (Button) findViewById(R.id.activity_register_bt_login);
        checkCode = (EditText) findViewById(R.id.activity_register_et_checkCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_topbar_ib_back:
                finish();
                break;
            case R.id.activity_register_bt_check:
//                Log.i("tag", "onClick: check");
                if (checkPhone(phone.getText().toString())) {
                    checkPause();
                    SMSSDK.getVerificationCode("86", phone.getText().toString());
//                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            code = response;
//                            Log.d("tag", code);
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(RegisterActivity.this, "连接不到服务器", Toast.LENGTH_SHORT).show();
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> map = new HashMap<>();
//                            map.put("rq", "checkCode");
//                            map.put("checkCode", "");
//                            return map;
//                        }
//                    };
//                    queue.add(request);
                } else {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    phone.setText("");
                }
                break;
            case R.id.activity_register_bt_login:
                if (buttonFlag) {
                    if (pwd.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
                        Toast.makeText(this, "手机号或密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        buttonFlag = false;
                        login.setText("验证中...");
                        SMSSDK.submitVerificationCode("86", phone.getText().toString(), checkCode.getText().toString());
                        flag = false;
//                    finish();
                    }
                }
                break;
            default:
        }
    }

    private void checkPause() {
        check.setClickable(false);
        check.setText("60s后再次获取");

        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                check.setText(millisUntilFinished / 1000 + "s后再次获取");
            }

            @Override
            public void onFinish() {
                check.setText("获取验证码");
                check.setClickable(true);
            }
        };
        timer.start();

//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case 1:
//                        check.setText(msg.arg1 + "s后可再次获取");
//                        break;
//                    case 2:
//                        check.setText("获取验证码");
//                        check.setClickable(true);
//                        break;
//                }
//            }
//        };
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    for (int i = 59; i >= 0; i--) {
//                        Thread.sleep(1000);
//                        Message message = new Message();
//                        message.what = 1;
//                        message.arg1 = i;
//                        handler.sendMessage(message);
//                    }
//                    Message message2 = new Message();
//                    message2.what = 2;
//                    handler.sendMessage(message2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    private boolean checkPhone(String s) {
        return !s.isEmpty() && s.matches("[1][358]\\d{9}");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
