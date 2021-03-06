/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie;

/*
 * Created by Palash on 26/02/17.
 */

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.pal.dev.udacitymovieapp.userinterface.model.DbNwMovieTrailerInterface;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieTrailer;

@SuppressWarnings("unused")
public class DbNwMovieTrailer implements DbNwMovieTrailerInterface{

    @SerializedName(MovieNetworkContract.KEY_MOVIE_VIDEO_ID)
    private final String videoId;

    @SerializedName(MovieNetworkContract.KEY_MOVIE_VIDEO_KEY)
    private final String videoKey;

    @SerializedName(MovieNetworkContract.KEY_MOVIE_VIDEO_NAME)
    private final String videoName;

    @SerializedName(MovieNetworkContract.KEY_MOVIE_VIDEO_SITE)
    private final String videoSite;

    @SerializedName(MovieNetworkContract.KEY_MOVIE_VIDEO_SIZE)
    private final long videoSize;

    @SerializedName(MovieNetworkContract.KEY_MOVIE_VIDEO_TYPE)
    private final String videoType;

    // constructor to be populated when network returns the result.
    public DbNwMovieTrailer(String posterPath, String isAdultMovie,
                            String overview, String releaseDate,
                            long size, String originalTitle) {

        this.videoId = posterPath;
        this.videoKey = isAdultMovie;
        this.videoName = overview;
        this.videoSite = releaseDate;
        this.videoSize = size;
        this.videoType = originalTitle;
    }


    @NonNull
    @Override
    public UiMovieTrailer constructUiMovie() {
        return new UiMovieTrailer(videoId, videoKey, videoName, videoSite, videoSize, videoType);
    }
}
