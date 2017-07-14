package cqu.liuhang.summerproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;
import java.util.Map;

import cqu.liuhang.summerproject.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LiuHang on 2017/7/11.
 **/

public class CommentAdapter extends BaseAdapter {

    private Context context;

    List<Map<String, Object>> mapList;

    RequestQueue queue;

    private String url = "http://192.168.191.1:8080/WebDemo/image";

    public CommentAdapter(Context context, List<Map<String, Object>> list, RequestQueue queue) {
        this.context = context;
        this.mapList = list;
        this.queue = queue;
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_commentitem, null);
            holder = new ViewHolder();
            holder.headPic = (CircleImageView) convertView.findViewById(R.id.listview_mymessageitem_headpic);
            holder.name = (TextView) convertView.findViewById(R.id.listview_mymessageitem_sellername);
            holder.date = (TextView) convertView.findViewById(R.id.listview_mymessageitem_time);
            holder.comment = (TextView) convertView.findViewById(R.id.listview_commentlist_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mapList.get(position).get("headPic").equals("null")) {
            holder.headPic.setImageResource(R.drawable.welcome1);
        } else {
            String newURL;
            newURL = url.concat((String) mapList.get(position).get("headPic"));
//            Log.d("URL", newURL);
            ImageLoader loader = new ImageLoader(queue, new MyBaseAdapter.BitmapCache());
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.headPic, R.drawable.welcome2, R.drawable.welcome3);
            loader.get(newURL, listener);
        }

        holder.name.setText((String) mapList.get(position).get("name"));
        holder.date.setText((String) mapList.get(position).get("date"));
        holder.comment.setText((String) mapList.get(position).get("comment"));


        return convertView;
    }

    private static class ViewHolder {
        CircleImageView headPic;
        TextView name;
        TextView date;
        TextView comment;
    }
}
