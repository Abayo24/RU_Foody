package com.example.ru_foody;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class DeliveryLoginPhone extends AppCompatActivity {

    TextInputLayout mobile_number;
    Button sendOtp;
    TextView signup;
    ImageView Back;
    FirebaseAuth Fauth;
    String number;

    CountryCodePicker Cpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_login_phone);

        mobile_number = findViewById(R.id.d_mobile_no);
        sendOtp = findViewById(R.id.otp_btn);
        signup = findViewById(R.id.d_create);
        Back = findViewById(R.id.back);
        Cpp = findViewById(R.id.Cpp);

        Fauth = FirebaseAuth.getInstance();

        sendOtp.setOnClickListener(v -> {
            number = Objects.requireNonNull(mobile_number.getEditText()).getText().toString().trim();
            String mobilenumber = Cpp.getSelectedCountryCodeWithPlus() + number;
            Intent b = new Intent(DeliveryLoginPhone.this, DeliverySendOtp.class);

            b.putExtra("mobilenumber", mobilenumber);
            startActivity(b);
            finish();
        });

        signup.setOnClickListener(v -> startActivity(new Intent(DeliveryLoginPhone.this, ChefRegistration.class)));

        Back.setOnClickListener(v -> onBackPressed());
    }
}