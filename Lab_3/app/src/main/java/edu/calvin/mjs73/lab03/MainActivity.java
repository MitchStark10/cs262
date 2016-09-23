package edu.calvin.mjs73.lab03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the Editor Action listener for the password input
        ((EditText)findViewById(R.id.passwordInput)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN ||
                                event.getAction() == KeyEvent.KEYCODE_ENTER) {
                            if (((EditText)findViewById(R.id.passwordInput)).getText().toString().equals("pass")) {
                                //display image
                                ImageView iv = (ImageView)findViewById(R.id.imageView);
                                iv.setVisibility(View.VISIBLE);
                            }
                        }
                        return false;
                    }
                }
        );
    }




}
