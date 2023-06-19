package com.example.ru_foody;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
        Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         button = findViewById(R.id.orderNbutton);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,MainMenu.class)));
    }
}