package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button firebase,sqlite,weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase = findViewById(R.id.firebase_data_button);
        sqlite = findViewById(R.id.SQLite_data_button);
        weather = findViewById(R.id.weather_report);
        //When user clicks on firebase button
        firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FirebaseData.class);
                startActivity(i);
            }
        });
        sqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SQLiteData.class);
                startActivity(i);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WeatherReportMainScreen.class);
                startActivity(intent);
            }
        });
    }
}