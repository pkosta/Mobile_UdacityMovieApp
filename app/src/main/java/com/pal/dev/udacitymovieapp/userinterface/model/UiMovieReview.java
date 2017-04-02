/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by Palash on 01/04/17.
 */

public class UiMovieReview implements Parcelable {

    private final String id;

    private final String author;

    private final String content;

    public static final Creator<UiMovieReview> CREATOR = new Creator<UiMovieReview>() {
        @Override
        public UiMovieReview createFromParcel(Parcel in) {
            return new UiMovieReview(in);
        }

        @Override
        public UiMovieReview[] newArray(int size) {
            return new UiMovieReview[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    private final String url;



    public UiMovieReview(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public UiMovieReview(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
}
