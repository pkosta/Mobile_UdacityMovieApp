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

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.databinding.SingleItemMoviePosterBinding;
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

        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        SingleItemMoviePosterBinding itemBinding =
                SingleItemMoviePosterBinding.inflate(layoutInflater, parent, false);

        /*View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_item_movie_poster, parent, false);*/

        return new MoviePosterViewHolder(itemBinding);
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


        final SingleItemMoviePosterBinding itemBinding;

        MoviePosterViewHolder(SingleItemMoviePosterBinding binding) {
            super(binding.getRoot());
            // set onclick listener.
            binding.getRoot().setOnClickListener(this);
            this.itemBinding = binding;
        }

        void bind(Context aContext, UiMovie aMovie) {

            if(NetworkUtils.isOnline(aContext)) {
                // This is how we use Picasso to load images from the internet.
                Picasso.with(aContext)
                        .load(aMovie.getPosterFullImagePath())
                        .placeholder(R.color.colorAccent)
                        .into(itemBinding.ivMoviePoster);
            } else {
                // This is how we use Picasso to load images from the cache.
                Picasso.with(aContext)
                        .load(aMovie.getPosterFullImagePath())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.color.colorAccent)
                        .into(itemBinding.ivMoviePoster);
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
