package com.example.ru_foody;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ChefLoginPhone extends AppCompatActivity {

    TextInputLayout mobile_number;
    Button sendOtp;
    TextView signup;
    FirebaseAuth Fauth;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_login_phone);

        mobile_number = findViewById(R.id.mobile_no);
        sendOtp = findViewById(R.id.otp_btn);
        signup = findViewById(R.id.create);

        Fauth = FirebaseAuth.getInstance();

        sendOtp.setOnClickListener(v -> {
            number = Objects.requireNonNull(mobile_number.getEditText()).toString().trim();
            Intent b = new Intent(ChefLoginPhone.this, ChefSendOtp.class);
            startActivity(b);
            finish();
        });

        signup.setOnClickListener(v -> startActivity(new Intent(ChefLoginPhone.this, ChefRegistration.class)));

    }
}