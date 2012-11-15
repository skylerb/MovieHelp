package edu.purdue.cs.movierec;

import java.io.InputStream;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityMovie extends Activity {
	
	MovieApp appState;	
	Movie movie;
	
	private TextView movieTitle;
	private TextView movieSubTitle;
	private TextView movieActors;
	private TextView movieGenres;
	private TextView movieDirectors;
	private ImageView fanArt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie);
        
        appState = (MovieApp)getApplicationContext();
        movie =  appState.getMovie();
        
        movieTitle = (TextView)findViewById(R.id.movie_title);
        movieSubTitle = (TextView)findViewById(R.id.movie_subtitle);
        movieActors = (TextView)findViewById(R.id.actors); 
        movieGenres = (TextView)findViewById(R.id.genres);
        movieDirectors = (TextView)findViewById(R.id.directors);
        fanArt = (ImageView)findViewById(R.id.fanart);        
        
        
        //Send fanart from server or download through url?????
    	//fanArt.setImageResource(R.drawable.prometheus);
    	new DownloadImageTask((ImageView) findViewById(R.id.fanart)).execute("http://cf2.imgobject.com/t/p/original/qfXeiDyuvLgfXqypnP5e3t8mxV4.jpg");
        	
        movieTitle.setText(movie.getTitle());
        
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
        movieSubTitle.setText(subtitle.toString());
        
        //Genres
        StringBuilder genres = new StringBuilder();
        genres.append(movie.getGenres().get(0));
        for(int i = 1; i < movie.getGenres().size(); i++) {
        	genres.append(", " + movie.getGenres().get(i));
        }
        movieGenres.setText(genres.toString());
        
        //Actors
        StringBuilder actors = new StringBuilder();
        actors.append(movie.getActors().get(0));
        for(int i = 1; i < movie.getActors().size(); i++) {
        	actors.append(", " + movie.getActors().get(i));
        }
        movieActors.setText(actors.toString());
        
        //Directors
        StringBuilder directors = new StringBuilder();
        directors.append(movie.getDirectors().get(0));
        for(int i = 1; i < movie.getDirectors().size(); i++) {
        	directors.append(", " + movie.getDirectors().get(i));
        }
        movieDirectors.setText(directors.toString());
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
