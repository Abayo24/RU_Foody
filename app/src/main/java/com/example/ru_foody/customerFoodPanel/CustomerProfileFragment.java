package com.example.ru_foody.customerFoodPanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.ru_foody.Customer;
import com.example.ru_foody.MainMenu;
import com.example.ru_foody.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CustomerProfileFragment extends Fragment {

    EditText fName, mobile;
    TextView mobileno, Email;
    Button Update;
    LinearLayout password, LogOut;
    DatabaseReference databaseReference, data;
    FirebaseDatabase firebaseDatabase;
    String email, passwordd, confirmpass, mobilenoo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_profile, null);
        getActivity().setTitle("Profile");

        fName = (EditText) v.findViewById(R.id.fnamee);
        mobile = (EditText) v.findViewById(R.id.address);
        Email = (TextView) v.findViewById(R.id.emailID);
        mobileno = (TextView) v.findViewById(R.id.mobilenumber);
        Update = (Button) v.findViewById(R.id.update);
        password = (LinearLayout) v.findViewById(R.id.passwordlayout);
        LogOut = (LinearLayout) v.findViewById(R.id.logout_layout);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Customer customer = dataSnapshot.getValue(Customer.class);

                fName.setText(customer.getFullName());
                mobileno.setText(customer.getMobileNo());
                Email.setText(customer.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateinformation();
        return v;
    }

    private void updateinformation() {


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                data = FirebaseDatabase.getInstance().getReference("Customer").child(useridd);
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Customer customer = dataSnapshot.getValue(Customer.class);

                        confirmpass = customer.getConfirmPassword();
                        email = customer.getEmail();
                        passwordd = customer.getPassword();
                        mobilenoo = customer.getMobileNo();

                        String Fname = fName.getText().toString().trim();
                        String Mobile = mobile.getText().toString().trim();

                        HashMap<String, String> hashMappp = new HashMap<>();
                        hashMappp.put("ConfirmPassword", confirmpass);
                        hashMappp.put("EmailID", email);
                        hashMappp.put("FirstName", Fname);
                        hashMappp.put("Mobileno", String.valueOf(mobilenoo));
                        hashMappp.put("Password", passwordd);
                        hashMappp.put("Number", Mobile);
                        firebaseDatabase.getInstance().getReference("Customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMappp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CustomerPassword.class);
                startActivity(intent);
            }
        });

        mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), CustomerPhonenumber.class);
                startActivity(i);
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Logout ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

    }

}
