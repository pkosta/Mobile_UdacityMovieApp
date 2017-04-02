/*
 * Copyright (c) 2017. The Android Open Source Project
 */
package com.pal.dev.udacitymovieapp.database.sqlite;

/*
 * Created by Palash on 26/03/17.
 */

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMovieDbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FavoriteMovieDbContract() {
    }


    /**
     * Add content provider constants to the Contract.
     * Client need to know how to access the task data, and it's your job to provide these content
     * URI's for the path to that data:
     * 1) Content authority.
     * 2) Base content URI
     * 3) Path(s) to the tasks directory.
     * 4) Content URI for the data in the MovieDetails class.
     */
    // The authority, which is how your code knows which Content Provider to access.
    public static final String AUTHORITY = "com.pal.dev.udacitymovieapp";

    // The base contract URI = "content://" + <authority>
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAV_MOVIE_LIST = "favorite_movies";



    /* Inner class that defines the table contents */
    public static class MovieDetails implements BaseColumns {

        // complete content URI.
        public static final Uri CONTENT_URI_MOVIE_LIST =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIE_LIST).build();

        public static final String TABLE_NAME = "favorite_movies";

        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";


        public static Uri buildContentUriWithId(long movieId) {
            return CONTENT_URI_MOVIE_LIST.buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .build();
        }
    }


}
