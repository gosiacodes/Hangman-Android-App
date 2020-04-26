package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HangingActivity extends AppCompatActivity {

    ImageView imgHangman;
    String value1;
    Integer value3;
    int imgResHang = R.drawable.picturehang0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hanging);

        imgHangman = findViewById(R.id.imgHangman);

        Bundle extraStuff = getIntent().getExtras();
        value3 = extraStuff.getInt("Value3");
        value1 = extraStuff.getString("Value1");

        if (value3 == 6){
            imgResHang = R.drawable.picturehang1;
            imgHangman.setImageResource(imgResHang);
        }
        else if (value3 == 5){
            imgResHang = R.drawable.picturehang2;
            imgHangman.setImageResource(imgResHang);
        }
        else if (value3 == 4){
            imgResHang = R.drawable.picturehang3;
            imgHangman.setImageResource(imgResHang);
        }
        else if (value3 == 3) {
            imgResHang = R.drawable.picturehang4;
            imgHangman.setImageResource(imgResHang);
        }
        else if (value3 == 2) {
            imgResHang = R.drawable.picturehang5;
            imgHangman.setImageResource(imgResHang);
        }
        else if (value3 == 1) {
            imgResHang = R.drawable.picturehang6;
            imgHangman.setImageResource(imgResHang);
        }
        else if (value3 == 0) {
            imgResHang = R.drawable.picturehang7;
            imgHangman.setImageResource(imgResHang);
        }

    }

    public void callActivityMain(View view) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("Value2", value1);
        setResult(2,i);
        finish();
    }
}
