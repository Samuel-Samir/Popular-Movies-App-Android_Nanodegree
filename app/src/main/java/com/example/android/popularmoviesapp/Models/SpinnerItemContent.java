package com.example.android.popularmoviesapp.Models;

/**
 * Created by samuel on 2/21/2017.
 */

public class SpinnerItemContent {
    private String title;
    private int icon;

    public SpinnerItemContent(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }
}
