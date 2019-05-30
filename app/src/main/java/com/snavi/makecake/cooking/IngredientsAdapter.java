package com.snavi.makecake.cooking;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {


    private ArrayList<Integer> m_ingredients;


    public IngredientsAdapter(ArrayList<Integer> ingredients)
    {
        m_ingredients = ingredients;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parentView, int i)
    {
        ImageView imageView = new ImageView(parentView.getContext());
        return new ViewHolder(imageView);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        ImageView imageView = viewHolder.getImageView();
        Drawable img = ContextCompat.getDrawable(viewHolder.getImageView().getContext(),
                m_ingredients.get(i));
        imageView.setImageDrawable(img);
    }



    @Override
    public int getItemCount()
    {
        return m_ingredients.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView m_imageView;

        ViewHolder(ImageView imageView)
        {
            super(imageView);

            m_imageView = imageView;
        }

        private ImageView getImageView() {return m_imageView;}
    }
}
