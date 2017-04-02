/*
 * Copyright (c) 2017. The Android Open Source Project
 */
package com.pal.dev.udacitymovieapp.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by Palash on 26/03/17.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FavMovie.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FavoriteMovieDbContract.MovieDetails.TABLE_NAME + " (" +
                    FavoriteMovieDbContract.MovieDetails._ID + " INTEGER PRIMARY KEY," +
                    FavoriteMovieDbContract.MovieDetails.COLUMN_MOVIE_ID + " INTEGER," +
                    FavoriteMovieDbContract.MovieDetails.COLUMN_ORIGINAL_TITLE + " TEXT," +
                    FavoriteMovieDbContract.MovieDetails.COLUMN_OVERVIEW + " TEXT," +
                    FavoriteMovieDbContract.MovieDetails.COLUMN_POSTER_PATH + " TEXT," +
                    FavoriteMovieDbContract.MovieDetails.COLUMN_RELEASE_DATE + " TEXT," +
                    FavoriteMovieDbContract.MovieDetails.COLUMN_VOTE_AVERAGE + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoriteMovieDbContract.MovieDetails.TABLE_NAME;


    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}
