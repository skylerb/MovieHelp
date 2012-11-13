package edu.purdue.cs.movierec;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ActivityQuestion extends Activity {
	private MovieClient mc;
	private Handler handler;
	private int questionNum = 1;
	
	private TextView questionTitleText;
	private TextView questionText;
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	private RadioButton radioYes;
	
	private List<Movie> movies = new ArrayList<Movie>();
	
	MovieApp appState;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.question);
	        
	        appState = (MovieApp)getApplicationContext();
	        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
	        radioYes = (RadioButton)findViewById(R.id.radioYes);
	        
	        createHandler();
	        
	        mc = appState.getMovieClient();
	        mc.setReadHandler(handler);
	        mc.ready();
	        
	        questionTitleText = (TextView) findViewById(R.id.textQuestionTitle);
	        questionText = (TextView) findViewById(R.id.textQuestion);
	        
	        final Button nextButton = (Button) findViewById(R.id.buttonNext);
	        nextButton.setClickable(true);
	        
	        nextButton.setOnClickListener(new OnClickListener() {
        		public void onClick(View v) {
        			//Disable button to prevent multiple clicks
        			int value = 0;
        			int selectedId = radioGroup.getCheckedRadioButtonId();
        			radioButton = (RadioButton)findViewById(selectedId);
        			if(radioButton.getText().equals("Yes")) {
        				value = 1;
        			} else if (radioButton.getText().equals("No")) {
        				value = -1;
        			}
        			//Get value of radio buttons
        			
        			mc.answerQuestion(value);
        		}
        });
	 }
	 
	 public void createHandler() {
		 handler = new Handler() {
			 public void handleMessage(Message msg) {
				 if(msg.what == 21) { //S_QUESTION_SEND
					 String questionTitle = "QUESTION #" + questionNum;
					 questionTitleText.setText(questionTitle);
					 String question = (String)msg.obj;
					 questionText.setText(question);
					 questionNum++;
					 radioYes.setChecked(true);
				 } else if(msg.what == 22) { //S_MOVIE_SEND
					 //Read in all movies into memory
					 Movie m = (Movie)msg.obj;
					 movies.add(m);
					 mc.movieSaved();
				 } else if(msg.what == 23) { //S_ALL_MOVIES_SENT
					 appState.setMovieList(movies);
					 
					 Intent listIntent = new Intent(getApplicationContext(), ActivityMovies.class);
	    			startActivity(listIntent);
	    			finish();
				 }
			 }
		 };
	 }
}
