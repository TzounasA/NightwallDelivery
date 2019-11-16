package gr.nightwall.deliveryapp.models.shop;

import com.google.firebase.database.Exclude;

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

    private float startPrice;
    private String priorityNumber;

    private String templateId;
    private ArrayList<String> selectedIngredientsIDs;

    // Optional
    private String imageURL, description;

    // For orders
    private int quantity;
    private String additionalInfo;
    private float ingredientsPrice;
    private Map<String, ArrayList<String>> ingredientsNames;

    //endregion

    //region CONSTRUCTORS

    public Item() {
        id = Utils.generateID(name);
        templateId = "";
        name = "";

        description = "";
        imageURL = "";
        ingredientsNames = new HashMap<>();
    }

    public Item(String categoryId, String priority){
        this();
        this.categoryId = categoryId;
        priorityNumber = priority;
    }

    //endregion

    //region SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setStartPrice(float startPrice) {
        this.startPrice = startPrice;
    }

    public void setPriorityNumber(String priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public float getStartPrice() {
        return startPrice;
    }

    public String getPriorityNumber() {
        return priorityNumber;
    }

    public String getTemplateId() {
        return templateId;
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

    @Exclude
    public float getFinalPrice() {
        return startPrice + ingredientsPrice;
    }

    //endregion

    //region GET MORE INFO

    @Exclude
    public String getNameWithFinalPrice(){
        return String.format(Locale.getDefault(), "%s (%.2f €)",name, getFinalPrice());
    }

    @Exclude
    public String getStartPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", startPrice);
    }

    @Exclude
    public String getStartPriceFormatted(){
        return String.format(Locale.US, "%.2f", startPrice);
    }

    @Exclude
    public String getIngredientsPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", ingredientsPrice);
    }

    @Exclude
    public String getIngredientsPriceFormatted(){
        return String.format(Locale.US, "%.2f", ingredientsPrice);
    }

    @Exclude
    public String getFinalPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", getFinalPrice());
    }

    @Exclude
    public String getFinalPriceFormatted(){
        return String.format(Locale.US, "%.2f", getFinalPrice());
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
    }

    @Exclude
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

    @Exclude
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
