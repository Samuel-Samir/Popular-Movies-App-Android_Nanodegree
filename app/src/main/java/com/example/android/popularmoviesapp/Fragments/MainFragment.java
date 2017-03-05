package com.example.android.popularmoviesapp.Fragments;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.popularmoviesapp.Adapters.RecyclerViewAdapter;
import com.example.android.popularmoviesapp.Adapters.SpinnerOrderAdapter;
import com.example.android.popularmoviesapp.FetchFromDataBase;
import com.example.android.popularmoviesapp.FetchMoviesAsyncTask;
import com.example.android.popularmoviesapp.Models.MovieDetails;
import com.example.android.popularmoviesapp.Models.MoviesList;
import com.example.android.popularmoviesapp.Models.SpinnerItemContent;
import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.Utils.NetworkUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private final String POPULAR="popular";
    private final String TOP_RATED="top_rated";
    private final String FAVORITES ="favorites";

    private MoviesList myMoviesList ;
    private RecyclerViewAdapter moviesRecyclerViewAdapter ;
    private RecyclerView moviesRecyclerView;

    private ArrayList<SpinnerItemContent> navSpinner;
    private SpinnerOrderAdapter spinnerOrderAdapter;
    private Spinner spinner;
    private LinearLayout noFavoritesLinearLayout ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       if (NetworkUtils.checkInternetConnection(getContext())==false)
             NetworkUtils.connectionAlart(getActivity());
        else {
            moviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.move_recyclerView);
            noFavoritesLinearLayout = (LinearLayout) rootView.findViewById(R.id.no_favorites);
            setHasOptionsMenu(true);

            navSpinner = new ArrayList<SpinnerItemContent>();
            navSpinner.add(new SpinnerItemContent(getResources().getString(R.string.popular), R.drawable.most));
            navSpinner.add(new SpinnerItemContent(getResources().getString(R.string.top_rated), R.drawable.top));
            navSpinner.add(new SpinnerItemContent(getResources().getString(R.string.favorites) ,R.drawable.fav));


            onOrientationChange(getResources().getConfiguration().orientation);
            if (savedInstanceState==null)
            {
                fetchDataFromAsyncTask(NetworkUtils.MoviesOreder);
            }
            else if (savedInstanceState!=null)
            {
                myMoviesList = (MoviesList) savedInstanceState.getParcelable("myMoviesList");
                moviesRecyclerViewAdapter.setMoviesList(myMoviesList ,getActivity());

            }
        }

        return rootView;
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("myMoviesList" ,myMoviesList);
    }


    public void onOrientationChange(int orientation){
        int landScape=3;
        int portrait= 2;
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        if (widthPixels>=1023 || heightPixels>=1023)
        {
            landScape=4;
            portrait=3;
        }

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),portrait ));
            moviesRecyclerViewAdapter =new RecyclerViewAdapter() ;
            moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);

        }
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE){

            moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),landScape ));
            moviesRecyclerViewAdapter =new RecyclerViewAdapter() ;
            moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);
        }
    }

    private void fetchDataFromAsyncTask (String movieOrder)
    {
        noFavoritesLinearLayout.setVisibility(View.GONE);

        if(NetworkUtils.OrderIndex<2 && !NetworkUtils.MoviesOreder.equals(FAVORITES))
        {
            FetchMoviesAsyncTask fetchMoviesAsyncTask =new FetchMoviesAsyncTask(getActivity()) ;
            fetchMoviesAsyncTask.setFetchMoviesAsyncTaskCallBack(new FetchMoviesAsyncTask.FetchMoviesAsyncTaskCallBack() {
                @Override
                public void onPostExecute(Object object) {
                    myMoviesList =(MoviesList) object ;
                    moviesRecyclerViewAdapter.setMoviesList(myMoviesList ,getActivity());
                }
            });
            fetchMoviesAsyncTask.execute(movieOrder);
        }

        else if (NetworkUtils.OrderIndex==2&&NetworkUtils.MoviesOreder.equals(FAVORITES))
        {
            fetchDataFromDB();
        }

    }

    public void fetchDataFromDB() {
        FetchFromDataBase fetchFromDataBase = new FetchFromDataBase(getActivity());
        fetchFromDataBase.setFetchMoviesDBCallback(new FetchFromDataBase.FetchFromDBCallback() {
            @Override
            public void onPostExecute(Object Object) {
                myMoviesList =(MoviesList) Object ;
                moviesRecyclerViewAdapter.setMoviesList(myMoviesList ,getActivity());

                if(myMoviesList.results.size()==0)
                {
                    noFavoritesLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        fetchFromDataBase.execute("movie" ,"movie");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.spinner_order, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinnerOrderAdapter = new SpinnerOrderAdapter(getActivity() , navSpinner);
        spinner.setAdapter(spinnerOrderAdapter);
        spinner.setSelection(NetworkUtils.OrderIndex);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    NetworkUtils.MoviesOreder = POPULAR;
                    NetworkUtils.OrderIndex = 0;
                    fetchDataFromAsyncTask(POPULAR);
                } else if (position == 1) {
                    NetworkUtils.MoviesOreder = TOP_RATED;
                    NetworkUtils.OrderIndex = 1;
                    fetchDataFromAsyncTask(TOP_RATED);

                } else if (position == 2) {
                    NetworkUtils.MoviesOreder=FAVORITES;
                    NetworkUtils.OrderIndex=2;
                    fetchDataFromDB();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // fetchDataFromAsyncTask (NetworkUtils.MoviesOreder);

            }
        });


    }


}
