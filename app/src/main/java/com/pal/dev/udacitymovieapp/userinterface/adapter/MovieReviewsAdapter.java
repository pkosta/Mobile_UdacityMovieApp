package com.pal.dev.udacitymovieapp.userinterface.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pal.dev.udacitymovieapp.R;
import com.pal.dev.udacitymovieapp.userinterface.model.UiMovieReview;

import java.util.List;

/*
 * Created by Palash on 25/03/17.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {

    private Context mContext;

    private List<UiMovieReview> mMovieReviewList;

    private ListItemClickListener mItemClickListener;

    public MovieReviewsAdapter(Context context, List<UiMovieReview> movieReviews,
                               @Nullable ListItemClickListener listener) {

        mContext = context;
        mMovieReviewList = movieReviews;
        mItemClickListener = listener;

    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(mContext)
                .inflate(R.layout.single_item_movie_review, parent, false);
        return new MovieReviewViewHolder(v);
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

        TextView tvMovieReviewAuthor;

        TextView tvMovieReviewContent;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            tvMovieReviewAuthor = (TextView) itemView.findViewById(R.id.tv_movie_review_author);
            tvMovieReviewContent = (TextView) itemView.findViewById(R.id.tv_movie_review_content);
            itemView.setOnClickListener(this);
        }

        void bind(Context aContext, UiMovieReview aMovieReview) {

            // This is how we use Picasso to load images from the internet.
            tvMovieReviewAuthor.setText(aMovieReview.getAuthor());
            tvMovieReviewContent.setText(aMovieReview.getContent());

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
