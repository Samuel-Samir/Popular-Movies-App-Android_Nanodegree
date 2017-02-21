package com.example.android.popularmoviesapp.Fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.popularmoviesapp.Adapters.RecyclerViewAdapter;
import com.example.android.popularmoviesapp.Adapters.SpinnerOrderAdapter;
import com.example.android.popularmoviesapp.FetchMoviesAsyncTask;
import com.example.android.popularmoviesapp.Models.MoviesList;
import com.example.android.popularmoviesapp.Models.SpinnerItemContent;
import com.example.android.popularmoviesapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private final String POPULAR="popular";
    private final String TOP_RATED="top_rated";

    private RecyclerViewAdapter moviesRecyclerViewAdapter ;
    private RecyclerView moviesRecyclerView;
    private ArrayList<SpinnerItemContent> navSpinner;
    private SpinnerOrderAdapter spinnerOrderAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        moviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.move_recyclerView);
        setHasOptionsMenu(true);
        navSpinner = new ArrayList<SpinnerItemContent>();
        navSpinner.add(new SpinnerItemContent(getResources().getString(R.string.popular), R.drawable.most));
        navSpinner.add(new SpinnerItemContent(getResources().getString(R.string.top_rated), R.drawable.top));
        onOrientationChange(getResources().getConfiguration().orientation);
       // fetchDataFromAsyncTask();
        return rootView;
    }


    public void onOrientationChange(int orientation){
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2 ));
            moviesRecyclerViewAdapter =new RecyclerViewAdapter() ;
            moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);

        }
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE){

            moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3 ));
            moviesRecyclerViewAdapter =new RecyclerViewAdapter() ;
            moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);
        }
    }
    private void fetchDataFromAsyncTask (String movieOrder)
    {
        FetchMoviesAsyncTask fetchMoviesAsyncTask =new FetchMoviesAsyncTask(getActivity()) ;
        fetchMoviesAsyncTask.setFetchMoviesAsyncTaskCallBack(new FetchMoviesAsyncTask.FetchMoviesAsyncTaskCallBack() {
            @Override
            public void onPostExecute(MoviesList moviesList) {
                moviesRecyclerViewAdapter.setMoviesList(moviesList ,getActivity());
            }
        });
        fetchMoviesAsyncTask.execute(movieOrder);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.spinner_order, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinnerOrderAdapter = new SpinnerOrderAdapter(getActivity() , navSpinner);
        spinner.setAdapter(spinnerOrderAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    fetchDataFromAsyncTask(POPULAR);
                }
                else if (position==1)
                {
                    fetchDataFromAsyncTask(TOP_RATED);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fetchDataFromAsyncTask(POPULAR);
            }
        });
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.android_action_bar_spinner_menu, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        navSpinner = new ArrayList<SpinnerNavItem>();
        navSpinner.add(new SpinnerItemContent("Local", R.drawable.ic_location));
        navSpinner.add(new SpinnerItemContent("My Places", R.drawable.ic_location));

        // title drop down adapter
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String f ="ff";
                Toast.makeText(MainActivity.this, f+" "+position,
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });        // assigning the spinner navigation
        return true;
    }*/
}
