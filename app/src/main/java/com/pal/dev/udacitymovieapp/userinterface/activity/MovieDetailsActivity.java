/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.database.sqlite.FavoriteMovieDbContract;
import com.pal.dev.udacitymovieapp.databinding.ActivityScrollMovieDetailBinding;
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
import com.pal.dev.udacitymovieapp.utility.Constant;
import com.pal.dev.udacitymovieapp.utility.NetworkUtils;
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
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_FAVORITE_MOVIE_WITH_ID = 101;

    private UiMovie mCurrentUiMovie;

    private MovieVideosAdapter mMovieVideoAdapter;

    private MovieReviewsAdapter mMovieReviewAdapter;

    private ArrayList<UiMovieTrailer> mMovieTrailers;

    private ArrayList<UiMovieReview> mMovieReviews;

    private MenuItem mShareMenuItem;

    private ActivityScrollMovieDetailBinding mBindingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingLayout = DataBindingUtil.setContentView(this, R.layout.activity_scroll_movie_detail);

        setUpActionBar();   // set up the acion bar.

        setLayoutProperties();        // initialize the ui views.

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.movie_details, menu);

        mShareMenuItem = menu.findItem(R.id.action_share);
        mShareMenuItem.setVisible(false);

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
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

        /* Home button item clicked */
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        /* Share menu item clicked */
        if (item.getItemId() == R.id.action_share) {
            Intent shareIntent = createFirstTrailerShareIntent();
            if(shareIntent != null &&
                    shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private Intent createFirstTrailerShareIntent() {
        if(mMovieTrailers != null && mMovieTrailers.size() > 0) {
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText("http://m.youtube.com/watch?v=" + mMovieTrailers.get(0).getVideoKey())
                    .getIntent();
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            return shareIntent;
        } else {
            return null;
        }

    }

    /**
     * method to initialize the views.
     */
    private void setLayoutProperties() {

        mBindingLayout.rvMovieVideos.setNestedScrollingEnabled(false);
        mBindingLayout.rvMovieReviews.setNestedScrollingEnabled(false);
        mBindingLayout.ibFavMovieToggle.setOnClickListener(this);

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

        mBindingLayout.tvMovieName.setText(mCurrentUiMovie.getOriginalTitle());
        mBindingLayout.tvReleaseDate.setText(mCurrentUiMovie.getReleaseDate());
        mBindingLayout.tvUserRating.setText(String.valueOf(mCurrentUiMovie.getVoteAverage()));
        mBindingLayout.tvMovieOverview.setText(mCurrentUiMovie.getOverview());

        // asynchronous loading of the image.
        Picasso.with(this)
                .load(mCurrentUiMovie.getPosterFullImagePath())
                .placeholder(R.color.colorAccent)
                .into(mBindingLayout.ivMoviePoster);

        getSupportLoaderManager().initLoader(LOADER_FAVORITE_MOVIE_WITH_ID, null, this);

        if(mShareMenuItem != null) {
            mShareMenuItem.setVisible(false);
        }

        mBindingLayout.tvHeadingTrailers.setText(getString(R.string.lbl_heading_trailers, getString(R.string.msg_loading)));
        mBindingLayout.tvHeadingReviews.setText(getString(R.string.lbl_heading_reviews, getString(R.string.msg_loading)));

    }

    private void getMovieTrailersFromWb() {

        if(NetworkUtils.isOnline(this)) {
            Log.d(Constant.APP_LOG_TAG,"Web Service Request for Trailers");
            MovieNetworkManager movieNetworkManager =
                    new NetworkFactory().getMovieNetworkManager();
            movieNetworkManager.getMovieTrailers(mCurrentUiMovie.getMovieId(),
                    new NetworkOperationCallback<String, List<UiMovieTrailer>>() {
                        @Override
                        public void onSuccess(String s, List<UiMovieTrailer> dbNwMovieTrailers) {

                            Log.d(Constant.APP_LOG_TAG,"Web Service Request for Trailers - Completed");
                            showMovieVideos(dbNwMovieTrailers);
                        }

                        @Override
                        public void onFailure(String s, Throwable throwable) {
                            Log.d(Constant.APP_LOG_TAG,"Web Service Request for Trailers - Failed " +
                                    "Please check your connection and API keys and retry.");
                        }

                        @Override
                        public void onUnAuthorized() {
                            Log.d(Constant.APP_LOG_TAG,"Web Service Request for Trailers - UnAuthorized" +
                                    "Please check your API key and retry.");
                        }
                    });
        }
    }

    private void getMovieReviewsFromWb() {

        if(NetworkUtils.isOnline(this)) {
            Log.d(Constant.APP_LOG_TAG,"Web Service Request for Reviews");
            MovieNetworkManager movieNetworkManager =
                    new NetworkFactory().getMovieNetworkManager();
            movieNetworkManager.getMovieReviews(mCurrentUiMovie.getMovieId(),
                    new NetworkOperationCallback<String, List<UiMovieReview>>() {
                        @Override
                        public void onSuccess(String s, List<UiMovieReview> uiMovieReviews) {

                            Log.d(Constant.APP_LOG_TAG,"Web Service Request for Reviews - Completed");
                            showMovieReviews(uiMovieReviews);

                        }

                        @Override
                        public void onFailure(String s, Throwable throwable) {
                            Log.d(Constant.APP_LOG_TAG,"Web Service Request for Reviews - Failed " +
                                    "Please check your connection and API keys and retry.");

                        }

                        @Override
                        public void onUnAuthorized() {
                            Log.d(Constant.APP_LOG_TAG,"Web Service Request for Reviews - UnAuthorized" +
                                    "Please check your API key and retry.");

                        }
                    });

        }
    }

    private void showMovieReviews(List<UiMovieReview> movieReviews) {

        mBindingLayout.tvHeadingReviews.setText(getString(R.string.lbl_heading_reviews,
                String.valueOf(movieReviews.size())));

        mMovieReviews = new ArrayList<>(movieReviews);

        if(mMovieReviewAdapter != null) {

            mMovieReviewAdapter.updateDataList(movieReviews);
            mMovieVideoAdapter.notifyDataSetChanged();

        } else {

            mBindingLayout.rvMovieReviews.setLayoutManager(new LinearLayoutManager(this));
            // passing null below to avoid click event.
            mMovieReviewAdapter = new MovieReviewsAdapter(this, mMovieReviews, null);
            mBindingLayout.rvMovieReviews.setAdapter(mMovieReviewAdapter);
            mMovieReviewAdapter.notifyDataSetChanged();

        }

    }

    private void showMovieVideos(List<UiMovieTrailer> movieTrailers) {
        mBindingLayout.tvHeadingTrailers.setText(getString(R.string.lbl_heading_trailers,
                String.valueOf(movieTrailers.size())));

        if(mShareMenuItem != null) {
            mShareMenuItem.setVisible(true);
        }

        mMovieTrailers = new ArrayList<>(movieTrailers);

        if (mMovieVideoAdapter != null) {

            mMovieVideoAdapter.updateDataList(movieTrailers);
            mMovieVideoAdapter.notifyDataSetChanged();

        } else {

            mBindingLayout.rvMovieVideos.setLayoutManager(new LinearLayoutManager(this));
            mMovieVideoAdapter = new MovieVideosAdapter(this, movieTrailers, this);
            mBindingLayout.rvMovieVideos.setAdapter(mMovieVideoAdapter);
            mMovieVideoAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onListItemClick(int clickedItemPosition) {
        watchYoutubeVideo(mMovieTrailers.get(clickedItemPosition).getVideoKey());
    }

    private void watchYoutubeVideo(String id) {
        //Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v=" + id));
        appIntent.putExtra("VIDEO_ID", id);
        if (appIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(appIntent);
        }
    }

    private void changeFavoriteImageButton(boolean isSelected) {
        if (isSelected) {
            // selected image
            mBindingLayout.ibFavMovieToggle
                    .setBackground(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24px));
        } else {
            // un-seleted image.
            mBindingLayout.ibFavMovieToggle
                    .setBackground(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24px));
        }
    }

    @Override
    public void onClick(View v) {
        int clickedView = v.getId();
        // when user select the movie as favorite, the app will store the movie in the local database.
        // when user un-select the movie as favorite, the app will remove the movie from the local db
        switch (clickedView) {
            case R.id.ib_fav_movie_toggle:
                if (mBindingLayout.ibFavMovieToggle.getTag().equals(true)) {
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
                mBindingLayout.ibFavMovieToggle.setTag(true);
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
        builder.setTitle(R.string.lbl_title_remove_favorite);
        builder.setMessage(R.string.lbl_msg_remove_favorite);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBindingLayout.ibFavMovieToggle.setTag(false);
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

            changeFavoriteImageButton((Boolean) mBindingLayout.ibFavMovieToggle.getTag());
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
            changeFavoriteImageButton((Boolean) mBindingLayout.ibFavMovieToggle.getTag());
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_FAVORITE_MOVIE_WITH_ID:
                Uri movieWithIdUri = FavoriteMovieDbContract.MovieDetails
                        .buildContentUriWithId(mCurrentUiMovie.getMovieId());
                return new CursorLoader(
                        this,
                        movieWithIdUri,
                        null,
                        null,
                        null,
                        null
                );

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0) {
            mBindingLayout.ibFavMovieToggle.setTag(true);
            changeFavoriteImageButton(true);
        } else {
            mBindingLayout.ibFavMovieToggle.setTag(false);
            changeFavoriteImageButton(false);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
