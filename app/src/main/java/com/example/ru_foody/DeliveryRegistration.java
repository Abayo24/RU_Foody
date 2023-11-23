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

import androidx.appcompat.app.AppCompatActivity;

import com.example.ru_foody.ReusableCode.ReusableCodeForAll;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Objects;

public class DeliveryRegistration extends AppCompatActivity {

    TextInputLayout chname, chmobile, chemail, chpassword, chcpassword;
    Button chsignup;
    ImageView Back;

    CountryCodePicker Cpp;

    ProgressBar mDialog;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String first, mobile, email, password, c_password,role="DeliveryPerson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_registration);

        chname = findViewById(R.id.d_full_name);
        chmobile = findViewById(R.id.d_mobile_no);
        chemail = findViewById(R.id.d_email);
        chpassword= findViewById(R.id.d_password);
        chcpassword = findViewById(R.id.dc_password);
        Back = findViewById(R.id.back);
        Cpp = findViewById(R.id.Cpp);
        chsignup = findViewById(R.id.d_signup_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chef");
        FAuth = FirebaseAuth.getInstance();

        chsignup.setOnClickListener(v -> {
            first = Objects.requireNonNull(chname.getEditText()).getText().toString().trim();
            mobile = Objects.requireNonNull(chmobile.getEditText()).getText().toString().trim();
            email = Objects.requireNonNull(chemail.getEditText()).getText().toString().trim();
            password = Objects.requireNonNull(chpassword.getEditText()).getText().toString().trim();
            c_password = Objects.requireNonNull(chcpassword.getEditText()).getText().toString().trim();

            //used to show a progress dialog when a certain condition isValid is true
            if (isValid()) {
                mDialog = findViewById(R.id.progressBar);
                AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryRegistration.this);
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

                                FirebaseDatabase.getInstance().getReference("DeliveryPerson")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(hashMap1).addOnCompleteListener(task11 -> {
                                            mDialog.dismiss();

                                            //Send an email verification link to the user
                                            Objects.requireNonNull(FAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task111 -> {
                                                if (task.isSuccessful()) {
                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DeliveryRegistration.this);
                                                    builder1.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                    builder1.setCancelable(false);
                                                    builder1.setPositiveButton("Ok", (dialog, which) -> {
                                                        dialog.dismiss();

                                                        String mobilenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                        Intent intent = new Intent(DeliveryRegistration.this, DeliveryVerifyPhone.class);
                                                        intent.putExtra("mobilenumber", mobilenumber);
                                                        startActivity(intent);
                                                    });
                                                    AlertDialog alert = builder1.create();
                                                    alert.show();
                                                } else {
                                                    mDialog.dismiss();
                                                    ReusableCodeForAll.ShowAlert(DeliveryRegistration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                                                }

                                            });
                                        });

                            });
                        }else{
                            mDialog.dismiss();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(DeliveryRegistration.this);
                            builder1.setMessage("Email already exists. Please use a different email.");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                            AlertDialog Alert = builder1.create();
                            Alert.show();
                        }
                    } else {
                        mDialog.dismiss();
                        ReusableCodeForAll.ShowAlert(DeliveryRegistration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
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
            if(mobile.length() < 9){
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
                isValid_email = true;
            }else {
                chemail.setErrorEnabled(true);
                chemail.setError("Enter a Valid Email Id");
            }

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