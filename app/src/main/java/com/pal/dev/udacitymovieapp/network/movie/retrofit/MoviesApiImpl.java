/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pal.dev.udacitymovieapp.network.MovieNetworkManager;
import com.pal.dev.udacitymovieapp.network.NetworkOperationCallback;
import com.pal.dev.udacitymovieapp.network.movie.DbNwMovie;
import com.pal.dev.udacitymovieapp.network.movie.MovieJsonDeserializer;
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

    private Retrofit retrofit;

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
    public void getPopularMovies(final NetworkOperationCallback<String, List<UiMovie>> networkOperationCallback) {

        retrofit.create(MoviesApiService.class).fetchPopularMovies(ConfigConstant.API_KEY)
                .enqueue(new Callback<List<DbNwMovie>>() {

            @Override
            public void onResponse(Call<List<DbNwMovie>> call, Response<List<DbNwMovie>> response) {

                List<UiMovie> uiMovies = new ArrayList<>();

                for(DbNwMovie dbNwMovie : response.body()) {
                    uiMovies.add(dbNwMovie.constructUiMovie());
                    Log.d("BUGS","Name of Moview: " + dbNwMovie.getOriginalTitle());
                }

                if(networkOperationCallback != null) {
                    networkOperationCallback.onSuccess("", uiMovies);
                }
            }

            @Override
            public void onFailure(Call<List<DbNwMovie>> call, Throwable t) {

            }
        });

    }

    @Override
    public void cancelPopularMoviesRequest() {

    }


}
