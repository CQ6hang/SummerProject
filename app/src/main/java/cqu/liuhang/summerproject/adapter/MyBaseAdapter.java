package cqu.liuhang.summerproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cqu.liuhang.summerproject.R;

/**
 * Created by LiuHang on 2017/7/5.
 **/

public class MyBaseAdapter extends BaseAdapter {

    List<Map<String, Object>> list = new ArrayList<>();

    Context context;

    RequestQueue queue;

    String url = "http://192.168.191.1:8080/WebDemo/image";

    public MyBaseAdapter(Context context, List<Map<String, Object>> list, RequestQueue queue) {
        this.list = list;
        this.context = context;
        this.queue = queue;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
//            Log.d("tag", "bind component");
            convertView = inflater.inflate(R.layout.listview_staffitem, null);
            holder = new ViewHolder();
            holder.pic = (ImageView) convertView.findViewById(R.id.listview_staffitem_iv_staffPic);
            holder.detail = (TextView) convertView.findViewById(R.id.listview_staffitem_tv_detail);
            holder.price = (TextView) convertView.findViewById(R.id.listview_staffitem_tv_staffprice);
            holder.loveCnt = (TextView) convertView.findViewById(R.id.listview_staffitem_tv_lovecount);
            holder.name = (TextView) convertView.findViewById(R.id.listview_staffitem_tv_StaffName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (list.get(position).get("pic").equals("null")) {
            holder.pic.setImageResource(R.drawable.welcome1);
        } else {
            String newURL;
            newURL = url.concat((String) list.get(position).get("pic"));
//            Log.d("URL", newURL);
            ImageLoader loader = new ImageLoader(queue, new BitmapCache());
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.pic, R.drawable.welcome2, R.drawable.welcome3);
            loader.get(newURL, listener);
        }


        holder.detail.setText((String) list.get(position).get("detail"));
        holder.price.setText((String) list.get(position).get("price"));
//        Log.d("zero", "" + list.get(position).get("loveCnt"));
        holder.loveCnt.setText((String) list.get(position).get("loveCnt"));
        holder.name.setText((String) list.get(position).get("name"));
        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        ImageView pic;
        TextView detail;
        TextView price;
        TextView loveCnt;
    }

    public static class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }
}
