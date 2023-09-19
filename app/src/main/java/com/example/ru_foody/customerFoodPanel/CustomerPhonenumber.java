package com.example.ru_foody.customerFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ru_foody.R;
import com.hbb20.CountryCodePicker;

public class CustomerPhonenumber extends AppCompatActivity {

    EditText num;
    CountryCodePicker cpp;
    Button SendOTP;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_phonenumber);

        num = findViewById(R.id.phonenumber);
        cpp = findViewById(R.id.Countrycode);
        SendOTP = findViewById(R.id.sendotp);

        SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number=num.getText().toString().trim();
                String phonenumber= cpp.getSelectedCountryCodeWithPlus() + number;
                Intent intent=new Intent(CustomerPhonenumber.this,CustomerPhoneSendOTP.class);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
                finish();
            }
        });

    }
}