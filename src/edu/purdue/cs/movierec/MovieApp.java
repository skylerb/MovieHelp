package edu.purdue.cs.movierec;

import java.util.List;

import android.app.Application;

public class MovieApp extends Application {
	
	private MovieClient movieClient;
	private List<Movie> movies;
	
	public void setMovieClient(MovieClient mc) {
		this.movieClient = mc;
	}
	
	public MovieClient getMovieClient() {
		return movieClient;
	}
	
	public void setMovieList(List<Movie> movies) {
		this.movies = movies;
	}
	
	public List<Movie> getMovieList() {
		return movies;
	}
	
	public void clearMemory() {
		this.movies.clear();
		this.movieClient = null;
	}
}
