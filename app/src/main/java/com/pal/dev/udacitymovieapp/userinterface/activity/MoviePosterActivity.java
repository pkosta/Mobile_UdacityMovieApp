/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.activity;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.annotation.SortType;
import com.pal.dev.udacitymovieapp.database.sqlite.FavoriteMovieDbContract;
import com.pal.dev.udacitymovieapp.databinding.ActivityMoviePosterBinding;
import com.pal.dev.udacitymovieapp.network.MovieNetworkManager;
import com.pal.dev.udacitymovieapp.network.NetworkFactory;
import com.pal.dev.udacitymovieapp.network.NetworkOperationCallback;
import com.pal.dev.udacitymovieapp.userinterface.adapter.ListItemClickListener;
import com.pal.dev.udacitymovieapp.userinterface.adapter.MoviePosterAdapter;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;
import com.pal.dev.udacitymovieapp.utility.BundleConstants;
import com.pal.dev.udacitymovieapp.utility.NetworkUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Displays the posters for all needed movies in grid manner.
 *
 * @author Palash Kosta (kosta.palash@gmail.com)
 */

public class MoviePosterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        ListItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_FAVORITE_MOVIES = 100;

    private MoviePosterAdapter mMoviePosterAdapter;

    private ArrayList<UiMovie> mMovieList;

    private ActivityMoviePosterBinding mBindingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingLayout = DataBindingUtil.setContentView(this, R.layout.activity_movie_poster);

        setUpSpinner(); // set up the sort spinner.

        // checking the save instance state if exist use saved data.
        // this is may be due to screen orientation.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(BundleConstants.BUNDLE_MOVIE_LIST)) {

            ArrayList<UiMovie> savedList = savedInstanceState.getParcelableArrayList(BundleConstants.BUNDLE_MOVIE_LIST);

            if (savedList != null && !savedList.isEmpty()) {
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
        if (NetworkUtils.isOnline(this)) {
            mBindingLayout.tvEmptyRecyclerView.setVisibility(GONE);
            mBindingLayout.rvGridMoviePoster.setVisibility(View.VISIBLE);
            new GetMoviesListener(this, SortType.SORT_TYPE_POPULARITY).makeNetworkRequest();
        } else {
            mBindingLayout.tvEmptyRecyclerView.setVisibility(View.VISIBLE);
            mBindingLayout.rvGridMoviePoster.setVisibility(GONE);
        }

    }

    /**
     * method to fetch the list of movies by top rated.
     */
    private void getRatedMoviesFromWb() {
        if (NetworkUtils.isOnline(this)) {
            mBindingLayout.tvEmptyRecyclerView.setVisibility(GONE);
            mBindingLayout.rvGridMoviePoster.setVisibility(View.VISIBLE);
            new GetMoviesListener(this, SortType.SORT_TYPE_RATING).makeNetworkRequest();
        } else {
            mBindingLayout.tvEmptyRecyclerView.setVisibility(View.VISIBLE);
            mBindingLayout.rvGridMoviePoster.setVisibility(GONE);
        }
    }

    /**
     * method to fetch the list of favorite movies from DB through provider.
     */
    private void getFavoriteMoviesFromDB() {
        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(LOADER_FAVORITE_MOVIES, null, this);
    }

    /**
     * metthod to initialize or update the movies recycler view data.
     *
     * @param aMovieList, List of Movies to display.
     */
    private void initOrUpdateMoviePosters(@NonNull List<UiMovie> aMovieList) {
        // sett up the recycler view.

        mMovieList = new ArrayList<>(aMovieList);

        if (mMoviePosterAdapter != null) {

            // update the adapter.
            mMoviePosterAdapter.updateDataList(mMovieList);
            mMoviePosterAdapter.notifyDataSetChanged();     // update the recycler view.

        } else {

            // initialize the adapter.
            mBindingLayout.rvGridMoviePoster.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
            mMoviePosterAdapter = new MoviePosterAdapter(this, mMovieList, this);
            mBindingLayout.rvGridMoviePoster.setAdapter(mMoviePosterAdapter);
            mMoviePosterAdapter.notifyDataSetChanged();

        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    /**
     * update the action bar title based on the option selected in the sort spinner.
     *
     * @param selectedValue selected value in the sort spinner.
     */
    private void updateActionBarTitle(String selectedValue) {
        if (getSupportActionBar() != null) {
            if (selectedValue.equalsIgnoreCase(getString(R.string.sort_popularity))) {

                getSupportActionBar().setTitle(R.string.lbl_toolbar_most_popular_movies);

            } else if (selectedValue.equalsIgnoreCase(getString(R.string.sort_rating))) {

                getSupportActionBar().setTitle(R.string.lbl_toolbar_most_rated_movies);

            } else {

                getSupportActionBar().setTitle(R.string.lbl_toolbar_favorites_movies);

            }
        }
    }

    private void showErrorMessage() {
        // show the error message to the user.
    }

    private void setUpSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mBindingLayout.sortSpinner.setAdapter(adapter);

        mBindingLayout.sortSpinner.setOnItemSelectedListener(this);

        updateActionBarTitle(mBindingLayout.sortSpinner.getSelectedItem().toString());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedValue = parent.getItemAtPosition(position).toString();
        if (selectedValue.equalsIgnoreCase(getString(R.string.sort_rating))) {

            getRatedMoviesFromWb();

        } else if (selectedValue.equalsIgnoreCase(getString(R.string.sort_popularity))) {

            getPopularMoviesFromWb();

        } else {

            getFavoriteMoviesFromDB();

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
        if (mMovieList != null) {
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

        private
        @SortType
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
            if (mWeakPosterActivity.get() != null) {

                mWeakPosterActivity.get().initOrUpdateMoviePosters(uiMovies);
            }
        }

        @Override
        public void onFailure(String s, Throwable throwable) {
            if (mWeakPosterActivity.get() != null) {
                mWeakPosterActivity.get().showErrorMessage();
            }
        }

        @Override
        public void onUnAuthorized() {
            if (mWeakPosterActivity.get() != null) {
                mWeakPosterActivity.get().showErrorMessage();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Loader Callbacks
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case LOADER_FAVORITE_MOVIES:

                /* URI for all rows of weather data in our weather table */
                Uri favoriteMoviesUri = FavoriteMovieDbContract.MovieDetails.CONTENT_URI_MOVIE_LIST;

                return new CursorLoader(this,
                        favoriteMoviesUri,
                        null,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data != null && data.getCount() > 0) {
            // valid data
            if (mBindingLayout.sortSpinner.getSelectedItem().toString()
                    .equalsIgnoreCase(getString(R.string.sort_favorite))) {

                mBindingLayout.tvEmptyRecyclerView.setVisibility(GONE);
                mBindingLayout.rvGridMoviePoster.setVisibility(View.VISIBLE);
                List<UiMovie> uiMovies = getMoviesDetailsFromCursor(data);
                initOrUpdateMoviePosters(uiMovies);

            }
        } else {
            mBindingLayout.tvEmptyRecyclerView.setVisibility(View.VISIBLE);
            mBindingLayout.rvGridMoviePoster.setVisibility(GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
        mBindingLayout.tvEmptyRecyclerView.setVisibility(View.VISIBLE);
        mBindingLayout.rvGridMoviePoster.setVisibility(GONE);
    }

    private @NonNull List<UiMovie> getMoviesDetailsFromCursor(Cursor data) {

        List<UiMovie> list = new ArrayList<>();
        UiMovie uiMovie;

        data.moveToFirst();
        do {
            String posterPath = data.getString(data.getColumnIndex(
                    FavoriteMovieDbContract.MovieDetails.COLUMN_POSTER_PATH));

            String overview = data.getString(data.getColumnIndex(
                    FavoriteMovieDbContract.MovieDetails.COLUMN_OVERVIEW));

            String releaseDate = data.getString(data.getColumnIndex(
                    FavoriteMovieDbContract.MovieDetails.COLUMN_RELEASE_DATE));

            long movieId = data.getLong(data.getColumnIndex(
                    FavoriteMovieDbContract.MovieDetails.COLUMN_MOVIE_ID));

            String originalTitle = data.getString(data.getColumnIndex(
                    FavoriteMovieDbContract.MovieDetails.COLUMN_ORIGINAL_TITLE));

            double popularity = 0;      // not saved in the local DB.

            long voteCount = 0;      // not saved in the local DB.

            float voteAverage = data.getFloat(data.getColumnIndex(
                    FavoriteMovieDbContract.MovieDetails.COLUMN_VOTE_AVERAGE));

            uiMovie = new UiMovie(posterPath, overview, releaseDate, movieId, originalTitle,
                    popularity, voteCount, voteAverage, true);

            list.add(uiMovie);

        } while (data.moveToNext());


        return list;
    }
}
