/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovie;
import com.pal.dev.udacitymovieapp.utility.NetworkUtils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Palash on 26/02/17.
 *
 * adapter class for movie posters.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterViewHolder> {

    private final Context mContext;

    private final ArrayList<UiMovie> mMovieList;

    final private ListItemClickListener mListItemClickCallback;

    public MoviePosterAdapter(@NonNull Context aContext,
                              @NonNull List<UiMovie> aMovieList,
                              ListItemClickListener aListItemClickCallback) {
        mContext = aContext;
        mMovieList = new ArrayList<>(aMovieList);
        mListItemClickCallback = aListItemClickCallback;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_item_movie_poster, parent, false);
        return new MoviePosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        holder.bind(mContext, mMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void updateDataList(List<UiMovie> aUpdatedList) {
        mMovieList.clear();
        mMovieList.addAll(aUpdatedList);
    }

    class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mIvMoviePoster;

        MoviePosterViewHolder(View itemView) {
            super(itemView);
            mIvMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            // set onclick listener.
            itemView.setOnClickListener(this);
        }

        void bind(Context aContext, UiMovie aMovie) {

            if(NetworkUtils.isOnline(aContext)) {
                // This is how we use Picasso to load images from the internet.
                Picasso.with(aContext)
                        .load(aMovie.getPosterFullImagePath())
                        .placeholder(R.color.colorAccent)
                        .into(mIvMoviePoster);
            } else {
                // This is how we use Picasso to load images from the cache.
                Picasso.with(aContext)
                        .load(aMovie.getPosterFullImagePath())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.color.colorAccent)
                        .into(mIvMoviePoster);
            }

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            if(mListItemClickCallback != null) {
                mListItemClickCallback.onListItemClick(clickedPosition);
            }
        }
    }
}
