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
    public static class ViewHolder {
        TextView title;
        TextView overview;
        ImageView movieImage;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    //Return int for the type according to popularity of movie (1 or 0)
    @Override
    public int getItemViewType(int position) {
        //return 1 if movie is popular, or 0 if not
        return getItem(position).getVoteAverage() > 5 ? 1 : 0;
    }

    //Total number of type is 2 (popular an non-popular)
    @Override
    public int getViewTypeCount() {
        //For Movie popularity, either a Movie is popular, or it is not (2 possibilities)
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the data item for position
        Movie movie = getItem(position);

        //Get data item movie type for this position
        int type = getItemViewType(position);

        // check if an existing view being reused, if not inflate the view

        // View Lookup cache stored in tag
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            //Inflate the XML layout based on the type
            convertView = getInflatedLayoutForType(type);

            //Just in case convertView return null, inflate the default item_movie
            //if(convertView == null) convertView = inflater.inflate(R.layout.item_movie, parent, false);

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
        // Because item movie popular type contain only movieImage,
        // title and overview can e null sometimes
        // Set Text if title and overview are not null
        if(viewHolder.title != null)
            viewHolder.title.setText(movie.getOriginalTitle());
        if(viewHolder.overview != null)
            viewHolder.overview.setText(movie.getOverview());

        //Detect Layout Orientation to change dynamically from posterPath in Portait to backdropPath in Landscape
        int orientation = getContext().getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            //load image according to movie item time
            if(type == 1) {
                Picasso.with(getContext()).load(movie.getBackdropPath())
                        .placeholder(R.drawable.movie_placeholder)
                        .into(viewHolder.movieImage);
            } else {
                Picasso.with(getContext()).load(movie.getPosterPath())
                        .placeholder(R.drawable.movie_placeholder)
                        .into(viewHolder.movieImage);
            }


        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(getContext()).load(movie.getBackdropPath())
                    .placeholder(R.drawable.movie_placeholder)
                    .into(viewHolder.movieImage);
        }

        // return the view
        return convertView;
    }

    //Method to return the correct inflated XML Layout file depending on the item movie popularity
    private View getInflatedLayoutForType(int type) {

        //if movie is popular inflate item_movie_popular, else inflate item_movie
        if (type == 1) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, null);
        } else if (type == 0) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else {
            return null;
        }
    }

}
