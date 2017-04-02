/*
 * Copyright (c) 2017. The Android Open Source Project
 */

package com.pal.dev.udacitymovieapp.userinterface.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pal.dev.udacitymovieapp.databinding.SingleItemMovieReviewBinding;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieReview;

import java.util.List;

/*
 * Created by Palash on 25/03/17.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {

    private final Context mContext;

    private final List<UiMovieReview> mMovieReviewList;

    private final ListItemClickListener mItemClickListener;

    public MovieReviewsAdapter(Context context, List<UiMovieReview> movieReviews,
                               @Nullable ListItemClickListener listener) {

        mContext = context;
        mMovieReviewList = movieReviews;
        mItemClickListener = listener;

    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View v= LayoutInflater.from(mContext)
//                .inflate(R.layout.single_item_movie_review, parent, false);
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        SingleItemMovieReviewBinding itemBinding =
                SingleItemMovieReviewBinding.inflate(layoutInflater, parent, false);

        return new MovieReviewViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        holder.bind(mContext, mMovieReviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieReviewList.size();
    }

    public void updateDataList(List<UiMovieReview> movieTrailers) {
        mMovieReviewList.clear();
        mMovieReviewList.addAll(movieTrailers);
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final SingleItemMovieReviewBinding itemBinding;

        public MovieReviewViewHolder(SingleItemMovieReviewBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            this.itemBinding = binding;
        }

        void bind(Context aContext, UiMovieReview aMovieReview) {

            // This is how we use Picasso to load images from the internet.
            itemBinding.tvMovieReviewAuthor.setText(aMovieReview.getAuthor());
            itemBinding.tvMovieReviewContent.setText(aMovieReview.getContent());

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
