package com.example.ru_foody.chefFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ru_foody.R;

public class ChefProfileFragment extends Fragment {

    Button postDish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chef_profile, null);
        getActivity().setTitle("Post Dish");

        postDish = v.findViewById(R.id.post_dish);

        postDish.setOnClickListener(v1 -> startActivity(new Intent(getContext(),chef_postDish.class)));

        return v;

    }
}
