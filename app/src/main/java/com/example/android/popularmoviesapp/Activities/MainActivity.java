package com.example.android.popularmoviesapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.popularmoviesapp.Fragments.MainFragment;
import com.example.android.popularmoviesapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container,new MainFragment()).commit();
        }
    }
}


/*

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState ==null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container ,new PopularFragment()).commit() ;
        }

    }
}
 */