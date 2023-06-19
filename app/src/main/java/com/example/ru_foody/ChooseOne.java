package com.example.ru_foody;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseOne extends AppCompatActivity {

    Button chef, customer, delivery;
    Intent intent;
    String type;
    ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);

        intent = getIntent();
        type = intent.getStringExtra("Home").trim();

        chef = findViewById(R.id.chef_btn);
        delivery = findViewById(R.id.delivery_btn);
        customer = findViewById(R.id.customer_btn);
        Back = findViewById(R.id.back);

        chef.setOnClickListener(v -> {
            if (type.equals("Email")){
                Intent l = new Intent(ChooseOne.this, ChefLogin.class);
                startActivity(l);
            }
            if (type.equals("Phone")){
                Intent lo = new Intent(ChooseOne.this, ChefLoginPhone.class);
                startActivity(lo);
            }
            if (type.equals("Signup")){
                Intent Register = new Intent(ChooseOne.this, ChefRegistration.class);
                startActivity(Register);
            }
        });

        customer.setOnClickListener(v -> {
            if (type.equals("Email")){
                Intent L = new Intent(ChooseOne.this, Login.class);
                startActivity(L);
                finish();
            }
            if (type.equals("Phone")){
                Intent P = new Intent(ChooseOne.this, LoginPhone.class);
                startActivity(P);
                finish();
            }
            if (type.equals("Signup")){
                Intent S = new Intent(ChooseOne.this, Registration.class);
                startActivity(S);
                finish();
            }
        });

        delivery.setOnClickListener(v -> {
            if (type.equals("Email")){
                Intent E = new Intent(ChooseOne.this, DeliveryLogin.class);
                startActivity(E);
                finish();
            }
            if (type.equals("Phone")){
                Intent D = new Intent(ChooseOne.this, DeliveryLoginPhone.class);
                startActivity(D);
                finish();
            }
            if (type.equals("Signup")){
                Intent R = new Intent(ChooseOne.this, DeliveryRegistration.class);
                startActivity(R);
                finish();
            }
        });

        Back.setOnClickListener(v -> onBackPressed());

    }
}