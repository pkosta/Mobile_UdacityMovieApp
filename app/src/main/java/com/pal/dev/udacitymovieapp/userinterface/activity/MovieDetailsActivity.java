/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.database.sqlite.FavoriteMovieDbContract;
import com.pal.dev.udacitymovieapp.network.MovieNetworkManager;
import com.pal.dev.udacitymovieapp.network.NetworkFactory;
import com.pal.dev.udacitymovieapp.network.NetworkOperationCallback;
import com.pal.dev.udacitymovieapp.userinterface.adapter.ListItemClickListener;
import com.pal.dev.udacitymovieapp.userinterface.adapter.MovieReviewsAdapter;
import com.pal.dev.udacitymovieapp.userinterface.adapter.MovieVideosAdapter;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieReview;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieTrailer;
import com.pal.dev.udacitymovieapp.utility.BundleConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Palash
 * <p>
 * Activity class for movie details screens.
 */

public class MovieDetailsActivity extends AppCompatActivity implements
        ListItemClickListener,
        View.OnClickListener {

    private UiMovie mCurrentUiMovie;

    //Layout elements
    private TextView mTvMovieName;

    private TextView mTvMovieReleaseDate;

    private TextView mTvMovieUserRating;

    private TextView mTvMovieOverview;

    private ImageView mIvMoviePoster;

    private MovieVideosAdapter mMovieVideoAdapter;

    private MovieReviewsAdapter mMovieReviewAdapter;

    private RecyclerView mRvMovieVideos;

    private RecyclerView mRvMovieReviews;

    private ArrayList<UiMovieTrailer> mMovieTrailers;

    private ArrayList<UiMovieReview> mMovieReviews;

    private ImageButton mIbFavoriteMovieToggle;

    private TextView mTvHeadingTrailers, mTvHeadingReviews;

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

        // checking the save instance state if exist use saved data.
        // this is may be due to screen orientation.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(BundleConstants.BUNDLE_MOVIE_TRAILER_LIST)) {

            ArrayList<UiMovieTrailer> savedList = savedInstanceState
                    .getParcelableArrayList(BundleConstants.BUNDLE_MOVIE_TRAILER_LIST);

            if (savedList != null && !savedList.isEmpty()) {
                showMovieVideos(savedList);
            } else {
                getMovieTrailersFromWb();
            }
        } else {
            getMovieTrailersFromWb();
        }

        if (savedInstanceState != null
                && savedInstanceState.containsKey(BundleConstants.BUNDLE_MOVIE_REVIEW_LIST)) {

            ArrayList<UiMovieReview> savedList = savedInstanceState
                    .getParcelableArrayList(BundleConstants.BUNDLE_MOVIE_REVIEW_LIST);

            if (savedList != null && !savedList.isEmpty()) {
                showMovieReviews(savedList);
            } else {
                getMovieReviewsFromWb();
            }
        } else {
            getMovieReviewsFromWb();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMovieTrailers != null) {
            outState.putParcelableArrayList(BundleConstants.BUNDLE_MOVIE_TRAILER_LIST, mMovieTrailers);
        }

        if (mMovieReviews != null) {
            outState.putParcelableArrayList(BundleConstants.BUNDLE_MOVIE_REVIEW_LIST, mMovieReviews);
        }
        super.onSaveInstanceState(outState);
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

        mTvHeadingTrailers = (TextView) findViewById(R.id.tv_heading_trailers);
        mTvHeadingReviews = (TextView) findViewById(R.id.tv_heading_reviews);

        mIvMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);

        mRvMovieVideos = (RecyclerView) findViewById(R.id.rv_movie_videos);

        mRvMovieReviews = (RecyclerView) findViewById(R.id.rv_movie_reviews);

        mIbFavoriteMovieToggle = (ImageButton) findViewById(R.id.ib_fav_movie_toggle);
        mIbFavoriteMovieToggle.setOnClickListener(this);

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
                .load(mCurrentUiMovie.getPosterFullImagePath())
                .placeholder(R.color.colorAccent)
                .into(mIvMoviePoster);

        if (mCurrentUiMovie.isFavorite()) {
            mIbFavoriteMovieToggle.setTag(true);
        } else {
            mIbFavoriteMovieToggle.setTag(false);
        }
        changeFavoriteImageButton(mCurrentUiMovie.isFavorite());


        mTvHeadingTrailers.setText(getString(R.string.lbl_heading_trailers, getString(R.string.msg_loading)));
        mTvHeadingReviews.setText(getString(R.string.lbl_heading_reviews, getString(R.string.msg_loading)));

    }

    private void getMovieTrailersFromWb() {
        Log.d("BUGS","Web Service Request for Trailers");
        MovieNetworkManager movieNetworkManager =
                new NetworkFactory().getMovieNetworkManager();
        movieNetworkManager.getMovieTrailers(mCurrentUiMovie.getMovieId(),
                new NetworkOperationCallback<String, List<UiMovieTrailer>>() {
                    @Override
                    public void onSuccess(String s, List<UiMovieTrailer> dbNwMovieTrailers) {

                        Log.d("BUGS","Web Service Request for Trailers - Completed");

                        mTvHeadingTrailers.setText(getString(R.string.lbl_heading_trailers,
                                String.valueOf(dbNwMovieTrailers.size())));

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

    private void getMovieReviewsFromWb() {
        MovieNetworkManager movieNetworkManager =
                new NetworkFactory().getMovieNetworkManager();
        movieNetworkManager.getMovieReviews(mCurrentUiMovie.getMovieId(),
                new NetworkOperationCallback<String, List<UiMovieReview>>() {
                    @Override
                    public void onSuccess(String s, List<UiMovieReview> uiMovieReviews) {

                        mTvHeadingReviews.setText(getString(R.string.lbl_heading_reviews,
                                String.valueOf(uiMovieReviews.size())));


                        showMovieReviews(uiMovieReviews);
                    }

                    @Override
                    public void onFailure(String s, Throwable throwable) {

                    }

                    @Override
                    public void onUnAuthorized() {

                    }
                });
    }

    private void showMovieReviews(List<UiMovieReview> movieReviews) {

        mMovieReviews = new ArrayList<>(movieReviews);

        if(mMovieReviewAdapter != null) {

            mMovieReviewAdapter.updateDataList(movieReviews);
            mMovieVideoAdapter.notifyDataSetChanged();

        } else {

            mRvMovieReviews.setLayoutManager(new LinearLayoutManager(this));
            // passing null below to avoid click event.
            mMovieReviewAdapter = new MovieReviewsAdapter(this, mMovieReviews, null);
            mRvMovieReviews.setAdapter(mMovieReviewAdapter);
            mMovieReviewAdapter.notifyDataSetChanged();

        }

    }

    private void showMovieVideos(List<UiMovieTrailer> movieTrailers) {

        mMovieTrailers = new ArrayList<>(movieTrailers);

        if (mMovieVideoAdapter != null) {

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

    public void watchYoutubeVideo(String id) {
        //Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v=" + id));
        appIntent.putExtra("VIDEO_ID", id);
        if (appIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(appIntent);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CProvider Methods
    ///////////////////////////////////////////////////////////////////////////

    private void saveSelectedFavoriteMovie() {

        if (mCurrentUiMovie != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FavoriteMovieDbContract.MovieDetails.COLUMN_MOVIE_ID,
                    mCurrentUiMovie.getMovieId());
            contentValues.put(FavoriteMovieDbContract.MovieDetails.COLUMN_ORIGINAL_TITLE,
                    mCurrentUiMovie.getOriginalTitle());
            contentValues.put(FavoriteMovieDbContract.MovieDetails.COLUMN_POSTER_PATH,
                    mCurrentUiMovie.getPosterPath());
            contentValues.put(FavoriteMovieDbContract.MovieDetails.COLUMN_OVERVIEW,
                    mCurrentUiMovie.getOverview());
            contentValues.put(FavoriteMovieDbContract.MovieDetails.COLUMN_VOTE_AVERAGE,
                    mCurrentUiMovie.getVoteAverage());
            contentValues.put(FavoriteMovieDbContract.MovieDetails.COLUMN_RELEASE_DATE,
                    mCurrentUiMovie.getReleaseDate());


            getContentResolver().insert(FavoriteMovieDbContract.MovieDetails.CONTENT_URI_MOVIE_LIST,
                    contentValues);

            changeFavoriteImageButton((Boolean) mIbFavoriteMovieToggle.getTag());
        }

    }

    private void removeMovieFromDb() {

        String selection  = FavoriteMovieDbContract.MovieDetails.COLUMN_MOVIE_ID + " = ?";

        String [] selectonArgss = new String[] {String.valueOf(mCurrentUiMovie.getMovieId())};

        int numberOfRowsDeleted = getContentResolver().delete(
                FavoriteMovieDbContract.MovieDetails.CONTENT_URI_MOVIE_LIST,
                selection,
                selectonArgss);
        if(numberOfRowsDeleted > 0 ) {
            changeFavoriteImageButton((Boolean) mIbFavoriteMovieToggle.getTag());
        }

    }

    private void changeFavoriteImageButton(boolean isSelected) {
        if (isSelected) {
            // selected image
            mIbFavoriteMovieToggle.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24px));
        } else {
            // unseleted image.
            mIbFavoriteMovieToggle.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24px));
        }
    }

    @Override
    public void onClick(View v) {
        int clickedView = v.getId();
        // when user select the movie as favorite, the app will store the movie in the local database.
        // when user un-select the movie as favorite, the app will remove the movie from the local db
        switch (clickedView) {
            case R.id.ib_fav_movie_toggle:
                if (mIbFavoriteMovieToggle.getTag().equals(true)) {
                    removeFavorite();
                } else {
                    addToFavorite();
                }
                break;

            default:

        }
    }

    private void addToFavorite() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.lbl_title_add_favorite);
        builder.setMessage(R.string.lbl_msg_add_favorite);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIbFavoriteMovieToggle.setTag(true);
                saveSelectedFavoriteMovie();
            }
        });
        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dismiss the dialog.
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void removeFavorite() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Favorite");
        builder.setMessage("By removing favorite, movie details will not be accessible in offline mode.");
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIbFavoriteMovieToggle.setTag(false);
                removeMovieFromDb();
            }
        });
        builder.setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // simply dismiss the dialog.
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
