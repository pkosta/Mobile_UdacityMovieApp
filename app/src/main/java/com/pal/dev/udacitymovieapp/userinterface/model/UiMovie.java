/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.model;

/**
 * Created by Palash on 26/02/17.
 *
 * Core object of the Movie. This object will be related to the object
 * {@link com.pal.dev.udacitymovieapp.network.movie.DbNwMovie}
 * which is used to get data from the server. And the same object will be used for DB operation.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.pal.dev.udacitymovieapp.utility.ConfigConstant;

@SuppressWarnings("unused")
public class UiMovie implements Parcelable {

    private final String posterPath;

    private final String overview;        // description of the movie.

    private final String releaseDate;

    private final long movieId;

    private final String originalTitle;   // primary title of the movie.

    private final double popularity;

    private final long voteCount;

    private final float voteAverage;

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

    private UiMovie(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        movieId = in.readLong();
        originalTitle = in.readString();
        popularity = in.readDouble();
        voteCount = in.readLong();
        voteAverage = in.readFloat();
    }

    public static final Creator<UiMovie> CREATOR = new Creator<UiMovie>() {
        @Override
        public UiMovie createFromParcel(Parcel in) {
            return new UiMovie(in);
        }

        @Override
        public UiMovie[] newArray(int size) {
            return new UiMovie[size];
        }
    };

    public String getPosterPath() {
        return ConfigConstant.IMAGE_BASE_URL + "/" + ConfigConstant.IMAGE_SIZE +
                "/" + posterPath;
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




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeLong(movieId);
        dest.writeString(originalTitle);
        dest.writeDouble(popularity);
        dest.writeLong(voteCount);
        dest.writeFloat(voteAverage);
    }

}
