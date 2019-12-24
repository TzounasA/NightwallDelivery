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
    private String categoryId, categoryName;

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

    public Item(String categoryId, String categoryName, String priority){
        this();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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

    public void clearIngredients(){
        if (selectedIngredientsIDs != null)
            selectedIngredientsIDs.clear();

        if (ingredientsNames != null)
            ingredientsNames.clear();

        ingredientsPrice = 0;
    }

    //endregion

    //region GETTERS

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
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

    public Map<String, ArrayList<String>> getIngredientsNames() {
        return ingredientsNames;
    }

    @Exclude
    public float getFinalPrice() {
        return startPrice + ingredientsPrice;
    }

    @Exclude
    public float getTotalItemsPrice(){
        return getFinalPrice() * quantity;
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

    @Exclude
    public String getTotalItemsString(){
        return String.format(Locale.getDefault(), "%.2f €", getTotalItemsPrice());
    }

    @Exclude
    public String getTotalItemsFormatted(){
        return String.format(Locale.US, "%.2f", getTotalItemsPrice());
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
        // Checks
        if(ingredientsNames == null){
            ingredientsNames = new HashMap<>();
        }

        if(selectedIngredientsIDs == null){
            selectedIngredientsIDs = new ArrayList<>();
        }

        if (ingredientCategoryName == null)
            return;

        // Add
        if (!ingredientsNames.containsKey(ingredientCategoryName)) {
            ingredientsNames.put(ingredientCategoryName, new ArrayList<>());
        }

        if (!ingredientsNames.get(ingredientCategoryName).contains(ingredient.getName()))
            ingredientsNames.get(ingredientCategoryName).add(ingredient.getName());

        if (!selectedIngredientsIDs.contains(ingredient.getId())){
            selectedIngredientsIDs.add(ingredient.getId());
            ingredientsPrice += ingredient.getPrice();
        }
    }


    public void removeIngredient(String ingredientCategoryName, Ingredient ingredient){
        // Check
        if (ingredientCategoryName == null || ingredient == null)
            return;

        if (ingredientsNames == null || selectedIngredientsIDs == null)
            return;

        if (!ingredientsNames.containsKey(ingredientCategoryName))
            return;

        if (ingredientsNames.get(ingredientCategoryName) == null)
            return;

        // Remove
        try {
            ingredientsNames.get(ingredientCategoryName).remove(ingredient.getName());

            if (ingredientsNames.get(ingredientCategoryName).isEmpty())
                ingredientsNames.remove(ingredientCategoryName);

            boolean removed = selectedIngredientsIDs.remove(ingredient.getId());

            if (removed)
                ingredientsPrice -= ingredient.getPrice();

        } catch (Exception ignored) {

        }
    }

    @Exclude
    public String getAllIngredientsString(){
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

    public int getAllIngredientsCount(){
        if (ingredientsNames == null)
            ingredientsNames = new HashMap<>();

        int counter = 0;
        Set<String> categories = ingredientsNames.keySet();
        for (String category : categories){
            counter += ingredientsNames.get(category).size();
        }

        return counter;
    }

    //endregion

}
