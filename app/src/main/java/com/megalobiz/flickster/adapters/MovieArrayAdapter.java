package com.megalobiz.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.megalobiz.flickster.R;
import com.megalobiz.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by KeitelRobespierre on 7/13/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    //View Lookup Cache
    public static class ViewHolder{
        TextView title;
        TextView overview;
        ImageView movieImage;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for position
        Movie movie = getItem(position);

        // check if an existing view being reused, if not inflate the view

        // View Lookup cache stored in tag
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            // find the image view
            viewHolder.movieImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);

            // clear out image from convertView
            viewHolder.movieImage.setImageResource(0);

            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);

            // Cache the Views
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // poulate data if viewHolder is not null
        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.overview.setText(movie.getOverview());

        //Detect Layout Orientation to change dynamically from posterPath in Portait to backdropPath in Landscape
        int orientation = getContext().getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            Picasso.with(getContext()).load(movie.getPosterPath())
                    .placeholder(R.drawable.movie_placeholder)
                    .into(viewHolder.movieImage);
        }
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            Picasso.with(getContext()).load(movie.getBackdropPath())
                    .placeholder(R.drawable.movie_placeholder)
                    .into(viewHolder.movieImage);
        }

        // return the view
        return convertView;
    }
}
