package edu.purdue.cs.movierec;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class Movie {
	
	private String title;
	private int mpaa;
	private int year;
	private List<String> genres =  new ArrayList<String>();
	private Bitmap coverart;
	
	public Movie(String title, int mpaa, int year, List<String> genres, Bitmap image) {
		this.title = title;
		this.mpaa = mpaa;
		this.year = year;
		this.genres = genres;
		this.coverart = image;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMPAA() {
		return mpaa;
	}
	
	public List<String> getGenres() {
		return genres;
	}
	
	public Bitmap getCoverart() {
		return coverart;
	}
	
	
}
