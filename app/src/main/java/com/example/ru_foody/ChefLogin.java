package com.example.ru_foody;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ChefLogin extends AppCompatActivity {

    TextInputLayout email, password;
    Button login;
    ImageView Back;
    ProgressBar mDialog;
    TextView forgotPassword, signup;
    FirebaseAuth Fauth;
    String email_id, password_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_login);

        Back = findViewById(R.id.back);

        try{
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);
            login = findViewById(R.id.login_btn);
            signup = findViewById(R.id.create);
            forgotPassword = findViewById(R.id.f_password);

            Fauth = FirebaseAuth.getInstance();

            login.setOnClickListener(v -> {
                email_id = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
                password_id = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

                if (isValid()){
                    mDialog = findViewById(R.id.progressBar);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChefLogin.this);
                    builder.setCancelable(false);

// Set a custom layout for the dialog
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.progress_dialog, null);
                    builder.setView(dialogView);

                    AlertDialog mDialog = builder.create();
                    mDialog.show();

                    Fauth.signInWithEmailAndPassword(email_id, password_id).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            mDialog.dismiss();

                            if (Objects.requireNonNull(Fauth.getCurrentUser()).isEmailVerified()) {
                                mDialog.dismiss();
                                Toast.makeText(ChefLogin.this, "Congratulations You Have Successfully Logged in", Toast.LENGTH_SHORT).show();
                                Intent Z = new Intent(ChefLogin.this, ChefFoodPanel_BottomNavigation.class);
                                startActivity(Z);
                                finish();

                            } else {
                                ReusableCodeForAll.ShowAlert(ChefLogin.this, "Verification Failed", "You Have not Verified Your Email");
                            }
                        } else {
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(ChefLogin.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
                }
            });

            signup.setOnClickListener(v -> {
                startActivity(new Intent(ChefLogin.this, ChefRegistration.class));
                finish();
            });

            forgotPassword.setOnClickListener(v -> {
                startActivity(new Intent(ChefLogin.this, ChefForgotPassword.class));
                finish();
            });
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Back.setOnClickListener(v -> onBackPressed());
    }
    String emailpattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

    public boolean isValid(){
        email.setErrorEnabled(false);
        email.setError("");
        password.setErrorEnabled(false);
        password.setError("");

        boolean isValid, isValid_email = false, isValid_password = false;
        if (TextUtils.isEmpty(email_id)){
            email.setErrorEnabled(true);
            email.setError("Email is Required");
        }else{
            if(email_id.matches(emailpattern)){
                isValid_email = true;
            }else{
                email.setErrorEnabled(true);
                email.setError("Invalid Email Address");
            }
        }

        if (TextUtils.isEmpty(password_id)){
            password.setErrorEnabled(true);
            password.setError("Password is Required");
        }else{
            isValid_password = true;
        }

        isValid = isValid_email && isValid_password;
        return isValid;
    }

}