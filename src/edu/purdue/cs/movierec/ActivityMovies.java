package edu.purdue.cs.movierec;

import java.util.List;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class ActivityMovies extends Activity {
	private MovieClient mc;
	
	MovieApp appState;
	ActionBar actionBar;
	
	List<Movie> movies;
	private ListView movieListView;
	private GridView movieGridView;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			landscape();
		} else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			portrait();
		}
	}
	
	public void portrait() {
		MovieAdapter adapter = new MovieAdapter(this,R.layout.movielist_item_row,movies);
		setContentView(R.layout.movies);
		movieListView = (ListView)findViewById(R.id.movieList);
        View header = (View)getLayoutInflater().inflate(R.layout.movielist_header_row, null);
        movieListView.addHeaderView(header);
        movieListView.setAdapter(adapter);       
        
        actionBar.setTitle(R.string.app_name);
        
        movieListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Movie m = (Movie) movieListView.getAdapter().getItem(position);
                appState.setMovie(m);
                Intent singleMovie = new Intent(getApplicationContext(), ActivityMovie.class);
                startActivity(singleMovie);
            }
        });
	}
	
	public void landscape() {
		MovieAdapter adapter = new MovieAdapter(this,R.layout.movielist_item_row,movies);
		setContentView(R.layout.movies_landscape);
		movieGridView = (GridView)findViewById(R.id.movieListLandscape);
    	movieGridView.setAdapter(adapter);
    	
        actionBar.setTitle(R.string.rec);
    	
    	movieGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Movie m = (Movie) movieGridView.getAdapter().getItem(position);
                appState.setMovie(m);
                Intent singleMovie = new Intent(getApplicationContext(), ActivityMovie.class);
                startActivity(singleMovie);
            }
        });
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WindowManager mWindowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();
        
        appState = (MovieApp)getApplicationContext();
        movies = appState.getMovieList();
        mc = appState.getMovieClient();  
        
        actionBar = this.getActionBar();
        
        if(mDisplay.getRotation() > 0) {
        	landscape();
        } else {
        	portrait();
        }              
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies, menu);
        return true;
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	mc.disconnect();
	    	appState.clearMemory();
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
