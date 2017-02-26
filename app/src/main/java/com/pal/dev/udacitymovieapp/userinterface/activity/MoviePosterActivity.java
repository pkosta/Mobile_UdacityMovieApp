/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.network.MovieNetworkManager;
import com.pal.dev.udacitymovieapp.network.NetworkFactory;
import com.pal.dev.udacitymovieapp.network.NetworkOperationCallback;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Displays the posters for all needed movies in grid manner.
 *
 * @author Palash Kosta (kosta.palash@gmail.com)
 */

public class MoviePosterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);

        new GetPopularMoviesListener(this).makeNetworkRequest();

    }

    public void initOrUpdateMoviePosters() {
        // sett up the recycler view.
    }

    public void showErrorMessage() {
        // shhow the error message to the user.
    }

    private static class GetPopularMoviesListener implements NetworkOperationCallback<String, List<UiMovie>> {

        private WeakReference<MoviePosterActivity> mWeakPosterActivity;

        private MovieNetworkManager mMovieNetworkManager;

        GetPopularMoviesListener(MoviePosterActivity aActivity) {

            mWeakPosterActivity = new WeakReference<>(aActivity);
            mMovieNetworkManager = new NetworkFactory().getMovieNetworkManager();

        }

        public void makeNetworkRequest() {
            mMovieNetworkManager.getPopularMovies(this);
        }

        @Override
        public void onSuccess(String s, List<UiMovie> uiMovies) {
            if(mWeakPosterActivity.get() != null) {
                mWeakPosterActivity.get().initOrUpdateMoviePosters();
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
}
