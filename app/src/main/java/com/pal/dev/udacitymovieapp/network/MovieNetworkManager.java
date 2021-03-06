/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network;

/*
 * Created by Palash on 26/02/17.
 */

import com.pal.dev.udacitymovieapp.annotation.SortType;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieReview;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieTrailer;

import java.util.List;

public interface MovieNetworkManager {

    void getPopularMovies(@SortType String aSortType,
                          NetworkOperationCallback<String, List<UiMovie>> networkOperationCallback);

    void getMovieTrailers(long movieId,
                          NetworkOperationCallback<String, List<UiMovieTrailer>> networkOperationCallback);

    void getMovieReviews(long movieId,
                         NetworkOperationCallback<String, List<UiMovieReview>> networkOperationCallback);
}
