package com.snavi.makecake.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.snavi.makecake.R;
import com.snavi.makecake.cooking.ChooseIngredientsView;
import com.snavi.makecake.cooking.Ingredient;
import com.snavi.makecake.cooking.IngredientsAdapter;

import java.util.ArrayList;

public class NeccessaryIngredientsActivity extends AppCompatActivity {


    // CONST //////////////////////////////////////////////////////////////////////////////////////
    public static final int    NUM_OF_COLS     = 5;
    public static final String INGREDIENTS_KEY = "ingredients";
    public static final String MIX_KEY         = "mix";
    public static final String FINAL_IMG_KEY   = "final_img";


    // fields /////////////////////////////////////////////////////////////////////////////////////
    private RecyclerView               m_recyclerView;
    private RecyclerView.Adapter       m_adapter;
    private RecyclerView.LayoutManager m_manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neccessary_ingredients);

        Intent intent = getIntent();
        ArrayList<Integer> ingredients = intent.getIntegerArrayListExtra(INGREDIENTS_KEY);
        ArrayList<Integer> mix         = intent.getIntegerArrayListExtra(MIX_KEY);

        m_recyclerView  = findViewById(R.id.necessary_ingredients_recycler_view);
        m_recyclerView.setHasFixedSize(true);
        m_manager       = new LinearLayoutManager(this);
        ((LinearLayoutManager) m_manager).setOrientation(LinearLayoutManager.HORIZONTAL);
        m_recyclerView.setLayoutManager(m_manager);
        m_adapter       = new IngredientsAdapter(ingredients);
        m_recyclerView.setAdapter(m_adapter);

        setRememberButtonOnClick(ingredients, mix);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



    private void setRememberButtonOnClick(final ArrayList<Integer> ingredients, final ArrayList<Integer> mix)
    {
        Button but = findViewById(R.id.necessary_ingredients_but_remember);
        but.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startChooseActivity(ingredients, mix);
            }
        });
    }



    private void startChooseActivity(ArrayList<Integer> ingredients, ArrayList<Integer> mix)
    {
        int finalImg = getIntent().getIntExtra(FINAL_IMG_KEY, -1);
        Intent intent = new Intent(this, ChooseIngredientsActivity.class);
        intent.putIntegerArrayListExtra(ChooseIngredientsActivity.INGREDIENTS_KEY, ingredients);
        intent.putIntegerArrayListExtra(ChooseIngredientsActivity.MIX_KEY, mix);
        intent.putExtra(ChooseIngredientsActivity.FINAL_IMG_KEY, finalImg);
        startActivity(intent);
        finish();
    }

}
