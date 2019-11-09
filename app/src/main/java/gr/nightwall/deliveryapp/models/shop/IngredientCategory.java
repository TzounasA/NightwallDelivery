package gr.nightwall.deliveryapp.models.shop;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.utils.Utils;

public class IngredientCategory {

    public enum Options{SINGLE, MULTIPLE}

    private String id, name;
    private Options options;
    private boolean required;
    private ArrayList<Ingredient> ingredients;
    private String priorityNumber;

    private String description, iconURL;


    /* = = = = = = = = = = = = = = = *
     *          CONSTRUCTORS         *
     * = = = = = = = = = = = = = = = */

    public IngredientCategory(){
        this("010");
    }

    public IngredientCategory(String priority){
        name = "";
        priorityNumber = priority;

        options = Options.MULTIPLE;

        id = Utils.generateID();

        description = "";
        iconURL = "";
    }


    /* = = = = = = = = = = = = = = = *
     *            SETTERS            *
     * = = = = = = = = = = = = = = = */

    public void setName(String name) {
        this.name = name;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public void setPriorityNumber(String priorityNumber) {
        this.priorityNumber = priorityNumber;
    }


    /* = = = = = = = = = = = = = = = *
     *             EDITS             *
     * = = = = = = = = = = = = = = = */

    public void addIngredient(Ingredient ingredient){
        checkIfNull();
        ingredients.add(ingredient);
    }

    public void removeIngredientAt(int index){
        checkIfNull();
        ingredients.remove(index);
    }

    public void removeIngredient(Ingredient ingredient){
        checkIfNull();
        ingredients.remove(ingredient);
    }


    /* = = = = = = = = = = = = = = = *
     *            GETTERS            *
     * = = = = = = = = = = = = = = = */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Options getOptions() {
        return options;
    }

    public boolean isRequired() {
        return required;
    }

    public ArrayList<Ingredient> getIngredients() {
        checkIfNull();
        return ingredients;
    }

    public String getDescription() {
        return description;
    }

    public String getPriorityNumber() {
        return priorityNumber;
    }

    public String getIconURL() {
        return iconURL;
    }


    /* = = = = = = = = = = = = = = = *
     *         GET MORE INFO         *
     * = = = = = = = = = = = = = = = */

    @Exclude
    public int getIngredientsCount(){
        checkIfNull();
        return ingredients.size();
    }


    /* = = = = = = = = = = = = = = = *
     *        PRIVATE METHODS        *
     * = = = = = = = = = = = = = = = */

    @Exclude
    private void checkIfNull(){
        if (ingredients == null)
            ingredients = new ArrayList<>();
    }


    /* = = = = = = = = = = = = = = = *
     *            USELESS            *
     * = = = = = = = = = = = = = = = */

    public void setId(String id) {
        this.id = id;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
