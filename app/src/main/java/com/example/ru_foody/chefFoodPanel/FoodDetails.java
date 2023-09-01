package com.example.ru_foody.chefFoodPanel;

import com.google.firebase.database.PropertyName;

public class FoodDetails {

    private String dish;
    private String quantity;
    private String price;
    private String description;
    private String imageURL;
    private String randomUID;
    private static String chefId;

    public FoodDetails() {
        // Default constructor required for Firebase Realtime Database
    }

    public FoodDetails(String dish, String quantity, String price, String description, String imageURL, String randomUID, String chefId) {
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
        dish = dishes;
    }

    @PropertyName("quantity")
    public String getQuantity() {
        return quantity;
    }

    @PropertyName("quantity")
    public void setQuantity(String quantity) {
        quantity = quantity;
    }

    @PropertyName("price")
    public String getPrice() {
        return price;
    }

    @PropertyName("price")
    public void setPrice(String price) {
        price = price;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        description = description;
    }


    @PropertyName("imageURL")
    public String getImageURL() {
        return imageURL;
    }

    @PropertyName("imageURL")
    public void setImageURL(String imageURL) {
        imageURL = imageURL;
    }

    @PropertyName("randomUID")
    public String getRandomUID() {
        return randomUID;
    }

    @PropertyName("randomUID")
    public void setRandomUID(String randomUID) {
        randomUID = randomUID;
    }

    @PropertyName("chefId")
    public String getChefId() {
        return chefId;
    }

    @PropertyName("chefId")
    public void setChefId(String chefId) {
        chefId = chefId;
    }
}
