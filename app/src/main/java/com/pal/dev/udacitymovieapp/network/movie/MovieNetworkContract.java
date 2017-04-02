/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie;

/*
 * Created by Palash on 26/02/17.
 */

public class MovieNetworkContract {

    // configurations belows

    public static final String BASE_URL = "https://api.themoviedb.org";

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    public final static String IMAGE_SIZE = "w185";

    public static final String API_KEY = "";      // TODO - Place your key here.

    // json keys below

    public static final String CONSTANT_API_KEY = "api_key";

    static final String CONSTANT_ROOT_TAG = "results";

    public static final String SORT_POPULARITY = "popular";

    public static final String SORT_TOP_RATED = "top_rated";

    static final String KEY_POSTER_PATH = "poster_path";

    static final String KEY_ADULT = "adult";

    static final String KEY_OVERVIEW = "overview";

    static final String KEY_RELEASE_DATE = "release_date";

    static final String KEY_MOVIE_ID = "id";

    static final String KEY_ORIGINAL_TITLE = "original_title";

    static final String KEY_ORIGINAL_LANGUAGE = "original_language";

    static final String KEY_TITLE = "title";

    static final String KEY_BACKDROP_PATH = "backdrop_path";

    static final String KEY_POPULARITY = "popularity";

    static final String KEY_VOTE_COUNT = "vote_count";

    static final String KEY_VIDEO = "video";

    static final String KEY_VOTE_AVERAGE = "vote_average";

    // GET MOVIE VIDEOS

    static final String KEY_MOVIE_VIDEO_ID = "id";

    static final String KEY_MOVIE_VIDEO_KEY = "key";

    static final String KEY_MOVIE_VIDEO_NAME = "name";

    static final String KEY_MOVIE_VIDEO_SITE = "site";

    static final String KEY_MOVIE_VIDEO_SIZE = "size";

    static final String KEY_MOVIE_VIDEO_TYPE = "type";

    // GET MOVIE REVIEWS

    static final String KEY_MOVIE_REVIEW_ID = "id";

    static final String KEY_MOVIE_REVIEW_AUTHOR = "author";

    static final String KEY_MOVIE_REVIEW_CONTENT = "content";

    static final String KEY_MOVIE_REVIEW_URL = "url";
}
