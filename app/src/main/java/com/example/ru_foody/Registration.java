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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Objects;

public class Registration extends AppCompatActivity {

    TextInputLayout cuname, cumobile, cuemail, cupassword, cucpassword;
    Button cusignup;
    ImageView Back;
    CountryCodePicker Cpp;

    ProgressBar mDialog;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String first, mobile, email, password, c_password,role="Customer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        cuname= findViewById(R.id.cu_fullName);
        cumobile = findViewById(R.id.cu_mobileNo);
        cuemail = findViewById(R.id.cu_email);
        cupassword= findViewById(R.id.cu_password);
        cucpassword = findViewById(R.id.cuc_password);
        Back = findViewById(R.id.back);
        Cpp = findViewById(R.id.Cpp);


        cusignup = findViewById(R.id.cu_signup_btn);

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Customer");
        FAuth = FirebaseAuth.getInstance();

        cusignup.setOnClickListener(v -> {
            first = Objects.requireNonNull(cuname.getEditText()).getText().toString().trim();
            mobile = Objects.requireNonNull(cumobile.getEditText()).getText().toString().trim();
            email = Objects.requireNonNull(cuemail.getEditText()).getText().toString().trim();
            password = Objects.requireNonNull(cupassword.getEditText()).getText().toString().trim();
            c_password = Objects.requireNonNull(cucpassword.getEditText()).getText().toString().trim();

            //used to show a progress dialog when a certain condition isValid is true
            if (isValid()) {
                mDialog = findViewById(R.id.progressBar);
                AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
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
                            //set user's role to Customer
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

                                FirebaseDatabase.getInstance().getReference("Customer")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(hashMap1).addOnCompleteListener(task11 -> {
                                            mDialog.dismiss();

                                            //Send an email verification link to the user
                                            Objects.requireNonNull(FAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task111 -> {
                                                if (task.isSuccessful()) {
                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Registration.this);
                                                    builder1.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                    builder1.setCancelable(false);
                                                    builder1.setPositiveButton("Ok", (dialog, which) -> {
                                                        dialog.dismiss();

                                                        String mobilenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                        Intent intent = new Intent(Registration.this, Login.class);
                                                        intent.putExtra("mobilenumber", mobilenumber);
                                                        startActivity(intent);
                                                    });
                                                    AlertDialog alert = builder1.create();
                                                    alert.show();
                                                } else {
                                                    mDialog.dismiss();
                                                    ReusableCodeForAll.ShowAlert(Registration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                                                }
                                            });
                                        });

                            });
                        }else{
                            mDialog.dismiss();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Registration.this);
                            builder1.setMessage("Email already exists. Please use a different email.");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                            AlertDialog Alert = builder1.create();
                            Alert.show();
                        }
                    } else {
                        mDialog.dismiss();
                        ReusableCodeForAll.ShowAlert(Registration.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
            }

        });

        Back.setOnClickListener(v -> onBackPressed());
    }

    String emailpattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    public boolean isValid(){
        cuname.setErrorEnabled(false);
        cuname.setError("");
        cumobile.setErrorEnabled(false);
        cumobile.setError("");
        cuemail.setErrorEnabled(false);
        cuemail.setError("");
        cupassword.setErrorEnabled(false);
        cupassword.setError("");
        cucpassword.setErrorEnabled(false);
        cucpassword.setError("");

        boolean isValid, isValid_name=false, isValid_email = false,
                isValid_password = false, isValid_confirm_password = false,
                isValid_mobile = false;

        if(TextUtils.isEmpty(first)){
            cuname.setErrorEnabled(true);
            cuname.setError("Enter your Full Name");
        }else{
            isValid_name = true;
        }

        if(TextUtils.isEmpty(mobile)){
            cumobile.setErrorEnabled(true);
            cumobile.setError("Enter your Mobile Number");
        }else{
            if(mobile.length() < 9){
                cumobile.setErrorEnabled(true);
                cumobile.setError("Invalid Mobile Number");
            }else {
                isValid_mobile = true;
            }
        }

        if(TextUtils.isEmpty(email)){
            cuemail.setErrorEnabled(true);
            cuemail.setError("Enter your Email");
        }else {
            if (email.matches(emailpattern)){
                isValid_email = true;
            }else {
                cuemail.setErrorEnabled(true);
                cuemail.setError("Enter a Valid Email Id");
            }

        }

        if(TextUtils.isEmpty(password)){
            cupassword.setErrorEnabled(true);
            cupassword.setError("Enter your Password");
        }else{
            if(password.length() < 8){
                cupassword.setErrorEnabled(true);
                cupassword.setError("Your password is Weak");
            }else {
                isValid_password = true;
            }
        }

        if(TextUtils.isEmpty(c_password)){
            cucpassword.setErrorEnabled(true);
            cucpassword.setError("Enter your Password Again");
        }else{
            if(c_password.length() < 8){
                cucpassword.setErrorEnabled(true);
                cucpassword.setError("Your passwords Don't Match");
            }else {
                isValid_confirm_password = true;
            }
        }

        isValid = isValid_name && isValid_mobile && isValid_email
                && isValid_password && isValid_confirm_password;
        return isValid;

    }
}
