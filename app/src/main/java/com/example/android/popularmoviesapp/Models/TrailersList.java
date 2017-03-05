package com.example.android.popularmoviesapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuel on 3/4/2017.
 */

public class TrailersList implements Parcelable{

    public long id;
    public List<MovietTrailers> results = new ArrayList<>();

    public TrailersList ()
    {

    }
    protected TrailersList(Parcel in) {
        id = in.readLong();
        results = in.createTypedArrayList(MovietTrailers.CREATOR);
    }

    public static final Creator<TrailersList> CREATOR = new Creator<TrailersList>() {
        @Override
        public TrailersList createFromParcel(Parcel in) {
            return new TrailersList(in);
        }

        @Override
        public TrailersList[] newArray(int size) {
            return new TrailersList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeTypedList(results);
    }
}
