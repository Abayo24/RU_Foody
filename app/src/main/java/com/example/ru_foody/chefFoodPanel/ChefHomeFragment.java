package com.example.ru_foody.chefFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ru_foody.MainMenu;
import com.example.ru_foody.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChefHomeFragment extends Fragment {

    RecyclerView recyclerView;
    private List<UpdateDishModel> updateDishModelList;
    private ChefHomeAdapter adapter;
    DatabaseReference dataRef;
    private String fName, mobileno, emailid, RandomUID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chef_home, container, false);
        requireActivity().setTitle("Home");
        setHasOptionsMenu(true);

        //Initializes the RecyclerView to display a list of dishes.
        recyclerView = v.findViewById(R.id.Recycle_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();

        String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        dataRef = FirebaseDatabase.getInstance().getReference("Chef").child(userid);

        //Reads data from dataRef
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // triggered when the data at the specified DatabaseReference changes
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String mobileNumber = Objects.requireNonNull(snapshot.child("Mobile Number").getValue()).toString();
                    String email = Objects.requireNonNull(snapshot.child("Email").getValue()).toString();
                    String fullName = Objects.requireNonNull(snapshot.child("Full Name").getValue()).toString();
                    Log.d("DebugFoody", "user data : " + "mobile number : " + mobileNumber + " email : " + email + " Full name : " + fullName);
                    chefDishes();
                } else {
                    Log.d("DebugFoody", "DataSnapshot does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DebugFoody", "error : " + error);
            }
        });

        return v;

    }

    private void chefDishes() {

        String useridd = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails")
                .child(useridd);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateDishModelList.clear();//// Clear the existing list of dishes to avoid duplicates.
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    UpdateDishModel updateDishModel = snapshot1.getValue(UpdateDishModel.class);
                    Log.d("DebugFoody", "Fetched UpdateDishModel: " + updateDishModel);
                    updateDishModelList.add(updateDishModel); // // Add the fetched UpdateDishModel to the list.
                }

                // Initialize the ChefHomeAdapter with the updated list of dishes.
                adapter = new ChefHomeAdapter(getContext(), updateDishModelList);
                // Set the adapter for the RecyclerView to display the dishes.
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idd = item.getItemId();
        if (idd == R.id.logout){
            Logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Logout() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear the current screen (and its stack)
        startActivity(intent);
    }
}
