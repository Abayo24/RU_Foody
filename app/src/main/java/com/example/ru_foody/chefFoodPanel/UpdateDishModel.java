package com.example.ru_foody.chefFoodPanel;

public class UpdateDishModel {

    private String ChefId, Description, Dish, ImageURL, Price, Quantity, RandomUID;

    public  UpdateDishModel(){

    }

    public UpdateDishModel(String chefId, String description, String dish, String imageURL, String price, String quantity, String randomUID) {
        ChefId = chefId;
        Description = description;
        Dish = dish;
        ImageURL = imageURL;
        Price = price;
        Quantity = quantity;
        RandomUID = randomUID;

    }

    public String getChefId() {
        return ChefId;
    }

    public void setChefId(String chefId) {
        ChefId = chefId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }
}
