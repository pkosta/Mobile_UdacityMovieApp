/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie.retrofit;

/*
 * Created by Palash on 26/02/17.
 */

import com.pal.dev.udacitymovieapp.network.movie.DbNwMovie;
import com.pal.dev.udacitymovieapp.network.movie.NetworkKeyConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MoviesApiService {

    @GET("/3/movie/{sort}")
    Call<List<DbNwMovie>> fetchPopularMovies(
            @Path("sort") String aSortOrder,
            @Query(NetworkKeyConstant.CONSTANT_API_KEY) String key);

}
