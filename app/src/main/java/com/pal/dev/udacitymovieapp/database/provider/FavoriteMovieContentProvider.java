/*
 * Copyright (c) 2017. The Android Open Source Project
 */
package com.pal.dev.udacitymovieapp.database.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pal.dev.udacitymovieapp.database.sqlite.FavoriteMovieDbContract;
import com.pal.dev.udacitymovieapp.database.sqlite.FavoriteMovieDbHelper;
import com.pal.dev.udacitymovieapp.utility.Constant;

import static com.pal.dev.udacitymovieapp.database.sqlite.FavoriteMovieDbContract.MovieDetails.CONTENT_URI_MOVIE_LIST;
import static com.pal.dev.udacitymovieapp.database.sqlite.FavoriteMovieDbContract.MovieDetails.TABLE_NAME;

/*
 * Created by Palash on 26/03/17.
 */

public class FavoriteMovieContentProvider extends ContentProvider {

    public static final int FAV_MOVIE_LIST = 100;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMovieDbContract.AUTHORITY,
                FavoriteMovieDbContract.PATH_FAV_MOVIE_LIST, FAV_MOVIE_LIST);
        return uriMatcher;
    }

    // Member variable for a FavoriteMovieDbHelper that's initialized in the onCreate() method.
    FavoriteMovieDbHelper mFavoriteMovieDbHelper;

    @Override
    public boolean onCreate() {
        // best place to initialize the underlying database. e.g SQLite or MangoDb etc,
        Context context = getContext();
        mFavoriteMovieDbHelper = new FavoriteMovieDbHelper(context);

        return true;        // return true when this method is done.
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = mFavoriteMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor refCursor;

        switch (match) {
            case FAV_MOVIE_LIST:

                refCursor = sqLiteDatabase.query(FavoriteMovieDbContract.MovieDetails.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        // notification will be given to the cursor.
        if(getContext() != null) {
            // cursor will be known if anything changes.
            refCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return refCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    // bulk insert - not using for now in this application.
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        SQLiteDatabase sqLiteDatabase = mFavoriteMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAV_MOVIE_LIST:

                // will be done by transaction.
                // start the transaction.
                sqLiteDatabase.beginTransaction();
                int numberOfRowsInserted = 0;

                try {

                    // try to insert all data.
                    for(ContentValues cv : values) {

                        long _id = sqLiteDatabase.insert(
                                FavoriteMovieDbContract.MovieDetails.TABLE_NAME, null, cv);

                        if(_id != -1) {
                            numberOfRowsInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();

                } finally {
                    // end the transaction.
                    sqLiteDatabase.endTransaction();
                }

                if(numberOfRowsInserted > 0 && getContext() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return numberOfRowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }


    }

    // single insert.
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = mFavoriteMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAV_MOVIE_LIST:

                long id = sqLiteDatabase.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(CONTENT_URI_MOVIE_LIST, id);
                    Log.d(Constant.APP_LOG_TAG, "Insert Successful: " + uri);
                } else {
                    // failure
                    throw new SQLException("Failed to insert the row for URI : " +uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        // Notify the resolver if the uri has been changed.
        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        // close the database instance.
        sqLiteDatabase.close();

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         *********** Good Concept ***********
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case FAV_MOVIE_LIST:
                numRowsDeleted = mFavoriteMovieDbHelper.getWritableDatabase().delete(
                        FavoriteMovieDbContract.MovieDetails.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}
