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

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("adult")
    private boolean isAdultMovie;

    @SerializedName("overview")
    private String overview;        // description of the movie.

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("id")
    private long movieId;

    @SerializedName("original_title")
    private String originalTitle;   // primary title of the movie.

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("title")
    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("vote_count")
    private long voteCount;

    @SerializedName("video")
    private boolean isVideoPresent;

    @SerializedName("vote_average")
    private float voteAverage;

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
