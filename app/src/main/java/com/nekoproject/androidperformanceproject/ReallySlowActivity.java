package com.nekoproject.androidperformanceproject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class ReallySlowActivity extends AppCompatActivity {

    String[] fileList;
    ListView listview;
    static Bitmap wrongBitmapNotReleased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        StrictMode.noteSlowCall("onCreate");
        setContentView(R.layout.activity_lag);
        listview = (ListView)findViewById(R.id.list_view);

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                AssetManager am = getAssets();
                try {
                    fileList = am.list("testimages");

                    //First bitmap
                    String path = fileList[0];

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
        //task.execute();

        AssetManager am = getAssets();
        try {
            fileList = am.list("testimages");

            //First bitmap
            String path = fileList[0];

        }catch (IOException e){
            Log.e("error", "err", e);
        }
        MemoryLeakAdapter adapter = new MemoryLeakAdapter();
        listview.setAdapter(adapter);

        try {
            Thread.sleep(1000);
        }catch (Exception e){

        }
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
            return fileList.length;
        }

        @Override
        public Object getItem(int position) {
            return fileList[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            StrictMode.noteSlowCall("getView");
            View view = View.inflate(ReallySlowActivity.this, R.layout.item_lag, null);
            final TextView tv = (TextView)view.findViewById(R.id.display_text);
            final ImageView iv = (ImageView)view.findViewById(R.id.image_view);
            final LagView lv = (LagView)view.findViewById(R.id.lag_view);
            final String path = (String)getItem(position);
            /*AsyncTask task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    try {
                        AssetManager am = getAssets();
                        InputStream stream = am.open("testimages/" + path);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        Bitmap b = BitmapFactory.decodeStream(stream, null, options);
                        wrongBitmapNotReleased = b;
                        return b;

                    }catch (OutOfMemoryError e){
                        return "OutOfMemoryError";
                    } catch (Exception e){
                        return e.getMessage();
                    }
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);

                    if(o!=null) {
                        if(o instanceof Bitmap)
                            iv.setImageBitmap((Bitmap)o);
                        if(o instanceof String)
                            tv.setText((String)o);
                    }
                }
            };
            task.execute();*/

            try {
                AssetManager am = getAssets();
                InputStream stream = am.open("testimages/" + path);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap b = BitmapFactory.decodeStream(stream, null, options);
                wrongBitmapNotReleased = b;
                iv.setImageBitmap(wrongBitmapNotReleased);
                String text = (String)getItem(position);
                tv.setText(text);

            }catch (OutOfMemoryError e){
                tv.setText("OutOfMemoryError");
            } catch (Exception e){
                tv.setText(e.getMessage());
            }

            lv.setText(String.valueOf(position));
            convertView = view;
            return convertView;
        }
    }
}
