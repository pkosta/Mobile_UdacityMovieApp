/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.network.movie;

/*
 * Created by Palash on 26/02/17.
 */

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

public class MovieJsonDeserializer implements JsonDeserializer<List<DbNwMovie>> {
    @Override
    public List<DbNwMovie> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        // Get the "content" element from the parsed JSON
        JsonElement content = json.getAsJsonObject().get(NetworkKeyConstant.CONSTANT_ROOT_TAG);

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(content, typeOfT);

    }
}
