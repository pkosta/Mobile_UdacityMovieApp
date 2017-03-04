/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;
import com.pal.dev.udacitymovieapp.utility.BundleConstants;
import com.squareup.picasso.Picasso;

/**
 * Created by Palash
 *
 * Activity class for movie details screens.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    private UiMovie mCurrentUiMovie;

    //Layout elements

    private TextView mTvMovieName;

    private TextView mTvMovieReleaseDate;

    private TextView mTvMovieUserRating;

    private TextView mTvMovieOverview;

    private ImageView mIvMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_movie_detail);

        setUpActionBar();   // set up the acion bar.

        initViews();        // initialize the ui views.

        setIntentData();    // get the intent data and set to the member variables.

        if (mCurrentUiMovie != null) {
            // if the current movie is not null. we can uodate the user interface.
            updateUserInterface();      // update the UI.
        }

    }

    /**
     * method to set the action bar title and home button enabled.
     */
    private void setUpActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.lbl_toolbar_movie_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * method to initialize the views.
     */
    private void initViews() {

        mTvMovieName = (TextView) findViewById(R.id.tv_movie_name);
        mTvMovieReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mTvMovieUserRating = (TextView) findViewById(R.id.tv_user_rating);
        mTvMovieOverview = (TextView) findViewById(R.id.tv_movie_overview);

        mIvMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);

    }

    /**
     * method to set the intent data from getIntent() method.
     */
    private void setIntentData() {

        if (getIntent().getBundleExtra(BundleConstants.BUNDLE) != null) {
            Bundle bundle = getIntent().getBundleExtra(BundleConstants.BUNDLE);

            if (bundle != null && bundle.getParcelable(BundleConstants.BUNDLE_MOVIE) != null) {
                mCurrentUiMovie = bundle.getParcelable(BundleConstants.BUNDLE_MOVIE);
            }
        }

    }

    /**
     * method to update the ui.
     */
    private void updateUserInterface() {

        mTvMovieName.setText(mCurrentUiMovie.getOriginalTitle());
        mTvMovieReleaseDate.setText(mCurrentUiMovie.getReleaseDate());
        mTvMovieUserRating.setText(String.valueOf(mCurrentUiMovie.getVoteAverage()));
        mTvMovieOverview.setText(mCurrentUiMovie.getOverview());

        // asynchronous loading of the image.
        Picasso.with(this)
                .load(mCurrentUiMovie.getPosterPath())
                .placeholder(R.color.colorAccent)
                .into(mIvMoviePoster);
    }

}
