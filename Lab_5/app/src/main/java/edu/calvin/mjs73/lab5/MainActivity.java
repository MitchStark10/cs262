package edu.calvin.mjs73.lab5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //create static variable
    //on click of check box, set to true or false accordingly
    private SharedPreferences prefs;
    private boolean samplePref = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setLabel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent activity_2 = new Intent(this, MyPreferenceActivity.class);
                startActivity(activity_2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }

    @Override
    public void onResume() {
        super.onResume();
        samplePref = prefs.getBoolean("check_box_preference_1", true);
        setLabel();
    }

    public void setLabel() {
        TextView t = (TextView)findViewById(R.id.textView2);
        t.setText("Sample preference is: " + samplePref);
    }
}
