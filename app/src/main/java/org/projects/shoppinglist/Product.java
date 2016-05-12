package org.projects.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Created by Gabor on 2016.04.28..
 */

public class Product implements Parcelable {

    String name;
    String quantity;
    String size;

    public Product(){

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {

        this.name = name;
    }
    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {

        this.quantity = quantity;
    }
    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {

        this.size = size;
    }


    public Product(String quantity, String size, String name)
    {
        this.name = name;
        this.quantity = quantity;
        this.size = size;
    }

    @Override
    //public String toString() {
      //  return quantity+" "+size+" "+name;


    public String toString(){

        if (quantity == null){
            return quantity + " " + name;
        } else {
            return quantity+" "+size+" " + name;
        }
    }






    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(size);
        dest.writeString(name);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    // "De-parcel object
    public Product(Parcel in) {
        quantity = in.readString();
        size = in.readString();
        name = in.readString();
    }
}
