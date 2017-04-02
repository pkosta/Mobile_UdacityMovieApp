/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie;

/*
 * Created by Palash on 01/04/17.
 */

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.pal.dev.udacitymovieapp.userinterface.model.DbNwMovieReviewInterface;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieReview;

@SuppressWarnings("unused")
public class DbNwMovieReview implements DbNwMovieReviewInterface{

    @SerializedName(MovieNetworkContract.KEY_MOVIE_REVIEW_ID)
    private final String reviewId;

    @SerializedName(MovieNetworkContract.KEY_MOVIE_REVIEW_AUTHOR)
    private final String author;

    @SerializedName(MovieNetworkContract.KEY_MOVIE_REVIEW_CONTENT)
    private final String content;

    @SerializedName(MovieNetworkContract.KEY_MOVIE_REVIEW_URL)
    private final String url;

    // constructor to be populated when network returns the result.
    public DbNwMovieReview(String reviewId, String author, String content, String url) {
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    @NonNull
    @Override
    public UiMovieReview constructUiMovie() {
        return new UiMovieReview(reviewId, author, content, url);
    }
}
