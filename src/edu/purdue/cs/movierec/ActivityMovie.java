package edu.purdue.cs.movierec;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityMovie extends Activity {
	
	MovieApp appState;	
	Movie movie;
	
	private TextView movieTitle;
	private TextView movieSubTitle;
	private ImageView fanArt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie);
        
        appState = (MovieApp)getApplicationContext();
        movie =  appState.getMovie();
        
        movieTitle = (TextView)findViewById(R.id.movie_title);
        movieSubTitle = (TextView)findViewById(R.id.movie_subtitle);
        fanArt = (ImageView)findViewById(R.id.fanart);
        
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
        
        fanArt.setImageResource(R.drawable.weddingcrashers);
        
        
        
    }
}
