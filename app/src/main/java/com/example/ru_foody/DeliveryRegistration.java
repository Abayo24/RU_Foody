package com.example.ru_foody;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DeliveryRegistration extends AppCompatActivity {

    ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_registration);

        Back = findViewById(R.id.back);

        Back.setOnClickListener(v -> onBackPressed());
    }
}