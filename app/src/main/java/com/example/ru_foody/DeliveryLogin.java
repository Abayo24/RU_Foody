package com.example.ru_foody;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DeliveryLogin extends AppCompatActivity {

    ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_login);

        Back = findViewById(R.id.back);

        Back.setOnClickListener(v -> onBackPressed());
    }
}