package com.example.ru_foody;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ru_foody.customerFoodPanel.CustomerCartFragment;
import com.example.ru_foody.customerFoodPanel.CustomerHomeFragment;
import com.example.ru_foody.customerFoodPanel.CustomerOrdersFragment;
import com.example.ru_foody.customerFoodPanel.CustomerProfileFragment;
import com.example.ru_foody.customerFoodPanel.CustomerTrackFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerFoodPanel_BottomNavigation extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_panel_bottom_navigation);
        BottomNavigationView navigationView= findViewById(R.id.customerBottomNavigation);
        navigationView.setOnItemSelectedListener(this);
        String name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (name != null){
            if (name.equalsIgnoreCase("Homepage")){
                loadCustomerFragment(new CustomerHomeFragment());
            }else if (name.equalsIgnoreCase("Preparingpage")){
                loadCustomerFragment(new CustomerTrackFragment());
            }else if (name.equalsIgnoreCase("DeliveryOrderpage")){
                loadCustomerFragment(new CustomerTrackFragment());
            }else if (name.equalsIgnoreCase("Thankyoupage")){
                loadCustomerFragment(new CustomerHomeFragment());
            }
        }else{
            loadCustomerFragment(new CustomerHomeFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.customerHome) {
            fragment = new CustomerHomeFragment();
        } else if (itemId == R.id.cart) {
            fragment = new CustomerCartFragment();
        } else if (itemId == R.id.customerProfile) {
            fragment = new CustomerProfileFragment();
        } else if (itemId == R.id.customerOrders) {
            fragment = new CustomerOrdersFragment();
        }else if (itemId == R.id.track) {
            fragment = new CustomerTrackFragment();
        }
        return loadCustomerFragment(fragment);
    }

    private boolean loadCustomerFragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
            return true;
        }
        return false;
    }
}