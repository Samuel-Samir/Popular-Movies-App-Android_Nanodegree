package com.example.android.popularmoviesapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by samuel on 3/4/2017.
 */

public class MovietTrailers implements Parcelable{
    public String key ;
    public  String name;

    public MovietTrailers ()
    {

    }
    protected MovietTrailers(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<MovietTrailers> CREATOR = new Creator<MovietTrailers>() {
        @Override
        public MovietTrailers createFromParcel(Parcel in) {
            return new MovietTrailers(in);
        }

        @Override
        public MovietTrailers[] newArray(int size) {
            return new MovietTrailers[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
    }
}
