package com.example.ru_foody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ChefVerifyPhone extends AppCompatActivity {

    String verification_id;
    FirebaseAuth FAuth;
    TextInputLayout enter_code;
    TextView resend, txt;
    Button verify;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_verify_phone);

        mobile = getIntent().getStringExtra("mobilenumber").trim();

        enter_code = findViewById(R.id.code);
        verify = findViewById(R.id.verify);
        resend = findViewById(R.id.resend);
        txt = findViewById(R.id.txt);
        FAuth = FirebaseAuth.getInstance();

        resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        sendVerificationCode(mobile);

        verify.setOnClickListener(v -> {

            String code = Objects.requireNonNull(enter_code.getEditText()).getText().toString().trim();
            resend.setVisibility(View.INVISIBLE);

            if (code.length() == 0 || code.length() < 6){
                enter_code.setError("Enter The Correct Code");
                enter_code.requestFocus();
                return;
            }
            verifyCode(code);
        });

        new CountDownTimer(60000,1000){

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                txt.setVisibility(View.VISIBLE);
                txt.setText("Resend Code within "+millisUntilFinished/1000+" seconds");
            }

            @Override
            public void onFinish() {

                resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);

            }
        }.start();

        resend.setOnClickListener(v -> {
            resend.setVisibility(View.INVISIBLE);
            resendotp(mobile);

            new CountDownTimer(60000,1000){
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    txt.setVisibility(View.VISIBLE);
                    txt.setText("Resend Code within "+millisUntilFinished/1000+" seconds");

                }

                @Override
                public void onFinish() {

                    resend.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.INVISIBLE);

                }
            }.start();
        });

    }

    private void resendotp(String mobile_num) {

        sendVerificationCode(mobile_num);
    }


    //PhoneAuthProvider Function

    private void sendVerificationCode(String number) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                TextInputEditText editText = (TextInputEditText) enter_code.getEditText();
                if (editText != null) {
                    editText.setText(code);
                }
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ChefVerifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verification_id = s;
        }
    };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, code);
        linkCredential(credential);
    }

    private void linkCredential(PhoneAuthCredential credential) {

        Objects.requireNonNull(FAuth.getCurrentUser()).linkWithCredential(credential)
                .addOnCompleteListener(ChefVerifyPhone.this, task -> {

                    if(task.isSuccessful()){

                        Intent intent = new Intent(ChefVerifyPhone.this, MainMenu.class);
                        startActivity(intent);
                        finish();
                    }else{
                        ReusableCodeForAll.ShowAlert(ChefVerifyPhone.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                    }

                });
    }

}