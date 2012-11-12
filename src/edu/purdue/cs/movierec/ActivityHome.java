package edu.purdue.cs.movierec;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityHome extends Activity {
	
	private EditText movieField;
	private Button searchButton;

	private MovieClient mc = null;   
	
	static public final int SERVERPORT = 25201;
	
	MovieApp appState = null;
	
	public void showToast(CharSequence text) {
		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
		toast.show();   
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        movieField = (EditText)findViewById(R.id.Movie);
        searchButton = (Button)findViewById(R.id.Search);       
        
        appState = (MovieApp)getApplicationContext();
        
        searchButton.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			//Hide Keyboard
        			InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        	    	mgr.hideSoftInputFromWindow(movieField.getWindowToken(), 0);
        	    	
        			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        			String serverPref = sharedPref.getString("pref_server", "");
        			//System.out.println("Home: Server Address = " + serverPref);
        			final String movieTitle = movieField.getText().toString(); 
        			if(serverPref.length() > 0) {
        					if(movieTitle.length() > 0) {
        						search(v, serverPref, movieTitle); 
        					} else {
        						CharSequence text = "Please Enter A Movie Title";
        						showToast(text);
        					}
        			} else {
        				CharSequence text = "No Server Address Specified. Check Settings.";
       					showToast(text);
        			}
        		}
        });
    }
    
    public void search(View v, final String server, final String movieTitle) {    	   	
    	final View clickView = v;
		
		final ProgressDialog connectDialog = new ProgressDialog(ActivityHome.this);
    	connectDialog.setMessage("Connecting...");
    	connectDialog.setCancelable(true);
    	connectDialog.show();
    	
    	final Handler mHandler = new Handler();
    	final Runnable mConnectionFailed = new Runnable() {
    		public void run() {
    			CharSequence message = "Connection failed";
    			showToast(message);
    		}
    	};
    	
    	final Runnable mSearching = new Runnable() {
    		public void run() {
    			connectDialog.setMessage("Searching...");
    		}
    	};
    	
    	final Handler searchHandler = new Handler() {
    		public void handleMessage(Message msg) {
    			connectDialog.dismiss();
    			
    			if(msg.what == 20 && msg.obj.equals("C_SEARCH")) {
    				//Start the question activity
    				Intent questionIntent = new Intent(clickView.getContext(), ActivityQuestion.class);
    				startActivity(questionIntent);
    			} else {
    				CharSequence text = "Movie Not Found";
    				showToast(text);
    			}
    		}
    	};
    	
    	new Thread(new Runnable() {
    		public void run() {    			
    			try {
    				mc = new MovieClient(server, movieTitle, searchHandler);
    				appState.setMovieClient(mc);
    				mHandler.post(mSearching);
    				mc.search();
    			} catch(Exception e) {
    				connectDialog.dismiss();
    				mHandler.post(mConnectionFailed);
    				e.printStackTrace();
    			}
    		}
    	}).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
	    	case R.id.menu_settings:
	    		startActivity(new Intent(ActivityHome.this, ActivitySettings.class));
	    		return true;
	    	default:
	    		return super.onOptionsItemSelected(item);
    	}
    }
    
    
}
