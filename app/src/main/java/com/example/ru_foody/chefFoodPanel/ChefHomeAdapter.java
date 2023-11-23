package com.example.ru_foody.chefFoodPanel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ru_foody.R;

import java.util.List;

public class ChefHomeAdapter extends RecyclerView.Adapter<ChefHomeAdapter.ViewHolder> {

    private final Context mcont;
    private final List<UpdateDishModel> updateDishModelList;

    //binds data from a list of UpdateDishModel objects to views within a RecyclerView
    public ChefHomeAdapter(Context context, List<UpdateDishModel>updateDishModelList){
        this.updateDishModelList = updateDishModelList;
        this.mcont = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcont).inflate(R.layout.chefmenu_update_delete, parent, false);
        return new ChefHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChefHomeAdapter.ViewHolder holder, int position) {

        final UpdateDishModel updateDishModel = updateDishModelList.get(position);
        holder.Dish.setText(updateDishModel.getDish());
        String randomUID = updateDishModel.getRandomUID();
        Log.d("DebugFoody", "Random UID: " + randomUID);
        holder.itemView.setOnClickListener(v -> {
            Log.d("DebugFoody", "Random UID (before starting activity): " + randomUID);
            Intent intent = new Intent(mcont, UpdateDelete_Dish.class);
            intent.putExtra("updatedeletedish", randomUID);
            mcont.startActivity(intent);
        });

    }

    //returns the total number of items in the dataset
    @Override
    public int getItemCount() {
        return updateDishModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView Dish;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Dish = itemView.findViewById(R.id.dish_name);
        }
    }

}
