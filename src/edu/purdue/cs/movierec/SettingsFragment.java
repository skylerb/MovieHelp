package edu.purdue.cs.movierec;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	private EditTextPreference mEditTextPreference;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);		
		
		mEditTextPreference = (EditTextPreference)getPreferenceScreen().findPreference("pref_server");		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		
		SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
		String serverVal = sharedPref.getString("pref_server", "");
		System.out.println("Current Server IP: " + serverVal);
		if(serverVal.length() > 0) {
			mEditTextPreference.setSummary(serverVal);
		} else {
			mEditTextPreference.setSummary("Enter Server IP Address");
		}
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if(key.equals("pref_server")) {
			String serverVal = sharedPreferences.getString(key,"");
			System.out.println("Current Server IP: " + serverVal);
			mEditTextPreference.setSummary(sharedPreferences.getString(key, ""));
		}
	}
}
