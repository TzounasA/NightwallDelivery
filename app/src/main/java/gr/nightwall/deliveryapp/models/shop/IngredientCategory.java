package gr.nightwall.deliveryapp.models.shop;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.utils.Utils;

public class IngredientCategory {

    public enum Options{SINGLE, MULTIPLE}

    private String id, name, itemId;
    private Options options;
    private boolean required;
    private ArrayList<Ingredient> ingredients;
    private String optionPriority, priorityNumber;

    private String description, iconURL;


    /* = = = = = = = = = = = = = = = *
     *          CONSTRUCTORS         *
     * = = = = = = = = = = = = = = = */

    public IngredientCategory(){}

    public IngredientCategory(String name, Options options, boolean required, String priorityNumber) {
        this.name = name;
        this.options = options;
        this.required = required;
        this.itemId = null;
        this.optionPriority = priorityNumber;
        this.priorityNumber = priorityNumber;

        id = Utils.generateID(name);

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

    public void setOptionPriority(String optionPriority) {
        this.optionPriority = optionPriority;
    }

    public void setPriorityNumber(String priorityNumber) {
        this.priorityNumber = priorityNumber;
        setOptionPriority(itemId + "_" + priorityNumber);
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

    public String getItemId() {
        return itemId;
    }

    public ArrayList<Ingredient> getIngredients() {
        checkIfNull();
        return ingredients;
    }

    public String getDescription() {
        return description;
    }

    public String getOptionPriority() {
        return optionPriority;
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

    public int getIngredientsCount(){
        checkIfNull();
        return ingredients.size();
    }


    /* = = = = = = = = = = = = = = = *
     *        PRIVATE METHODS        *
     * = = = = = = = = = = = = = = = */

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

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
