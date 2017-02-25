/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.model;

/*
 * Created by Palash on 26/02/17.
 */

@SuppressWarnings("unused")
public class UiMovie {

    private String posterPath;

    private String overview;        // description of the movie.

    private String releaseDate;

    private long movieId;

    private String originalTitle;   // primary title of the movie.

    private double popularity;

    private long voteCount;

    private float voteAverage;

    // constructor - attributes required only used for UI.
    public UiMovie(String posterPath, String overview,
                   String releaseDate, long movieId,
                   String originalTitle, double popularity,
                   long voteCount, float voteAverage) {

        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;

    }

    public String getPosterPath() {
        return posterPath;
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

    public double getPopularity() {
        return popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }
}
