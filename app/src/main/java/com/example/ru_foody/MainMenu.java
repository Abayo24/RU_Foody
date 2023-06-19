package com.example.ru_foody;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    Button signinemail, signinphone, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        signinemail = findViewById(R.id.signwithemail);
        signinphone = findViewById(R.id.signwithphone);
        signup = findViewById(R.id.signup);

        signinemail.setOnClickListener(v -> {
            Intent signemail = new Intent(MainMenu.this,ChooseOne.class);
            signemail.putExtra("Home", "Email");
            startActivity(signemail);
        });

        signinphone.setOnClickListener(v -> {
            Intent signphone = new Intent(MainMenu.this,ChooseOne.class );
            signphone.putExtra("Home", "Phone");
            startActivity(signphone);
        });

        signup.setOnClickListener(v -> {
            Intent signup = new Intent(MainMenu.this,ChooseOne.class);
            signup.putExtra("Home", "Signup");
            startActivity(signup);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}