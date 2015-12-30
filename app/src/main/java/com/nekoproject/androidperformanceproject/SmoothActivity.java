package com.nekoproject.androidperformanceproject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SmoothActivity extends AppCompatActivity {

    List<String> fileList = new ArrayList<>();
    ListView listview;
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.noteSlowCall("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lag);
        listview = (ListView)findViewById(R.id.list_view);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        ImageLoader.getInstance().pause();
                        break;
                    default:
                        ImageLoader.getInstance().resume();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        if(!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(SmoothActivity.this)
                    .memoryCacheExtraOptions(200, 200)
                    .memoryCacheSize(20 * 1024 * 1024)
                    .memoryCacheSizePercentage(50)
                    .build();
            ImageLoader.getInstance().init(config);
        }

        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.mipmap.loading__img)
                .showImageOnFail(R.mipmap.missing__img)
                .cacheInMemory(true)
                .build();

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                AssetManager am = getAssets();
                try {
                    String[] list = am.list("testimages");

                    for(String file : list) {
                        File f = new File(getCacheDir() + "/" + file);
                        if (!f.exists()) try {

                            InputStream is = getAssets().open("testimages/" + file);
                            int size = is.available();
                            byte[] buffer = new byte[size];
                            is.read(buffer);
                            is.close();

                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(buffer);
                            fos.close();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        fileList.add("file:///" + f.getAbsolutePath());
                    }

                }catch (IOException e){
                    Log.e("error", "err", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                MemoryLeakAdapter adapter = new MemoryLeakAdapter();
                listview.setAdapter(adapter);
            }
        };
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memory_leak, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MemoryLeakAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return fileList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder{
            TextView tv;
            ImageView iv;
            SmoothView sv;
            int position;
            String path;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            StrictMode.noteSlowCall("getView");

            final ViewHolder vh;
            if(convertView == null) {
                convertView = View.inflate(SmoothActivity.this, R.layout.item_smooth, null);
                vh = new ViewHolder();
                vh.tv = (TextView) convertView.findViewById(R.id.display_text);
                vh.iv = (ImageView) convertView.findViewById(R.id.image_view);
                vh.sv = (SmoothView) convertView.findViewById(R.id.smooth_view);
                convertView.setTag(vh);
            }else{
                vh = (ViewHolder)convertView.getTag();
                ImageLoader.getInstance().cancelDisplayTask(vh.iv);
            }

            final String path = (String)getItem(position);
            vh.iv.setTag(position);

            ImageLoader.getInstance().displayImage(path, vh.iv, options);

            String text = (String)getItem(position);
            vh.tv.setText(text);
            vh.sv.setText(String.valueOf(position));
            return convertView;
        }
    }
}
