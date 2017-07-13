package cqu.liuhang.summerproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youth.banner.Banner;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

    private List<Uri> uriList = new ArrayList<>();

    private boolean flag;

    String url = "http://192.168.191.1:8080/WebDemo/image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        Bundle bundle = getIntent().getExtras();

        flag = bundle.getInt("flag") == 1;
//        Log.d("tag", ""+index);
        sellInfo = (LinearLayout) findViewById(R.id.activity_info_ll_sellerinfo);
        back = (ImageButton) findViewById(R.id.activity_topbar3_ib_back);
        hisStaff = (LinearLayout) findViewById(R.id.activity_staff_main_ll_staffs);
        staffName = (TextView) findViewById(R.id.content_name);
        staffDetail = (TextView) findViewById(R.id.content_detail);
        staffPrice = (TextView) findViewById(R.id.content_price);
        title = (TextView) findViewById(R.id.activity_topbar3_tv_title);
        title.setText("物品详情");
        banner = (Banner) findViewById(R.id.activity_staff_banner_staffpic);
        banner.setImageLoader(new GlideImageLoader());

        index = bundle.getInt("pos") - bundle.getInt("flag");
        if (flag) {
            staffDetail.setText(LobbyFragment.staff.getStaffList().get(index).getStaff_detail());
            staffPrice.setText(LobbyFragment.staff.getStaffList().get(index).getStaff_price());
            staffName.setText(LobbyFragment.staff.getStaffList().get(index).getStaff_name());
            List<String> list = LobbyFragment.staff.getStaffList().get(index).getStaff_pic();
            for (String s : list) {
                uriList.add(Uri.parse(url.concat(s)));
            }
        } else {
            staffDetail.setText(SellerStaffActivity.staff.getStaffList().get(index).getStaff_detail());
            staffPrice.setText(SellerStaffActivity.staff.getStaffList().get(index).getStaff_price());
            staffName.setText(SellerStaffActivity.staff.getStaffList().get(index).getStaff_name());
            List<String> list = SellerStaffActivity.staff.getStaffList().get(index).getStaff_pic();
            for (String s : list) {
                uriList.add(Uri.parse(url.concat(s)));
            }
        }

        banner.setImages(uriList);
        banner.start();

        back.setOnClickListener(this);
        sellInfo.setOnClickListener(this);
        hisStaff.setOnClickListener(this);
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
            default:
        }
    }
}
