package com.example.guki;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

public class Panel extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));
    }

    class DrawView extends View
    {
        public DrawView(Context context)
        {
            super(context);
        }

        void Draw (Canvas c) {
            Bitmap redGukLive = BitmapFactory.decodeResource(getResources(), R.drawable.ic_redGukLive);

        }
    }
}
