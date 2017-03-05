package com.example.android.popularmoviesapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuel on 3/4/2017.
 */

public class ReviewsList implements Parcelable{

    public List<MovieReviews> results = new ArrayList<>();
    public long id ;

    public ReviewsList ()
    {

    }
    protected ReviewsList(Parcel in) {
        results = in.createTypedArrayList(MovieReviews.CREATOR);
        id = in.readLong();
    }

    public static final Creator<ReviewsList> CREATOR = new Creator<ReviewsList>() {
        @Override
        public ReviewsList createFromParcel(Parcel in) {
            return new ReviewsList(in);
        }

        @Override
        public ReviewsList[] newArray(int size) {
            return new ReviewsList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeLong(id);
    }
}
