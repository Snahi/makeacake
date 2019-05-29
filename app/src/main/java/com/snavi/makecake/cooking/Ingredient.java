package com.snavi.makecake.cooking;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {


    private int m_image;

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>(){

        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[0];
        }
    };


    public Ingredient(int image)
    {
        m_image = image;
    }



    public Ingredient(Parcel parcel)
    {
        m_image = parcel.readInt();
    }



    @Override
    public int describeContents()
    {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(m_image);
    }



    // Getters ////////////////////////////////////////////////////////////////////////////////////



    public int getImage()
    {
        return m_image;
    }
}
