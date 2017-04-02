/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pal.dev.udacitymovieapp.databinding.SingleItemMovieVideoBinding;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieTrailer;

import java.util.List;

/*
 * Created by Palash on 25/03/17.
 */

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.MovieVideoViewHolder> {

    private final Context mContext;

    private final List<UiMovieTrailer> mMovieTrailerList;

    private final ListItemClickListener mItemClickListener;

    public MovieVideosAdapter(Context context, List<UiMovieTrailer> movieTrailers,
                              ListItemClickListener listener) {

        mContext = context;
        mMovieTrailerList = movieTrailers;
        mItemClickListener = listener;

    }

    @Override
    public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /*View v= LayoutInflater.from(mContext)
                .inflate(R.layout.single_item_movie_video, parent, false);*/
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleItemMovieVideoBinding itemBinding =
                SingleItemMovieVideoBinding.inflate(inflater, parent, false);
        return new MovieVideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(MovieVideoViewHolder holder, int position) {
        holder.bind(mContext, mMovieTrailerList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieTrailerList.size();
    }

    public void updateDataList(List<UiMovieTrailer> movieTrailers) {
        mMovieTrailerList.clear();
        mMovieTrailerList.addAll(movieTrailers);
    }

    class MovieVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final SingleItemMovieVideoBinding itemBinding;

        public MovieVideoViewHolder(SingleItemMovieVideoBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            this.itemBinding = binding;
        }

        void bind(Context aContext, UiMovieTrailer aMovieTrailer) {

            // This is how we use Picasso to load images from the internet.
            itemBinding.tvMovieTrailerName.setText(aMovieTrailer.getVideoName());

        }

        @Override
        public void onClick(View v) {
            int clickedPos = getAdapterPosition();
            if(mItemClickListener != null) {
                mItemClickListener.onListItemClick(clickedPos);
            }
        }
    }
}
