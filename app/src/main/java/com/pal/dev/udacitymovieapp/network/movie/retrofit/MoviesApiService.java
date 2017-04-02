/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie.retrofit;

/*
 * Created by Palash on 26/02/17.
 */

import com.pal.dev.udacitymovieapp.network.movie.DbNwMovie;
import com.pal.dev.udacitymovieapp.network.movie.DbNwMovieReview;
import com.pal.dev.udacitymovieapp.network.movie.DbNwMovieTrailer;
import com.pal.dev.udacitymovieapp.network.movie.MovieNetworkContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MoviesApiService {

    @GET("/3/movie/{sort}")
    Call<List<DbNwMovie>> fetchPopularMovies(
            @Path("sort") String aSortOrder,
            @Query(MovieNetworkContract.CONSTANT_API_KEY) String key);

    @GET("/3/movie/{movie_id}/videos")
    Call<List<DbNwMovieTrailer>> fetchMovieTrailers(
            @Path("movie_id") long aMovieId,
            @Query(MovieNetworkContract.CONSTANT_API_KEY) String key);

    @GET("/3/movie/{id}/reviews")
    Call<List<DbNwMovieReview>> fetchMovieRevies(
            @Path("id") long aMovieId,
            @Query(MovieNetworkContract.CONSTANT_API_KEY) String key);

}
