/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.network.MovieNetworkManager;
import com.pal.dev.udacitymovieapp.network.NetworkFactory;
import com.pal.dev.udacitymovieapp.network.NetworkOperationCallback;
import com.pal.dev.udacitymovieapp.userinterface.adapter.ListItemClickListener;
import com.pal.dev.udacitymovieapp.userinterface.adapter.MovieVideosAdapter;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieTrailer;
import com.pal.dev.udacitymovieapp.utility.BundleConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Palash
 *
 * Activity class for movie details screens.
 */

public class MovieDetailsActivity extends AppCompatActivity implements ListItemClickListener {

    private UiMovie mCurrentUiMovie;

    //Layout elements

    private TextView mTvMovieName;

    private TextView mTvMovieReleaseDate;

    private TextView mTvMovieUserRating;

    private TextView mTvMovieOverview;

    private ImageView mIvMoviePoster;

    private MovieVideosAdapter mMovieVideoAdapter;

    private RecyclerView mRvMovieVideos;

    private List<UiMovieTrailer> mMovieTrailers;

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

        mRvMovieVideos = (RecyclerView) findViewById(R.id.rv_movie_videos);

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



        // temp WB calling.
        // temp
        MovieNetworkManager movieNetworkManager =
                new NetworkFactory().getMovieNetworkManager();

        movieNetworkManager.getMovieTrailers(mCurrentUiMovie.getMovieId(),
                new NetworkOperationCallback<String, List<UiMovieTrailer>>() {
            @Override
            public void onSuccess(String s, List<UiMovieTrailer> dbNwMovieTrailers) {
                showMovieVideos(dbNwMovieTrailers);
            }

            @Override
            public void onFailure(String s, Throwable throwable) {

            }

            @Override
            public void onUnAuthorized() {

            }
        });
    }

    private void showMovieVideos(List<UiMovieTrailer> movieTrailers) {

        mMovieTrailers = new ArrayList<>(movieTrailers);

        if(mMovieVideoAdapter != null) {

            mMovieVideoAdapter.updateDataList(movieTrailers);
            mMovieVideoAdapter.notifyDataSetChanged();

        } else {

            mRvMovieVideos.setLayoutManager(new LinearLayoutManager(this));
            mMovieVideoAdapter = new MovieVideosAdapter(this, movieTrailers, this);
            mRvMovieVideos.setAdapter(mMovieVideoAdapter);
            mMovieVideoAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onListItemClick(int clickedItemPosition) {
        watchYoutubeVideo(mMovieTrailers.get(clickedItemPosition).getVideoKey());
    }

    public void watchYoutubeVideo(String id){
        //Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v="+id));
        appIntent.putExtra("VIDEO_ID", id);
        if(appIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(appIntent);
        }
    }
}
