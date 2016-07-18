package com.megalobiz.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.megalobiz.flickster.adapters.MovieArrayAdapter;
import com.megalobiz.flickster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MoviesActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeContainer;

    AsyncHttpClient client;
    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        // Lookup the Swipe Container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //Listen for Swipe Refresh to fetch Moives again
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Fetch Movies list again
                // Make sure to call swipeContainer.setRefreshing(fasle) once the
                // network has completed successfully
                fetchMoviesAsync();
            }
        });

        //Prepare the Movie view, array list and adapter
        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<Movie>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        //Listen for item click in ListView
        lvItems.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) lvItems.getItemAtPosition(position);
                //Toast.makeText(MoviesActivity.this, movie.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MoviesActivity.this, MovieProfileActivity.class);
                i.putExtra("movie", movie);
                startActivity(i);
            }
        });

        //Fetch Movie list on Create
        fetchMoviesAsync();

    }

    public void fetchMoviesAsync(){
        // Send the network request to fetch the updated data

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        // Instantiate AsyncHttpClient
        client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");

                    // Since we do not instantiate movies to a new ArrayList in this Method
                    // we have to clear movies collection first not to create duplicate movie
                    movies.clear();
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    // Log.d("DEBUG", movies.toString());

                    // Call swipeContainer.setRefreshing(false) in case this method has been
                    // called from RefreshListener to signal refresh has
                    swipeContainer.setRefreshing(false);

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }


}
