/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie;

/*
 * Created by Palash on 26/02/17.
 */

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class DbNwMovieTrailer {

    @SerializedName(NetworkKeyConstant.KEY_MOVIE_VIDEO_ID)
    private final String videoId;

    @SerializedName(NetworkKeyConstant.KEY_MOVIE_VIDEO_KEY)
    private final String videoKey;

    @SerializedName(NetworkKeyConstant.KEY_MOVIE_VIDEO_NAME)
    private final String videoName;

    @SerializedName(NetworkKeyConstant.KEY_MOVIE_VIDEO_SITE)
    private final String videoSite;

    @SerializedName(NetworkKeyConstant.KEY_MOVIE_VIDEO_SIZE)
    private final long videoSize;

    @SerializedName(NetworkKeyConstant.KEY_MOVIE_VIDEO_TYPE)
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


}
