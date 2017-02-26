/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network;

/**
 * Created by Palash on 26/02/17.
 */

public interface NetworkOperationCallback<Request, Response> {

    void onSuccess(Request request, Response response);

    void onFailure(Request request, Throwable throwable);

    void onUnAuthorized();

}
