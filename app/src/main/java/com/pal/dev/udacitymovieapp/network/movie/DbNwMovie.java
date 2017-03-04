/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie;

/*
 * Created by Palash on 26/02/17.
 */

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.pal.dev.udacitymovieapp.userinterface.model.DbNwMovieInterface;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;

@SuppressWarnings("unused")
public class DbNwMovie implements DbNwMovieInterface {

    @SerializedName(NetworkKeyConstant.KEY_POSTER_PATH)
    private final String posterPath;

    @SerializedName(NetworkKeyConstant.KEY_ADULT)
    private final boolean isAdultMovie;

    @SerializedName(NetworkKeyConstant.KEY_OVERVIEW)
    private final String overview;        // description of the movie.

    @SerializedName(NetworkKeyConstant.KEY_RELEASE_DATE)
    private final String releaseDate;

    @SerializedName(NetworkKeyConstant.KEY_MOVIE_ID)
    private final long movieId;

    @SerializedName(NetworkKeyConstant.KEY_ORIGINAL_TITLE)
    private final String originalTitle;   // primary title of the movie.

    @SerializedName(NetworkKeyConstant.KEY_ORIGINAL_LANGUAGE)
    private final String originalLanguage;

    @SerializedName(NetworkKeyConstant.KEY_TITLE)
    private final String title;

    @SerializedName(NetworkKeyConstant.KEY_BACKDROP_PATH)
    private final String backdropPath;

    @SerializedName(NetworkKeyConstant.KEY_POPULARITY)
    private final double popularity;

    @SerializedName(NetworkKeyConstant.KEY_VOTE_COUNT)
    private final long voteCount;

    @SerializedName(NetworkKeyConstant.KEY_VIDEO)
    private final boolean isVideoPresent;

    @SerializedName(NetworkKeyConstant.KEY_VOTE_AVERAGE)
    private final float voteAverage;

    // constructor to be populated when network returns the result.

    public DbNwMovie(String posterPath, boolean isAdultMovie,
                     String overview, String releaseDate,
                     long movieId, String originalTitle,
                     String originalLanguage, String title,
                     String backdropPath, double popularity,
                     long voteCount, boolean isVideoPresent,
                     float voteAverage) {

        this.posterPath = posterPath;
        this.isAdultMovie = isAdultMovie;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.isVideoPresent = isVideoPresent;
        this.voteAverage = voteAverage;
    }


    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdultMovie() {
        return isAdultMovie;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public long getMovieId() {
        return movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public boolean isVideoPresent() {
        return isVideoPresent;
    }

    public float getVoteAverage() {
        return voteAverage;
    }


    @Override
    public @NonNull UiMovie constructUiMovie() {
        return new UiMovie(this.posterPath, this.overview, this.releaseDate, this.movieId,
                this.originalTitle, this.popularity, this.voteCount, this.voteAverage);
    }

}
