package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CheckTriesLeftActivity extends AppCompatActivity {

    TextView txtTestActivityResult;
    TextView getTxtTestActivityResult2;
    String value1;
    Integer value3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_tries_left);
        txtTestActivityResult = findViewById(R.id.txtTestActivityResult);
        getTxtTestActivityResult2 = findViewById(R.id.txtActivityResult2);

        Bundle extraStuff = getIntent().getExtras();
        value1 = extraStuff.getString("Value1");
        value3 = extraStuff.getInt("Value3");

        txtTestActivityResult.setText(value1);
        getTxtTestActivityResult2.setText("You have " + value3 + " tries left.");
    }

    public void callMainActivity(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("Value2", value1);
        setResult(2,i);
        finish();
    }
}
