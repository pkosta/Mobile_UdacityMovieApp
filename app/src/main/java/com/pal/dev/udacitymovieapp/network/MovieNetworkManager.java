/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network;

/*
 * Created by Palash on 26/02/17.
 */

import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;

import java.util.List;

public interface MovieNetworkManager {

    void getPopularMovies(NetworkOperationCallback<String, List<UiMovie>> networkOperationCallback);

    void cancelPopularMoviesRequest();
}
