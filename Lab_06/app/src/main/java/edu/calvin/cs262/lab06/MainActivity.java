package edu.calvin.cs262.lab06;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Reads openweathermap's RESTful API for weather forecasts.
 * The code is based on Deitel's WeatherViewer (Chapter 17), simplified based on Murach's NewsReader (Chapter 10).
 * <p>
 * for CS 262, lab 6
 *
 * @author kvlinden
 * @version summer, 2016
 * -----------------------------------
 * Lab Answers:
 * 1. The app just continues spitting out data, even for invalid cities.
 * 2. ?
 * 3. This is an example of the JSON object I found: {"city":{"id":4994358,"name":"Grand Rapids","coord":{"lon":-85.668091,"lat":42.96336},"country":"US","population":0},"cod":"200","message":0.3328,"cnt":7,"list":[{"dt":1476291600,"temp":{"day":50.99,"min":45.1,"max":50.99,"night":45.1,"eve":50.99,"morn":50.99},"pressure":997.2,"humidity":100,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":18.48,"deg":332,"clouds":92,"rain":10.47},{"dt":1476378000,"temp":{"day":55.08,"min":33.15,"max":55.08,"night":33.15,"eve":43.9,"morn":43.52},"pressure":1008.81,"humidity":74,"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"speed":9.75,"deg":296,"clouds":0},{"dt":1476464400,"temp":{"day":56.82,"min":30.07,"max":58.21,"night":45.14,"eve":51.26,"morn":30.07},"pressure":1009.72,"humidity":69,"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"speed":12.1,"deg":195,"clouds":0},{"dt":1476550800,"temp":{"day":63.59,"min":51.84,"max":67.64,"night":67.64,"eve":65.89,"morn":51.84},"pressure":1000.96,"humidity":0,"weather":[{"id":502,"main":"Rain","description":"heavy intensity rain","icon":"10d"}],"speed":13.8,"deg":189,"clouds":97,"rain":27.95},{"dt":1476637200,"temp":{"day":69.84,"min":56.64,"max":69.84,"night":56.64,"eve":63,"morn":66.63},"pressure":993.29,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":11.65,"deg":269,"clouds":60,"rain":8.8},{"dt":1476723600,"temp":{"day":69.03,"min":53.69,"max":69.03,"night":53.69,"eve":61.48,"morn":60.15},"pressure":994.05,"humidity":0,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":8.01,"deg":253,"clouds":0},{"dt":1476810000,"temp":{"day":73.89,"min":56.97,"max":73.89,"night":70.95,"eve":73.71,"morn":56.97},"pressure":993.07,"humidity":0,"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"speed":11.74,"deg":152,"clouds":2}]}
 * 4. The system searches the JSON object for the data it needs to produce the output, and doesn't look at the rest.
 * 5. The weather class is used to store the day and weather summary.
 */
public class MainActivity extends AppCompatActivity {

    private EditText cityText;
    private Button fetchButton;

    private List<Weather> weatherList = new ArrayList<>();
    private ListView itemsListView;

    /* This formater can be used as follows to format temperatures for display.
     *     numberFormat.format(SOME_DOUBLE_VALUE)
     */
    //private NumberFormat numberFormat = NumberFormat.getInstance();

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = (EditText) findViewById(R.id.cityText);
        fetchButton = (Button) findViewById(R.id.fetchButton);
        itemsListView = (ListView) findViewById(R.id.weatherListView);

        // See comments on this formatter above.
        //numberFormat.setMaximumFractionDigits(0);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(cityText);
                new GetWeatherTask().execute(createURL(cityText.getText().toString()));
            }
        });
    }

    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param city the target city
     * @return URL formatted for openweathermap.com
     */
    private URL createURL(String city) {
        try {
            String urlString = getString(R.string.web_service_url) +
                    URLEncoder.encode(city, "UTF-8") +
                    "&units=" + getString(R.string.openweather_units) +
                    "&cnt=" + getString(R.string.openweather_count) +
                    "&APPID=" + getString(R.string.openweather_api_key);
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    /**
     * Deitel's method for programmatically dismissing the keyboard.
     *
     * @param view the TextView currently being edited
     */
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Inner class for GETing the current weather data from openweathermap.org asynchronously
     */
    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return new JSONObject(result.toString());
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather) {
            if (weather != null) {
                //Log.d(TAG, weather.toString());
                convertJSONtoArrayList(weather);
                MainActivity.this.updateDisplay();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Converts the JSON weather forecast data to an arraylist suitable for a listview adapter
     *
     * @param forecast
     */
    private void convertJSONtoArrayList(JSONObject forecast) {
        weatherList.clear(); // clear old weather data
        try {
            JSONArray list = forecast.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);
                JSONObject temperatures = day.getJSONObject("temp");
                String high_temp = Integer.toString(temperatures.getInt("max"));
                String low_temp = Integer.toString(temperatures.getInt("min"));
                JSONObject weather = day.getJSONArray("weather").getJSONObject(0);
                weatherList.add(new Weather(
                        day.getLong("dt"),
                        weather.getString("description"), high_temp, low_temp));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh the weather data on the forecast ListView through a simple adapter
     */
    private void updateDisplay() {
        if (weatherList == null) {
            Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (Weather item : weatherList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("day", item.getDay());
            map.put("description", item.getSummary());
            map.put("temps", highLowString(item.getHigh(), item.getLow()));
            data.add(map);
        }

        int resource = R.layout.weather_item;
        String[] from = {"day", "description", "temps"};
        int[] to = {R.id.dayTextView, R.id.summaryTextView, R.id.highLowtextView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }

    /**
     * create the string for the highs and lows
     * return: nothing
     */
    private String highLowString(String high, String low) {
        return "High: " + high + " - Low: " + low;
    }

}
