package com.example.ru_foody.chefFoodPanel;

public class FoodDetails {

    public String Description, Quantity, Price, Dish, ImageURL, RandomUID, ChefId;

    public FoodDetails(String dish, int quantity, double price, String description, String imageUrl, String randomUid, String chefId) {
        // Default constructor required for Firebase Realtime Database
    }
    public FoodDetails(String description, String quantity, String price, String dishes, String imageURL, String randomUID, String chefId) {
        Description = description;
        Quantity = quantity;
        Price = price;
        Dish = dishes;
        ImageURL = imageURL;
        RandomUID = randomUID;
        ChefId = chefId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDish() {
        return Dish;
    }

    public void setDish(String dish) {
        Dish = dish;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getChefId() {
        return ChefId;
    }

    public void setChefId(String chefId) {
        ChefId = chefId;
    }
}
