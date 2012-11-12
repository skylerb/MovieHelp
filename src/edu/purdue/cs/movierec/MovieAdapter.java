package edu.purdue.cs.movierec;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<Movie> {
	
	Context context;
	int layoutResourceId;
	List<Movie> movies = null;
	
	public MovieAdapter(Context context, int layoutResourceId, List<Movie> movies) {
		super(context,layoutResourceId, movies);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.movies = movies;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MovieHolder holder = null;
		
		if(row==null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new MovieHolder();
			holder.movieTitle = (TextView)row.findViewById(R.id.movieTitle);
			holder.movieYear = (TextView)row.findViewById(R.id.movieYear);
			holder.mpaaIcon = (ImageView)row.findViewById(R.id.mpaaIcon);
			holder.movieGenres = (TextView)row.findViewById(R.id.movieGenres);
			holder.coverArt = (ImageView)row.findViewById(R.id.coverArt);
			
			
			row.setTag(holder);
		} else {
			holder = (MovieHolder)row.getTag();
		}
		
		Movie movie = movies.get(position);
		holder.movieTitle.setText(movie.getTitle());
		holder.movieYear.setText("(" + movie.getYear() + ")");
		holder.mpaaIcon.setImageResource(movie.getMPAA());
		StringBuilder genre = new StringBuilder();
		genre.append(movie.getGenres().get(0));
		for(int i = 1; i < movie.getGenres().size(); i++) {
			genre.append(" | " + movie.getGenres().get(i));
		}
		holder.movieGenres.setText(genre.toString());
		holder.coverArt.setImageBitmap(movie.getCoverart());
		
		return row;
		
	}
	
	static class MovieHolder {
		ImageView mpaaIcon;
		TextView movieTitle;
		TextView movieYear;
		TextView movieGenres;
		ImageView coverArt;
	}

}
