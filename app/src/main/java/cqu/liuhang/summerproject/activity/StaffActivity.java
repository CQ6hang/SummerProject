package cqu.liuhang.summerproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.youth.banner.Banner;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.fragment.LobbyFragment;
import cqu.liuhang.summerproject.json.Staff;
import cqu.liuhang.summerproject.util.GlideImageLoader;

public class StaffActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout sellInfo;

    private LinearLayout hisStaff;

    private Banner banner;

    private TextView title;

    private TextView staffName;

    private TextView staffPrice;

    private TextView staffDetail;

    private int index;

    private ImageButton back;

    private TextView love;

    private ImageView loveImage;

    private LinearLayout loveLL;

    private boolean flag;

    private Staff staff;

    final String link = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";

    private List<Uri> uriList = new ArrayList<>();

    private RequestQueue queue;

    private Bundle bundle;

    String url = "http://192.168.191.1:8080/WebDemo/image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        queue = Volley.newRequestQueue(this);
        bundle = getIntent().getExtras();
//        Log.d("tag", ""+index);

        flag = bundle.getInt("flag") == 1 || bundle.getInt("flag") == 4 || bundle.getInt("flag") == 5;

        sellInfo = (LinearLayout) findViewById(R.id.activity_info_ll_sellerinfo);
        back = (ImageButton) findViewById(R.id.activity_topbar3_ib_back);
        hisStaff = (LinearLayout) findViewById(R.id.activity_staff_main_ll_staffs);
        staffName = (TextView) findViewById(R.id.content_name);
        loveLL = (LinearLayout) findViewById(R.id.activity_info_ll_love);
        staffDetail = (TextView) findViewById(R.id.content_detail);
        love = (TextView) findViewById(R.id.love_text);
        loveImage = (ImageView) findViewById(R.id.love_image);
        staffPrice = (TextView) findViewById(R.id.content_price);
        title = (TextView) findViewById(R.id.activity_topbar3_tv_title);
        title.setText("物品详情");
        banner = (Banner) findViewById(R.id.activity_staff_banner_staffpic);
        banner.setImageLoader(new GlideImageLoader());

        index = bundle.getInt("pos") - bundle.getInt("flag");

        switch (bundle.getInt("flag")) {
            case 0:
                index = bundle.getInt("pos");
                staff = SellerStaffActivity.staff;
                changeData();
                break;
            case 1:
                index = bundle.getInt("pos") - 1;
                staff = LobbyFragment.staff;
                changeData();
                break;
            case 3:
                index = bundle.getInt("pos");
                staff = ISellActivity.staff;
                changeData();
                break;
            case 4:
                index = bundle.getInt("pos");
                staff = ILoveActivity.staff;
                changeData();
                break;
            case 5:
                index = bundle.getInt("pos");
                staff = SearchActivity.staff;
                changeData();
                break;
            default:
        }

        banner.setImages(uriList);
        banner.start();

        back.setOnClickListener(this);
        sellInfo.setOnClickListener(this);
        hisStaff.setOnClickListener(this);
        loveLL.setOnClickListener(this);

    }

    private void changeData() {
        love.setText(staff.getStaffList().get(index).isIsCollect() ? "已收藏" : "收藏");
        loveImage.setImageResource(staff.getStaffList().get(index).isIsCollect() ? R.drawable.love_nor : R.drawable.love_sel);
        staffDetail.setText(staff.getStaffList().get(index).getStaff_detail());
        staffPrice.setText(staff.getStaffList().get(index).getStaff_price());
        staffName.setText(staff.getStaffList().get(index).getStaff_name());
        List<String> list = staff.getStaffList().get(index).getStaff_pic();
        for (String s : list) {
            uriList.add(Uri.parse(url.concat(s)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_info_ll_sellerinfo:
                Bundle bundleInfo = new Bundle();
                bundleInfo.putString("sellerID", LobbyFragment.staff.getStaffList().get(index).getSeller_id());
                Intent intent = new Intent(StaffActivity.this, SellerInfoActivity.class);
                intent.putExtras(bundleInfo);
                startActivity(intent);
                break;
            case R.id.activity_staff_main_ll_staffs:
                if (flag) {
                    Bundle bundleStaff = new Bundle();
                    bundleStaff.putString("sellerID", LobbyFragment.staff.getStaffList().get(index).getSeller_id());
                    Intent intent2 = new Intent(StaffActivity.this, SellerStaffActivity.class);
                    intent2.putExtras(bundleStaff);
                    startActivity(intent2);
                } else {
                    finish();
                }
                break;
            case R.id.activity_topbar3_ib_back:
                finish();
                break;
            case R.id.ibuy:

                break;
            case R.id.activity_info_ll_love:
                StringRequest request = new StringRequest(Request.Method.POST, link, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("true")) {
                            if (staff.getStaffList().get(index).isIsCollect()) {
                                staff.getStaffList().get(index).setIsCollect(false);
                                love.setText("收藏");
                                loveImage.setImageResource(R.drawable.love_sel);
                            } else {
                                staff.getStaffList().get(index).setIsCollect(true);
                                love.setText("已收藏");
                                loveImage.setImageResource(R.drawable.love_nor);
                            }
                        } else {
                            Toast.makeText(StaffActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StaffActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("rq", "docollect");
                        map.put("staffid", staff.getStaffList().get(index).getStaff_id());
                        map.put("userid", LoginActivity.user != null ? "" + LoginActivity.user.getUser_id() : "" + RegisterActivity.user.getUser_id());
                        return map;
                    }
                };
                queue.add(request);
                break;
            default:
        }
    }
}
