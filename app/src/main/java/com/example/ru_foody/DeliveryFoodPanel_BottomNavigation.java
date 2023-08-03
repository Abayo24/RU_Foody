package com.example.ru_foody;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ru_foody.deliveryFoodPanel.DeliveryPendingOrderFragment;
import com.example.ru_foody.deliveryFoodPanel.DeliveryShipOrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DeliveryFoodPanel_BottomNavigation extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_food_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.deliveryBottomNavigation);
        navigationView.setOnItemSelectedListener(this);
        String name = getIntent().getStringExtra("PAGE");
        if (name != null){
            if (name.equalsIgnoreCase("DeliveryOrderpage")) {
                loadDeliveryFragment(new DeliveryPendingOrderFragment());
            }
        }else{
            loadDeliveryFragment(new DeliveryPendingOrderFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.pendingOrders) {
            fragment = new DeliveryPendingOrderFragment();
        } else if (itemId == R.id.shipOrders) {
            fragment = new DeliveryShipOrderFragment();
        }
        return loadDeliveryFragment(fragment);
    }

    private boolean loadDeliveryFragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerBott,fragment).commit();
            return true;
        }
        return false;
    }
}