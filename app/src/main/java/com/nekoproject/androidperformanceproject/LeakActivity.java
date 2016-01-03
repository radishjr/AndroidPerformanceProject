package com.nekoproject.androidperformanceproject;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class LeakActivity extends AppCompatActivity {
    //private static Drawable sBackground;

    static private InnerLeakClass mObj;
    class InnerLeakClass{

    }

    private Bitmap mBitmap;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mObj == null) {
            mObj = new InnerLeakClass();
        }

        AssetManager am = getAssets();
        try {
            InputStream stream = am.open("testimages/img001.jpg");
            //stream.close();
            mBitmap = BitmapFactory.decodeStream(stream);
        }catch (IOException e){
            Log.e("exception", "err", e);
        }

        ImageView view = new ImageView(this);
        view.setImageBitmap(mBitmap);
        setContentView(view);
    }
}