/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.annotation.SortType;
import com.pal.dev.udacitymovieapp.network.MovieNetworkManager;
import com.pal.dev.udacitymovieapp.network.NetworkFactory;
import com.pal.dev.udacitymovieapp.network.NetworkOperationCallback;
import com.pal.dev.udacitymovieapp.userinterface.adapter.MoviePosterAdapter;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;
import com.pal.dev.udacitymovieapp.utility.BundleConstants;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Displays the posters for all needed movies in grid manner.
 *
 * @author Palash Kosta (kosta.palash@gmail.com)
 */

public class MoviePosterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MoviePosterAdapter.ListItemClickListener {

    private RecyclerView mRvGridMoviePosters;

    private MoviePosterAdapter mMoviePosterAdapter;

    private ArrayList<UiMovie> mMovieList;

    private TextView mTvEmptyDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);

        initViews();

        setUpSpinner(); // set up the sort spinner.

        // checking the save instance state if exist use saved data.
        // this is may be due to screen orientation.
        if(savedInstanceState != null
                && savedInstanceState.getParcelableArrayList(BundleConstants.BUNDLE_MOVIE_LIST) != null) {

            ArrayList<UiMovie> savedList = savedInstanceState.getParcelableArrayList(BundleConstants.BUNDLE_MOVIE_LIST);

            if(savedList != null && !savedList.isEmpty()) {
                initOrUpdateMoviePosters(savedList);
            } else {
                getPopularMoviesFromWb();
            }
        } else {
            getPopularMoviesFromWb();
        }



    }

    /**
     * method to fetch the list of movies by most popular.
     */
    private void getPopularMoviesFromWb() {
        if(isOnline()) {
            mTvEmptyDataView.setVisibility(GONE);
            mRvGridMoviePosters.setVisibility(View.VISIBLE);
            new GetMoviesListener(this, SortType.SORT_TYPE_POPULARITY).makeNetworkRequest();
        } else {
            mTvEmptyDataView.setVisibility(View.VISIBLE);
            mRvGridMoviePosters.setVisibility(GONE);
        }

    }

    /**
     * method to fetch the list of movies by top rated.
     */
    private void getRatedMoviesFromWb() {
        if(isOnline()) {
            mTvEmptyDataView.setVisibility(GONE);
            mRvGridMoviePosters.setVisibility(View.VISIBLE);
            new GetMoviesListener(this, SortType.SORT_TYPE_RATING).makeNetworkRequest();
        } else {
            mTvEmptyDataView.setVisibility(View.VISIBLE);
            mRvGridMoviePosters.setVisibility(GONE);
        }
    }


    /**
     * method to initialize the views.
     */
    private void initViews() {
        mRvGridMoviePosters = (RecyclerView) findViewById(R.id.rv_grid_movie_poster);
        mTvEmptyDataView = (TextView) findViewById(R.id.tv_empty_recycler_view);
    }

    /**
     * metthod to initialize or update the movies recycler view data.
     * @param aMovieList, List of Movies to display.
     */
    private void initOrUpdateMoviePosters(@NonNull List<UiMovie> aMovieList) {
        // sett up the recycler view.

        mMovieList = new ArrayList<>(aMovieList);

        if(mMoviePosterAdapter != null) {

            // update the adapter.
            mMoviePosterAdapter.updateDataList(mMovieList);
            mMoviePosterAdapter.notifyDataSetChanged();     // update the recycler view.

        } else {

            // initialize the adapter.
            mRvGridMoviePosters.setLayoutManager(new GridLayoutManager(this, 2));
            mMoviePosterAdapter = new MoviePosterAdapter(this, mMovieList, this);
            mRvGridMoviePosters.setAdapter(mMoviePosterAdapter);
            mMoviePosterAdapter.notifyDataSetChanged();

        }
    }

    /**
     * update the action bar title based on the option selected in the sort spinner.
     * @param selectedValue selected value in the sort spinner.
     */
    private void updateActionBarTitle(String selectedValue) {
        if(getSupportActionBar() != null) {
            if(selectedValue.equalsIgnoreCase(getString(R.string.sort_popularity))) {

                getSupportActionBar().setTitle(R.string.lbl_toolbar_most_popular_movies);

            } else {

                getSupportActionBar().setTitle(R.string.lbl_toolbar_most_rated_movies);

            }
        }
    }

    private void showErrorMessage() {
        // show the error message to the user.
    }

    private void setUpSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.sort_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        updateActionBarTitle(spinner.getSelectedItem().toString());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedValue = parent.getItemAtPosition(position).toString();
        if(selectedValue.equalsIgnoreCase(getString(R.string.sort_rating))) {

            getRatedMoviesFromWb();

        } else {

            getPopularMoviesFromWb();

        }
        updateActionBarTitle(selectedValue);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onListItemClick(int clickedItemPosition) {
        UiMovie clickedUiMovie = mMovieList.get(clickedItemPosition);

        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstants.BUNDLE_MOVIE, clickedUiMovie);

        Intent movieDetailsCreenIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsCreenIntent.putExtra(BundleConstants.BUNDLE, bundle);

        startActivity(movieDetailsCreenIntent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mMovieList != null) {
            outState.putParcelableArrayList(BundleConstants.BUNDLE_MOVIE_LIST, mMovieList);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * static nested class to call the web call to fetch the movie list based on the sort parameter.
     * It holds the weak reference of the activity to avoid the memory leak.
     */
    private static class GetMoviesListener implements NetworkOperationCallback<String, List<UiMovie>> {

        private final WeakReference<MoviePosterActivity> mWeakPosterActivity;

        private final MovieNetworkManager mMovieNetworkManager;

        private @SortType
        final String mSortType;

        GetMoviesListener(MoviePosterActivity aActivity, @SortType String aSortType) {

            mWeakPosterActivity = new WeakReference<>(aActivity);
            mMovieNetworkManager = new NetworkFactory().getMovieNetworkManager();
            mSortType = aSortType;

        }

        void makeNetworkRequest() {
            mMovieNetworkManager.getPopularMovies(mSortType, this);
        }

        @Override
        public void onSuccess(String s, @NonNull List<UiMovie> uiMovies) {
            if(mWeakPosterActivity.get() != null) {
                mWeakPosterActivity.get().initOrUpdateMoviePosters(uiMovies);
            }
        }

        @Override
        public void onFailure(String s, Throwable throwable) {
            if(mWeakPosterActivity.get() != null) {
                mWeakPosterActivity.get().showErrorMessage();
            }
        }

        @Override
        public void onUnAuthorized() {
            if(mWeakPosterActivity.get() != null) {
                mWeakPosterActivity.get().showErrorMessage();
            }
        }
    }

    /**
     * method to check if the network is connected or not.
     * @return if connected, otherwise false.
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
