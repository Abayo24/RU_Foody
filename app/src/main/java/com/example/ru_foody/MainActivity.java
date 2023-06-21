package com.example.ru_foody;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button button;
    FirebaseAuth Fauth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         button = findViewById(R.id.orderNbutton);
//AutoSignin
        new Handler().postDelayed(() -> {

            Fauth = FirebaseAuth.getInstance();
            if(Fauth.getCurrentUser()!=null){
                if(Fauth.getCurrentUser().isEmailVerified()){
                    Fauth=FirebaseAuth.getInstance();

                    databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()+"/Role");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String role = snapshot.getValue(String.class);
                            if(Objects.equals(role, "Chef")){
                                startActivity(new Intent(MainActivity.this,ChefFoodPanel_BottomNavigation.class));
                                finish();

                            }
                            if(Objects.equals(role, "Customer")){
                                startActivity(new Intent(MainActivity.this,CustomerFoodPanel_BottomNavigation.class));
                                finish();

                            }
                            if(Objects.equals(role, "DeliveryPerson")){
                                startActivity(new Intent(MainActivity.this,DeliveryFoodPanel_BottomNavigation.class));
                                finish();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Check Whether You Have Verified Your Detail , Otherwise Please Verify");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this,MainMenu.class);
                        startActivity(intent);
                        finish();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Fauth.signOut();
                }
            }else {

                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
                finish();
            }

        },3000);

        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,MainMenu.class)));
    }
}