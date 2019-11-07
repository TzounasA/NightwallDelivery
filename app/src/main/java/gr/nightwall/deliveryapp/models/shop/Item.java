package gr.nightwall.deliveryapp.models.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import gr.nightwall.deliveryapp.utils.Utils;

public class Item {

    //region Private fields

    private String id, name;
    private String categoryId;

    private double price;
    private String priorityNumber;

    private String templateID;
    private ArrayList<String> selectedIngredientsIDs;

    // Optional
    private String imageURL, description;

    // For orders
    private int quantity;
    private String additionalInfo;
    private float ingredientsPrice, finalPrice;
    private Map<String, ArrayList<String>> ingredientsNames;

    //endregion

    //region CONSTRUCTORS

    public Item() {}

    public Item(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;

        id = Utils.generateID(name);

        description = "";
        imageURL = "";
        ingredientsNames = new HashMap<>();
    }

    //endregion

    //region SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPriorityNumber(String priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public void setSelectedIngredientsIDs(ArrayList<String> selectedIngredientsIDs) {
        this.selectedIngredientsIDs = selectedIngredientsIDs;
    }

    // Optional

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // For orders

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    //endregion

    //region GETTERS

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getPriorityNumber() {
        return priorityNumber;
    }

    public String getTemplateID() {
        return templateID;
    }

    public ArrayList<String> getSelectedIngredientsIDs() {
        return selectedIngredientsIDs;
    }

    // Optional

    public String getImageURL() {
        return imageURL;
    }

    public String getDescription() {
        return description;
    }

    // For orders

    public int getQuantity() {
        return quantity;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public float getIngredientsPrice() {
        return ingredientsPrice;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    //endregion

    //region GET MORE INFO

    public String getPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", price);
    }

    public String getPriceFormatted(){
        return String.format(Locale.US, "%.2f", price);
    }

    public String getIngredientsPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", ingredientsPrice);
    }

    public String getIngredientsPriceFormatted(){
        return String.format(Locale.US, "%.2f", ingredientsPrice);
    }

    public String getFinalPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", finalPrice);
    }

    public String getFinalPriceFormatted(){
        return String.format(Locale.US, "%.2f", finalPrice);
    }

    //endregion

    //region FOR ORDERS

    public void increaseQuantity(){
        quantity ++;
    }

    public void decreaseQuantity(){
        quantity --;
    }

    public void addIngredient(String ingredientCategoryName, Ingredient ingredient){
        if(ingredientsNames == null){
            ingredientsNames = new HashMap<>();
        }

        if (ingredientCategoryName == null)
            return;

        if (!ingredientsNames.containsKey(ingredientCategoryName)) {
            ingredientsNames.put(ingredientCategoryName, new ArrayList<String>());
        }

        ingredientsNames.get(ingredientCategoryName).add(ingredient.getName());

        ingredientsPrice += ingredient.getPrice();
        finalPrice += ingredient.getPrice();
    }

    public String getAllSelectedIngredients(){
        try {
            if (ingredientsNames == null)
                return "";

            StringBuilder options = new StringBuilder();

            Set<String> categories = ingredientsNames.keySet();
            for (String category : categories){
                options.append("○ ").append(category).append(": \n");
                options.append(getIngredientsStringWithTabs(ingredientsNames.get(category)));
                options.append("\n");
            }

            return options.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    private String getIngredientsStringWithTabs(List<String> ingredients){
        StringBuilder ingredientsBuilder = new StringBuilder();

        if (ingredients.isEmpty())
            return "";

        for (String ingredient: ingredients){
            ingredientsBuilder.append("   • ").append(ingredient).append("\n");
        }

        return ingredientsBuilder.substring(0, ingredientsBuilder.length() - 1);
    }

    //endregion

}
