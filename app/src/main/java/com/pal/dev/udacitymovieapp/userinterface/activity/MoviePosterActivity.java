/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pal.dev.udacitymovieapp.R;

/**
 * Displays the posters for all needed movies in grid manner.
 *
 * @author Palash Kosta
 */

public class MoviePosterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);
    }
}
