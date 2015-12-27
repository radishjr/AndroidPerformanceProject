package com.nekoproject.androidperformanceproject;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LagActivity extends AppCompatActivity {

    ListView listview;
    List<String> textList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lag);
        listview = (ListView)findViewById(R.id.list_view);


        AsyncTask task = new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                AssetManager am = getAssets();
                try {
                    String[] fileList = am.list("");
                    for(String path : fileList){
                        if(path.endsWith(".jpg")){
                            Bitmap b = BitmapFactory.decodeStream(am.open(path));
                            int sampleSize = b.getWidth() / 200;
                            b.recycle();
                        }
                    }
                }catch (Exception e){
                    Log.e("test", "error", e);
                }
                return null;
            }
        };
        task.execute();


        LagAdapter adapter = new LagAdapter();
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lag, menu);
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

    class LagAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return textList.size();
        }

        @Override
        public Object getItem(int position) {
            return textList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(LagActivity.this, R.layout.item_lag, null);
            TextView tv = (TextView)view.findViewById(R.id.display_text);
            String text = (String)getItem(position);
            tv.setText(text);
            convertView = view;
            return convertView;
        }
    }
}
