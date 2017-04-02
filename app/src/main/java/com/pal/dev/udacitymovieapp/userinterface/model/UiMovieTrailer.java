/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.model;

/*
 * Created by Palash on 25/03/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class UiMovieTrailer implements Parcelable{

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

    private UiMovieTrailer(Parcel in) {
        this.videoId = in.readString();
        this.videoKey = in.readString();
        this.videoName = in.readString();
        this.videoSite = in.readString();
        this.videoSize = in.readLong();
        this.videoType = in.readString();
    }


    public static final Creator<UiMovieTrailer> CREATOR = new Creator<UiMovieTrailer>() {
        @Override
        public UiMovieTrailer createFromParcel(Parcel in) {
            return new UiMovieTrailer(in);
        }

        @Override
        public UiMovieTrailer[] newArray(int size) {
            return new UiMovieTrailer[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoId);
        dest.writeString(this.videoKey);
        dest.writeString(this.videoName);
        dest.writeString(this.videoSite);
        dest.writeLong(this.videoSize);
        dest.writeString(this.videoType);
    }
}
