package edu.calvin.mjs73.homework1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calculate(View v) {
        Spinner s = (Spinner)findViewById(R.id.spinner);
        int value_one = 0;
        int value_two = 0;
        try {
            value_one = Integer.parseInt(((TextView) findViewById(R.id.value_one_input)).getText().toString());
            value_two = Integer.parseInt(((TextView) findViewById(R.id.value_two_input)).getText().toString());
        } catch (Exception e) {
            ((TextView)findViewById(R.id.answer_label)).setText("Must be integers");
            return;
        }
        int pos = s.getSelectedItemPosition();
        /*
        0: +
        1: -
        2: *
        3: /
         */

        int final_value;

        switch(pos) {
            case 0:
                final_value = value_one + value_two;
                break;
            case 1:
                final_value = value_one - value_two;
                break;
            case 2:
                final_value = value_one * value_two;
                break;
            default:
                if (value_two == 0) {
                    ((TextView)findViewById(R.id.answer_label)).setText("Cannot divide by zero");
                    return;
                }
                final_value = value_one / value_two;
                break;
        }

        TextView display = (TextView)findViewById(R.id.answer_label);
        display.setText(Integer.toString(final_value));
    }
}
