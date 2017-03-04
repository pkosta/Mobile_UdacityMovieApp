/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network;

/*
 * Created by Palash on 26/02/17.
 */

import com.pal.dev.udacitymovieapp.network.movie.retrofit.MoviesApiImpl;

public class NetworkFactory {

    public MovieNetworkManager getMovieNetworkManager() {
        return new MoviesApiImpl();
    }

}
