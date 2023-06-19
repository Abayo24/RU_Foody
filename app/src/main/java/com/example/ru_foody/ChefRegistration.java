package com.example.ru_foody;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class ChefRegistration extends AppCompatActivity {

    TextInputLayout chname, chmobile, chemail, chpassword, chcpassword;
    Button chsignup;
    ImageView Back;

    ProgressBar mDialog;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String first, mobile, email, password, c_password,role="Chef";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_registration);

        chname = findViewById(R.id.ch_full_name);
        chmobile = findViewById(R.id.ch_mobile_no);
        chemail = findViewById(R.id.ch_email);
        chpassword= findViewById(R.id.ch_password);
        chcpassword = findViewById(R.id.chc_password);
        Back = findViewById(R.id.back);


        chsignup = findViewById(R.id.ch_signup_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chef");
        FAuth = FirebaseAuth.getInstance();

        //capturing the values entered by the user, when the button (chsignup)
        // is clicked. These values are then stored in corresponding variables

        chsignup.setOnClickListener(v -> {
            first = Objects.requireNonNull(chname.getEditText()).getText().toString().trim();
            mobile = Objects.requireNonNull(chmobile.getEditText()).getText().toString().trim();
            email = Objects.requireNonNull(chemail.getEditText()).getText().toString().trim();
            password = Objects.requireNonNull(chpassword.getEditText()).getText().toString().trim();
            c_password = Objects.requireNonNull(chcpassword.getEditText()).getText().toString().trim();

            //used to show a progress dialog when a certain condition isValid is true
            if (isValid()) {
                mDialog = findViewById(R.id.progressBar);
                AlertDialog.Builder builder = new AlertDialog.Builder(ChefRegistration.this);
                builder.setCancelable(false);

// Set a custom layout for the dialog
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.progress_dialog, null);
                builder.setView(dialogView);

                AlertDialog mDialog = builder.create();
                mDialog.show();

                //used to handle the registration process for a chef
                FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Check if the user is a new user (not signing in with an existing account)
                        if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                            String user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user_id);

                            user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user_id);
                            //set user's role to Chef
                            final HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("Role", role);

                            databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {

                                //Save chef's details in database
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Full Name", first);
                                hashMap1.put("Mobile Number", mobile);
                                hashMap1.put("Email", email);
                                hashMap1.put("Password", password);
                                hashMap1.put("Confirm Password", c_password);

                                FirebaseDatabase.getInstance().getReference("Chef")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(hashMap1).addOnCompleteListener(task11 -> {
                                            mDialog.dismiss();

                                            //Send an email verification link to the user
                                            Objects.requireNonNull(FAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task111 -> {
                                                if (task.isSuccessful()) {
                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ChefRegistration.this);
                                                    builder1.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                    builder1.setCancelable(false);
                                                    builder1.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                                                    AlertDialog Alert = builder1.create();
                                                    Alert.show();
                                                } else {
                                                    mDialog.dismiss();
                                                    ReusableCodeForAll.ShowAlert(ChefRegistration.this, "Error", Objects.requireNonNull(task111.getException()).getMessage());
                                                }
                                            });
                                        });

                            });
                    }else{
                            mDialog.dismiss();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ChefRegistration.this);
                            builder1.setMessage("Email already exists. Please use a different email.");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                            AlertDialog Alert = builder1.create();
                            Alert.show();
                        }
                    } else {
                        mDialog.dismiss();
                        ReusableCodeForAll.ShowAlert(ChefRegistration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
            }

        });

        Back.setOnClickListener(v -> onBackPressed());

    }

    String emailpattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    public boolean isValid(){
        chname.setErrorEnabled(false);
        chname.setError("");
        chmobile.setErrorEnabled(false);
        chmobile.setError("");
        chemail.setErrorEnabled(false);
        chemail.setError("");
        chpassword.setErrorEnabled(false);
        chpassword.setError("");
        chcpassword.setErrorEnabled(false);
        chcpassword.setError("");

        boolean isValid, isValid_name=false, isValid_email = false,
                isValid_password = false, isValid_confirm_password = false,
                isValid_mobile = false;

        if(TextUtils.isEmpty(first)){
            chname.setErrorEnabled(true);
            chname.setError("Enter your Full Name");
        }else{
            isValid_name = true;
        }

        if(TextUtils.isEmpty(mobile)){
            chmobile.setErrorEnabled(true);
            chmobile.setError("Enter your Mobile Number");
        }else{
            if(mobile.length() < 10){
                chmobile.setErrorEnabled(true);
                chmobile.setError("Invalid Mobile Number");
            }else {
                isValid_mobile = true;
            }
        }

        if(TextUtils.isEmpty(email)){
            chemail.setErrorEnabled(true);
            chemail.setError("Enter your Email");
        }else {
            if (email.matches(emailpattern)){
            }else {
                chemail.setErrorEnabled(true);
                chemail.setError("Enter a Valid Email Id");
            }
            isValid_email = true;
        }

        if(TextUtils.isEmpty(password)){
            chpassword.setErrorEnabled(true);
            chpassword.setError("Enter your Password");
        }else{
            if(password.length() < 8){
                chpassword.setErrorEnabled(true);
                chpassword.setError("Your password is Weak");
            }else {
                isValid_password = true;
            }
        }

        if(TextUtils.isEmpty(c_password)){
            chcpassword.setErrorEnabled(true);
            chcpassword.setError("Enter your Password Again");
        }else{
            if(c_password.length() < 8){
                chcpassword.setErrorEnabled(true);
                chcpassword.setError("Your passwords Don't Match");
            }else {
                isValid_confirm_password = true;
            }
        }

        isValid = isValid_name && isValid_mobile && isValid_email
                && isValid_password && isValid_confirm_password;
        return isValid;

    }
}