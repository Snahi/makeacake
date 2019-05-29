package com.snavi.makecake.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.snavi.makecake.R;
import com.snavi.makecake.sensorListeners.ShakeSensor;
import com.snavi.makecake.sensorListeners.ShakeSensorListener;

import java.util.ArrayList;

public class MixActivity extends AppCompatActivity implements ShakeSensorListener {


    // CONST //////////////////////////////////////////////////////////////////////////////////////
    public static final String PHOTOS_KEY       = "photos";
    public static final String FINAL_PHOTO_KEY  = "final_photo";
    private static final int MIN_SHAKE_TIME     = 5000;
    private static final int ANIMATION_INTERVAL = 100;
    private static final int SHOW_DURATION      = 4000;


    // fields ////////////////////////////////////////////////////////////////////////////////////
    private ArrayList<Integer> m_photos;
    private ShakeSensor        m_shakeListener;
    private long               m_shakeStart;
    private boolean            m_isShaking;
    private int                m_finalPhoto;

    // animation
    private long m_lastAnimation;
    private int  m_lastImgIdx;
    private ImageView m_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);

        Intent intent = getIntent();
        m_photos = intent.getIntegerArrayListExtra(PHOTOS_KEY);
        m_finalPhoto = intent.getIntExtra(FINAL_PHOTO_KEY, -1);

        m_shakeListener = new ShakeSensor(this);
        m_shakeListener.registerListener(this);

        m_img = findViewById(R.id.mix_img);
    }



    @Override
    public void shakeRead(boolean isShaking)
    {
        if (!m_isShaking && isShaking)
            m_shakeStart = System.currentTimeMillis();

        m_isShaking = isShaking;

        if (m_isShaking)
            animate();

        if (System.currentTimeMillis() - m_shakeStart > MIN_SHAKE_TIME && m_isShaking)
            showFinalCake();
    }



    private void animate()
    {
        if (System.currentTimeMillis() - m_lastAnimation >= ANIMATION_INTERVAL)
        {
            m_lastImgIdx = (m_lastImgIdx + 1) % m_photos.size();
            Bitmap img = BitmapFactory.decodeResource(getResources(), m_photos.get(m_lastImgIdx));
            m_img.setImageBitmap(img);

            m_lastAnimation = System.currentTimeMillis();
        }
    }



    private void showFinalCake()
    {
        m_img.setImageResource(m_finalPhoto);
        m_img.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
