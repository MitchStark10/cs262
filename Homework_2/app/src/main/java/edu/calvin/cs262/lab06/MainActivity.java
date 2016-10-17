package edu.calvin.cs262.lab06;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText idText;
    private Button fetchButton;

    private List<Player> playerList = new ArrayList<>();
    private ListView itemsListView;

    private boolean makeList = false;

    /* This formater can be used as follows to format temperatures for display.
     *     numberFormat.format(SOME_DOUBLE_VALUE)
     */
    //private NumberFormat numberFormat = NumberFormat.getInstance();

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idText = (EditText) findViewById(R.id.playerText);
        fetchButton = (Button) findViewById(R.id.fetchButton);
        itemsListView = (ListView) findViewById(R.id.playerListView);

        // See comments on this formatter above.
        //numberFormat.setMaximumFractionDigits(0);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(idText);
                new GetPlayerTask().execute(createURL(idText.getText().toString()));
            }
        });
    }

    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param id the target id
     * @return URL formatted for the example database
     */
    private URL createURL(String id) {
        try {
            String urlString;
            if (id.equals("")) {
                urlString = "http://cs262.cs.calvin.edu:8089/monopoly/players";
            } else {
                makeList = true;
                urlString = "http://cs262.cs.calvin.edu:8089/monopoly/player/" +
                        URLEncoder.encode(id, "UTF-8");
            }

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


    private class GetPlayerTask extends AsyncTask<URL, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(URL... params) {
            //must return JSON Array, however, when given a single ID
            //the response returns an object. We must manually add in the brackets then in order
            //to return the list, with the flag makeList
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
                    if (!makeList) {
                        return new JSONArray(result.toString());
                    } else {
                        return new JSONArray("[" + result.toString() + "]");
                    }

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
        protected void onPostExecute(JSONArray players) {
            if (players != null) {
                convertToJSONArrayList(players);
                MainActivity.this.updateDisplay();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateDisplay() {
        if (playerList == null) {
            Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (Player item : playerList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("display_string", item.getId() + ", " + item.getName() +  ", " + item.getEmail());
            data.add(map);
        }

        int resource = R.layout.player_item;
        String[] from = {"display_string"};
        int[] to = {R.id.display};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
        System.out.println("Should have updated screen");
    }


    /**
     * Converts the JSONArray response to a java array list
     * @param response
     */
    private void convertToJSONArrayList(JSONArray response) {
        playerList.clear();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject person = response.getJSONObject(i);
                String id = Integer.toString(person.getInt("id"));
                String email = person.getString("emailaddress");
                String name;
                try {
                    name = person.getString("name");
                } catch (JSONException e) {
                    //catch no name error
                    name = "No name listed";
                }
                playerList.add(new Player(id, name, email));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
