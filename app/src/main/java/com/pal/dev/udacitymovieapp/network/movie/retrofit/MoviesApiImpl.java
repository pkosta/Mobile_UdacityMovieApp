/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pal.dev.udacitymovieapp.annotation.SortType;
import com.pal.dev.udacitymovieapp.network.MovieNetworkManager;
import com.pal.dev.udacitymovieapp.network.NetworkOperationCallback;
import com.pal.dev.udacitymovieapp.network.movie.DbNwMovie;
import com.pal.dev.udacitymovieapp.network.movie.MovieJsonDeserializer;
import com.pal.dev.udacitymovieapp.network.movie.NetworkKeyConstant;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;
import com.pal.dev.udacitymovieapp.utility.ConfigConstant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Created by Palash on 26/02/17.
 */

public class MoviesApiImpl implements MovieNetworkManager {

    private static final int RESPONSE_SUCCESS = 200;

    private static final int RESPONSE_UNAUTHORIZED = 401;

    private final Retrofit retrofit;

    // initialize the retrofit request.
    public MoviesApiImpl() {

        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(List.class, new MovieJsonDeserializer());
        Gson movieGsonConverter = gsonBuilder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(movieGsonConverter))
                .build();
    }

    @Override
    public void getPopularMovies(@SortType String aSortType,
                                 final NetworkOperationCallback<String, List<UiMovie>> networkOperationCallback) {

        if(aSortType.equalsIgnoreCase(SortType.SORT_TYPE_POPULARITY)) {

            retrofit.create(MoviesApiService.class).fetchPopularMovies(NetworkKeyConstant.SORT_POPULARITY,
                    ConfigConstant.API_KEY)

                    .enqueue(new Callback<List<DbNwMovie>>() {

                        @Override
                        public void onResponse(Call<List<DbNwMovie>> call, Response<List<DbNwMovie>> response) {

                            if(response != null && response.code() == RESPONSE_SUCCESS) {
                                if(response.body() != null) {
                                    List<UiMovie> uiMovies = new ArrayList<>();

                                    for(DbNwMovie dbNwMovie : response.body()) {
                                        uiMovies.add(dbNwMovie.constructUiMovie());
                                    }

                                    if(networkOperationCallback != null) {
                                        networkOperationCallback.onSuccess("", uiMovies);
                                    }
                                } else {
                                    if(networkOperationCallback != null) {
                                        networkOperationCallback.onFailure("", new IllegalStateException());
                                    }
                                }
                            } else if(response != null && response.code() == RESPONSE_UNAUTHORIZED){

                                if(networkOperationCallback != null) {
                                    networkOperationCallback.onUnAuthorized();
                                }

                            } else {
                                if(networkOperationCallback != null) {
                                    networkOperationCallback.onFailure("", new IllegalStateException());
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<DbNwMovie>> call, Throwable t) {
                            if(networkOperationCallback != null) {
                                networkOperationCallback.onFailure("",t);
                            }
                        }
                    });
        } else {

            retrofit.create(MoviesApiService.class).fetchPopularMovies(NetworkKeyConstant.SORT_TOP_RATED,
                    ConfigConstant.API_KEY)

                    .enqueue(new Callback<List<DbNwMovie>>() {

                        @Override
                        public void onResponse(Call<List<DbNwMovie>> call, Response<List<DbNwMovie>> response) {

                            if(response != null && response.code() == RESPONSE_SUCCESS) {

                                if(response.body() != null) {
                                    List<UiMovie> uiMovies = new ArrayList<>();

                                    for(DbNwMovie dbNwMovie : response.body()) {
                                        uiMovies.add(dbNwMovie.constructUiMovie());
                                    }

                                    if(networkOperationCallback != null) {
                                        networkOperationCallback.onSuccess("", uiMovies);
                                    }
                                } else {
                                    if(networkOperationCallback != null) {
                                        networkOperationCallback.onFailure("", new IllegalStateException());
                                    }
                                }

                            } else if(response != null && response.code() == RESPONSE_UNAUTHORIZED) {
                                if(networkOperationCallback != null) {
                                    networkOperationCallback.onUnAuthorized();
                                }
                            } else {
                                if(networkOperationCallback != null) {
                                    networkOperationCallback.onFailure("", new IllegalStateException());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<DbNwMovie>> call, Throwable t) {

                            if(networkOperationCallback != null) {
                                networkOperationCallback.onFailure("",t);
                            }
                        }
                    });

        }


    }


}
