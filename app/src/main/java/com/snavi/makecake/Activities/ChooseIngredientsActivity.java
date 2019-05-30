package com.snavi.makecake.Activities;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import com.snavi.makecake.cooking.ChooseIngredientsView;
import com.snavi.makecake.sensorListeners.ProximitySensorListener;
import java.util.ArrayList;

public class ChooseIngredientsActivity extends AppCompatActivity {


    // CONST //////////////////////////////////////////////////////////////////////////////////////
    public static final String INGREDIENTS_KEY = "ingredients";
    public static final String MIX_KEY         = "mix";
    public static final String FINAL_IMG_KEY   = "final_img_key";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProximitySensorListener proximitySensorListener = new ProximitySensorListener(this);
        int[] screenDims               = getScreenDimensions();
        Intent intent                  = getIntent();
        ArrayList<Integer> ingredients = intent.getIntegerArrayListExtra(INGREDIENTS_KEY);
        ArrayList<Integer> mixImages   = intent.getIntegerArrayListExtra(MIX_KEY);
        int finalImg = intent.getIntExtra(FINAL_IMG_KEY, -1);

        ChooseIngredientsView view = new ChooseIngredientsView(this, ingredients, mixImages,
                screenDims[0], screenDims[1], proximitySensorListener, finalImg);

        setContentView(view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



    @Override
    public void onPause()
    {
        super.onPause();
        finish();
    }



    private int[] getScreenDimensions()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return new int[]{size.x, size.y};
    }


}
