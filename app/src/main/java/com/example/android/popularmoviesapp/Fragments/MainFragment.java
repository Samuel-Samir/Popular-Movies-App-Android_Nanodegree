package com.example.android.popularmoviesapp.Fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmoviesapp.Adapters.RecyclerViewAdapter;
import com.example.android.popularmoviesapp.FetchMoviesAsyncTask;
import com.example.android.popularmoviesapp.Models.MoviesList;
import com.example.android.popularmoviesapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private RecyclerViewAdapter moviesRecyclerViewAdapter ;
    private RecyclerView moviesRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        moviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.move_recyclerView);
        onOrientationChange(getResources().getConfiguration().orientation);
        fetchDataFromAsyncTask();
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
    private void fetchDataFromAsyncTask ()
    {
        FetchMoviesAsyncTask fetchMoviesAsyncTask =new FetchMoviesAsyncTask(getActivity()) ;
        fetchMoviesAsyncTask.setFetchMoviesAsyncTaskCallBack(new FetchMoviesAsyncTask.FetchMoviesAsyncTaskCallBack() {
            @Override
            public void onPostExecute(MoviesList moviesList) {
                moviesRecyclerViewAdapter.setMoviesList(moviesList ,getActivity());
            }
        });
        fetchMoviesAsyncTask.execute("popular");
    }

}
