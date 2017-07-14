package cqu.liuhang.summerproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cqu.liuhang.summerproject.R;
import cqu.liuhang.summerproject.activity.EditActivity;
import cqu.liuhang.summerproject.activity.ILoveActivity;
import cqu.liuhang.summerproject.activity.ISellActivity;
import cqu.liuhang.summerproject.activity.LoginActivity;
import cqu.liuhang.summerproject.activity.MyCommentActivity;
import cqu.liuhang.summerproject.activity.RegisterActivity;
import cqu.liuhang.summerproject.adapter.MyBaseAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LiuHang on 2017/7/2.
 **/

public class InfoFragment extends Fragment implements View.OnClickListener {


    private ImageButton button;

    private TextView title;

    private Button edit;

    private LinearLayout iSell;

    private LinearLayout iCollect;

    private LinearLayout comment;

    private CircleImageView headPic;

    private RequestQueue queue;

    static private TextView name;

    private String url = "http://192.168.191.1:8080/WebDemo/image";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        queue = Volley.newRequestQueue(getActivity());
        button = (ImageButton) view.findViewById(R.id.activity_topbar2_ib_cancel);
        title = (TextView) view.findViewById(R.id.activity_topbar2_tv_title);
        edit = (Button) view.findViewById(R.id.activity_topbar2_bt_edit);

        iSell = (LinearLayout) view.findViewById(R.id.activity_info_lo_iSell);
        iCollect = (LinearLayout) view.findViewById(R.id.activity_info_lo_iCollect);
        comment = (LinearLayout) view.findViewById(R.id.activity_info_lo_comment);

        headPic = (CircleImageView) view.findViewById(R.id.activity_info_head);
        name = (TextView) view.findViewById(R.id.activity_info_userName);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button.setVisibility(View.INVISIBLE);
        title.setText("");
        edit.setOnClickListener(this);
        iSell.setOnClickListener(this);
        iCollect.setOnClickListener(this);
        comment.setOnClickListener(this);
        name.setText(LoginActivity.user != null ? "" + LoginActivity.user.getUser_name() : "" + RegisterActivity.user.getUser_name());

        String newURL;
        newURL = url.concat(LoginActivity.user != null ? "" + LoginActivity.user.getHeadimage() : "" + RegisterActivity.user.getHeadimage());
//            Log.d("URL", newURL);
        ImageLoader loader = new ImageLoader(queue, new MyBaseAdapter.BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(headPic, R.drawable.welcome2, R.drawable.welcome3);
        loader.get(newURL, listener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_info_lo_iSell:
                Intent intent = new Intent(getActivity(), ISellActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_info_lo_iCollect:
                Intent intent2 = new Intent(getActivity(), ILoveActivity.class);
                startActivity(intent2);
                break;
            case R.id.activity_info_lo_comment:
                Intent intent3 = new Intent(getActivity(), MyCommentActivity.class);
                startActivity(intent3);
                break;
            case R.id.activity_topbar2_bt_edit:
                Intent intent4 = new Intent(getActivity(), EditActivity.class);
                startActivity(intent4);
                break;
            default:
        }
    }

    public static void refresh() {
        name.setText(LoginActivity.user != null ? "" + LoginActivity.user.getUser_name() : "" + RegisterActivity.user.getUser_name());
    }
}
