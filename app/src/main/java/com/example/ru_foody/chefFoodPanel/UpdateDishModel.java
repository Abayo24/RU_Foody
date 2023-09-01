package com.example.ru_foody.chefFoodPanel;

import com.google.firebase.database.PropertyName;

public class UpdateDishModel {

    private String dish, quantity, price, description, imageURL, randomUID, chefId;

    public  UpdateDishModel(){

    }

    public UpdateDishModel(String dish, String quantity, String price, String description, String imageURL, String randomUID, String chefId) {
        this.dish = dish;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
        this.randomUID = randomUID;
        this.chefId = chefId;
    }


    @PropertyName("dish")
    public String getDish() {
        return dish;
    }

    @PropertyName("dish")
    public void setDish(String dishes) {
        this.dish = dishes;
    }

    @PropertyName("quantity")
    public String getQuantity() {
        return quantity;
    }

    @PropertyName("quantity")
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @PropertyName("price")
    public String getPrice() {
        return price;
    }

    @PropertyName("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }


    @PropertyName("imageURL")
    public String getImageURL() {
        return imageURL;
    }

    @PropertyName("imageURL")
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @PropertyName("randomUID")
    public String getRandomUID() {
        return randomUID;
    }

    @PropertyName("randomUID")
    public void setRandomUID(String randomUID) {
        this.randomUID = randomUID;
    }

    @PropertyName("chefId")
    public String getChefId() {
        return chefId;
    }

    @PropertyName("chefId")
    public void setChefId(String chefId) {
        this.chefId = chefId;
    }




}