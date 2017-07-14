package cqu.liuhang.summerproject.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import cqu.liuhang.summerproject.activity.LoginActivity;
import cqu.liuhang.summerproject.activity.MainActivity;
import cqu.liuhang.summerproject.activity.RegisterActivity;
import cqu.liuhang.summerproject.activity.WelcomeActivity;
import cqu.liuhang.summerproject.json.Staff;
import cqu.liuhang.summerproject.util.Bitmap2StringUtils;

/**
 * Created by LiuHang on 2017/7/2.
 **/

public class SellFragment extends Fragment implements View.OnClickListener {

    private Button edit;

    private ImageButton cancel;

    private EditText staffName;

    private EditText staffDetail;

    private Button addPic;

    private EditText price;

    private Button sure;

    private RequestQueue queue;

    private List<String> imageString = new ArrayList<>();

    private ImageView pic1;

    private ImageView pic2;

    private ImageView pic3;

    private int cnt;

    final String url = "http://192.168.191.1:8080/WebDemo/servlet/AServlet";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        pic1 = (ImageView) view.findViewById(R.id.fragment_sell_bt_pic1);
        pic2 = (ImageView) view.findViewById(R.id.fragment_sell_bt_pic2);
        pic3 = (ImageView) view.findViewById(R.id.fragment_sell_bt_pic3);
        edit = (Button) view.findViewById(R.id.activity_topbar2_bt_edit);
        cancel = (ImageButton) view.findViewById(R.id.activity_topbar2_ib_cancel);

        staffName = (EditText) view.findViewById(R.id.fragment_sell_ev_staffName);
        staffDetail = (EditText) view.findViewById(R.id.fragment_sell_ev_staffDetail);
        price = (EditText) view.findViewById(R.id.ev_rmb);

        addPic = (Button) view.findViewById(R.id.fragment_sell_bt_addPic);
        sure = (Button) view.findViewById(R.id.fragent_sell_bt_sure);

        queue = Volley.newRequestQueue(getActivity());

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        edit.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        addPic.setOnClickListener(this);
        sure.setOnClickListener(this);
        cnt = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_sell_bt_addPic:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.fragent_sell_bt_sure:
                if (checkEmpty()) {
                    Toast.makeText(getActivity(), "请完善物品信息", Toast.LENGTH_SHORT).show();
                    break;
                }
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("true")) {
                            Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                            clearPage();
                            MainActivity.pages.setCurrentItem(1);
                        } else {
                            Toast.makeText(getActivity(), "发布失败", Toast.LENGTH_SHORT).show();
                        }
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
                        map.put("rq", "sale");
                        map.put("sale", packageData());
                        return map;
                    }
                };
                queue.add(request);
                break;
            default:
        }
    }

    private boolean checkEmpty() {
        return staffName.getText().toString().isEmpty() || staffDetail.getText().toString().isEmpty()
                || price.getText().toString().isEmpty() || imageString.isEmpty();
    }

    private void clearPage() {
        // 清空页面
        staffName.setText("");
        staffDetail.setText("");
        price.setText("");
        pic1.setVisibility(View.GONE);
        pic2.setVisibility(View.GONE);
        pic3.setVisibility(View.GONE);
        addPic.setVisibility(View.VISIBLE);
        cnt = 0;
    }

    private String packageData() {
        Gson gson = new Gson();

        Staff staff = new Staff();

        Staff.StaffListBean bean = new Staff.StaffListBean();

        List<Staff.StaffListBean> beanList = new ArrayList<>();

        bean.setCollectnum("0");
        bean.setStaff_id("0");
        bean.setStaff_date("0");

        bean.setStaff_detail(staffDetail.getText().toString());
        bean.setStaff_name(staffName.getText().toString());
        bean.setStaff_price(price.getText().toString());

        if (LoginActivity.user != null) {
            bean.setSeller_id(Integer.toString(LoginActivity.user.getUser_id()));
        } else {
            bean.setSeller_id(Integer.toString(RegisterActivity.user.getUser_id()));
        }

        bean.setStaff_pic(imageString);

        beanList.add(bean);
        staff.setStaffList(beanList);
        return gson.toJson(staff);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            if (c != null) {
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);

                String imagePath = c.getString(columnIndex);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
                BitmapFactory.decodeFile(imagePath, options);
                int height = options.outHeight;
                int width = options.outWidth;
                int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
                int minLen = Math.min(height, width); // 原图的最小边长
                if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                    float ratio = (float) minLen / 100.0f; // 计算像素压缩比例
                    inSampleSize = (int) ratio;
                }
                options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
                options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options); // 解码文件

                setPic(bitmap);

                imageString.add(Bitmap2StringUtils.convertIconToString(bitmap));
                c.close();
            }
        }
    }

    private void setPic(Bitmap pic) {
        switch (cnt++) {
            case 0:
                Log.d("cnt", "setPic: 0");
                pic1.setBackground(new BitmapDrawable(pic));
                pic1.setVisibility(View.VISIBLE);
                break;
            case 1:
                Log.d("cnt", "setPic: 1");
                pic2.setBackground(new BitmapDrawable(pic));
                pic2.setVisibility(View.VISIBLE);
                break;

            case 2:
                Log.d("cnt", "setPic: 2");
                pic3.setBackground(new BitmapDrawable(pic));
                pic3.setVisibility(View.VISIBLE);
                addPic.setVisibility(View.GONE);
                break;
            default:
        }
    }
}


