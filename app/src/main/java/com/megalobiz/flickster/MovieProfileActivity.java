package com.megalobiz.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.megalobiz.flickster.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_profile);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        // Get the views
        TextView title = (TextView) findViewById(R.id.tvTitle);
        TextView tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        TextView overview = (TextView) findViewById(R.id.tvOverview);
        TextView voteCount = (TextView) findViewById(R.id.tvVoteCount);
        ImageView imageView = (ImageView) findViewById(R.id.ivMovieImage);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rbMovie);

        // Set value to the Views
        title.setText(movie.getOriginalTitle());
        tvReleaseDate.append(movie.getReleaseDate().toString());
        overview.setText(movie.getOverview());
        voteCount.append(movie.getVoteCount().toString());

        ratingBar.setRating(movie.getVoteAverage());

        Picasso.with(this).load(movie.getBackdropPath())
                .placeholder(R.drawable.movie_placeholder)
                .into(imageView);
    }

    public void onBack(View view) {
        finish();
    }
}
