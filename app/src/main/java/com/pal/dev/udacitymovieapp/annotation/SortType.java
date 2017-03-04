/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/*
 * Created by Palash on 28/02/17.
 */

@StringDef({
        SortType.SORT_TYPE_POPULARITY,
        SortType.SORT_TYPE_RATING
})

@Retention(SOURCE)
public @interface SortType {

    String SORT_TYPE_POPULARITY = "sorting_popularity";

    String SORT_TYPE_RATING = "sorting_rating";

}




