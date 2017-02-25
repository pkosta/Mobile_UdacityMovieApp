/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie.retrofit;

/*
 * Created by Palash on 26/02/17.
 */

import retrofit2.http.GET;

public interface MoviesApiService {

    @GET("/movie/popular")
    void fetchPopularMovies();

}
