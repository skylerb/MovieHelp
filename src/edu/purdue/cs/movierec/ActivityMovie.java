package edu.purdue.cs.movierec;

import java.io.InputStream;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityMovie extends Activity {
	
	MovieApp appState;
	ActionBar actionBar;
	Movie movie;
	
	private TextView movieTitle;
	private TextView movieSubTitle;
	private TextView movieActors;
	private TextView movieGenres;
	private TextView movieDirectors;
	private ImageView coverArt;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			landscape();
		} else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			portrait();
		}
	}
	
	public void landscape() {
		setContentView(R.layout.movie_landscape);
		
		movieSubTitle = (TextView)findViewById(R.id.movie_subtitle_landscape);
		coverArt = (ImageView)findViewById(R.id.coverArtMovie);
		movieGenres = (TextView)findViewById(R.id.genres_landscape);
		movieActors = (TextView)findViewById(R.id.actors_landscape);
		movieDirectors = (TextView)findViewById(R.id.directors_landscape);
		
		actionBar.setTitle(movie.getTitle());
		
		coverArt.setImageBitmap(movie.getCoverart());
    	movieSubTitle.setText(getSubtitle()); 
    	movieGenres.setText(getGenres());
    	movieActors.setText(getActors());
    	movieDirectors.setText(getDirectors());
    	
	}
	
	public void portrait() {
        setContentView(R.layout.movie);
        
        movieTitle = (TextView)findViewById(R.id.movie_title);
        movieSubTitle = (TextView)findViewById(R.id.movie_subtitle);
        movieActors = (TextView)findViewById(R.id.actors); 
        movieGenres = (TextView)findViewById(R.id.genres);
        movieDirectors = (TextView)findViewById(R.id.directors);
        
        actionBar.setTitle(R.string.app_name);
        
    	new DownloadImageTask((ImageView) findViewById(R.id.fanart)).execute("http://cf2.imgobject.com/t/p/original/qfXeiDyuvLgfXqypnP5e3t8mxV4.jpg");
    	movieTitle.setText(movie.getTitle());    
    	movieSubTitle.setText(getSubtitle());                   
        movieGenres.setText(getGenres());        
        movieActors.setText(getActors());        
        movieDirectors.setText(getDirectors());
        
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WindowManager mWindowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();
        
        appState = (MovieApp)getApplicationContext();
        movie =  appState.getMovie(); 
        
        actionBar = this.getActionBar();
        
        if(mDisplay.getRotation() > 0) {
        	landscape();
        } else {
        	portrait();
        }    
    }
    
    public String getSubtitle() {
    	StringBuilder subtitle = new StringBuilder();
        switch(movie.getMPAA()) {
        	case R.drawable.g: subtitle.append("G, "); break; //G
        	case R.drawable.pg: subtitle.append("PG, "); break; //PG
        	case R.drawable.pg13: subtitle.append("PG-13, "); break; //PG-13
        	case R.drawable.r: subtitle.append("R, "); break;
        	case R.drawable.nc17: subtitle.append("NC-17, "); break;
        	case R.drawable.approved: subtitle.append("Approved, "); break;
        	case R.drawable.tv14: subtitle.append("TV-14, "); break;
        	case R.drawable.ur: subtitle.append("UR, "); break; 
        	case R.drawable.nr: subtitle.append("NR, "); break;
        }
        subtitle.append(movie.getYear());   
        
        return subtitle.toString();
    }
    
    public String getActors() {
    	//Actors
        StringBuilder actors = new StringBuilder();
        actors.append(movie.getActors().get(0));
        for(int i = 1; i < movie.getActors().size(); i++) {
        	actors.append(", " + movie.getActors().get(i));
        }
        
        return actors.toString();
    }

    public String getDirectors() {
    	//Directors
        StringBuilder directors = new StringBuilder();
        directors.append(movie.getDirectors().get(0));
        for(int i = 1; i < movie.getDirectors().size(); i++) {
        	directors.append(", " + movie.getDirectors().get(i));
        }
        
        return directors.toString();
    }
    
    public String getGenres() {
    	//Genres
        StringBuilder genres = new StringBuilder();
        genres.append(movie.getGenres().get(0));
        for(int i = 1; i < movie.getGenres().size(); i++) {
        	genres.append(", " + movie.getGenres().get(i));
        }
        
        return genres.toString();
    }
    
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
