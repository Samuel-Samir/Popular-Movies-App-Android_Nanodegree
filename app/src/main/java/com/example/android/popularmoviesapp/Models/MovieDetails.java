package com.example.android.popularmoviesapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by samuel on 2/18/2017.
 */

public class MovieDetails implements Parcelable {
    public String poster_path ;
    public String overview;
    public String release_date ;
    public int id ;
    public String original_title;
    public String title;
    public String backdrop_path;
    public long vote_count ;
    public double vote_average ;

    public MovieDetails ()
    {

    }

    protected MovieDetails(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        id = in.readInt();
        original_title = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        vote_count = in.readLong();
        vote_average = in.readDouble();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeInt(id);
        dest.writeString(original_title);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeLong(vote_count);
        dest.writeDouble(vote_average);
    }
}
