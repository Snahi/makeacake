package com.snavi.makecake.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.snavi.makecake.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStrawberryPieOnClick();
        setPinkPieOnClick();
        setCherryPieOnClick();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



    private void setStrawberryPieOnClick()
    {
        ImageView img = findViewById(R.id.main_img_strawberry_pie);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCooking(ingredientsForStrawberryPie(), R.drawable.chocolate_strawberry);
            }
        });
    }



    private void setPinkPieOnClick()
    {
        ImageView img = findViewById(R.id.main_img_pink_pie);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCooking(ingredientsForPinkPie(), R.drawable.pink);
            }
        });
    }



    private void setCherryPieOnClick()
    {
        ImageView img = findViewById(R.id.main_img_cherry_pie);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCooking(ingredientsForCherryPie(), R.drawable.chocolate_with_cherry);
            }
        });
    }



    private void startCooking(ArrayList<Integer> ingredients, int finalImage)
    {
        showIngredientsToRemember(ingredients, finalImage);
    }



    private void showIngredientsToRemember(ArrayList<Integer> ingredients, int finalImage)
    {
        Intent intent = new Intent(this, NeccessaryIngredientsActivity.class);
        intent.putIntegerArrayListExtra(NeccessaryIngredientsActivity.INGREDIENTS_KEY, ingredients);
        intent.putIntegerArrayListExtra(NeccessaryIngredientsActivity.MIX_KEY, mixImages());
        intent.putExtra(NeccessaryIngredientsActivity.FINAL_IMG_KEY, finalImage);
        startActivity(intent);
    }



    private ArrayList<Integer> ingredientsForStrawberryPie()
    {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(R.drawable.chocolate);
        res.add(R.drawable.egg);
        res.add(R.drawable.flour);
        res.add(R.drawable.hazelnut);
        res.add(R.drawable.sugar);
        res.add(R.drawable.strawberry);

        return res;
    }



    private ArrayList<Integer> ingredientsForCherryPie()
    {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(R.drawable.chocolate);
        res.add(R.drawable.egg);
        res.add(R.drawable.cherry);

        return res;
    }



    private ArrayList<Integer> ingredientsForPinkPie()
    {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(R.drawable.egg);
        res.add(R.drawable.flour);
        res.add(R.drawable.sugar);
        res.add(R.drawable.cherry);
        res.add(R.drawable.raspberry);

        return res;
    }



    private ArrayList<Integer> mixImages()
    {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(R.drawable.bowl_white_1);
        res.add(R.drawable.bowl_white_2);
        res.add(R.drawable.bowl_white_3);
        res.add(R.drawable.bowl_white_4);
        res.add(R.drawable.bowl_white_5);
        res.add(R.drawable.bowl_white_6);
        res.add(R.drawable.bowl_white_7);
        res.add(R.drawable.bowl_white_8);
        res.add(R.drawable.bowl_white_9);
        res.add(R.drawable.bowl_white_10);
        res.add(R.drawable.bowl_white_11);
        res.add(R.drawable.bowl_white_12);
        res.add(R.drawable.bowl_white_13);

        return res;
    }

}
