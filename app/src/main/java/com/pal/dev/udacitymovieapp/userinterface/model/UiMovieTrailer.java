package com.pal.dev.udacitymovieapp.userinterface.model;

/**
 * Created by Palash on 25/03/17.
 */

public class UiMovieTrailer {

    private final String videoId;

    private final String videoKey;

    private final String videoName;

    private final String videoSite;

    private final long videoSize;

    private final String videoType;

    // constructor - attributes required only used for UI.
    public UiMovieTrailer(String videoId, String videoKey,
                          String videoName, String videoSite,
                          long videoSize, String videoType) {

        this.videoId = videoId;
        this.videoKey = videoKey;
        this.videoName = videoName;
        this.videoSite = videoSite;
        this.videoSize = videoSize;
        this.videoType = videoType;

    }


    public String getVideoId() {
        return videoId;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public String getVideoType() {
        return videoType;
    }
}
