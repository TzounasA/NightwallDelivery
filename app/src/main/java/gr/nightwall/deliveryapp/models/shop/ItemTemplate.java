package gr.nightwall.deliveryapp.models.shop;

import java.util.ArrayList;
import java.util.UUID;

import gr.nightwall.deliveryapp.utils.Utils;

public class ItemTemplate {

    private String id, name;

    private ArrayList<IngredientCategory> ingredientsCategories;


    /* = = = = = = = = = = = = = = = *
     *          CONSTRUCTORS         *
     * = = = = = = = = = = = = = = = */

    public ItemTemplate() {
        name = "";
        ingredientsCategories = new ArrayList<>();

        id = UUID.randomUUID().toString();
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

    public ArrayList<IngredientCategory> getIngredientsCategories() {
        return ingredientsCategories;
    }


    /* = = = = = = = = = = = = = = = *
     *            SETTERS            *
     * = = = = = = = = = = = = = = = */

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredientsCategories(ArrayList<IngredientCategory> ingredientsCategories) {
        this.ingredientsCategories = ingredientsCategories;
    }


    /* = = = = = = = = = = = = = = = *
     *             EDITS             *
     * = = = = = = = = = = = = = = = */

    public void addIngredientCategory(IngredientCategory category){
        if (ingredientsCategories == null)
            ingredientsCategories = new ArrayList<>();

        ingredientsCategories.add(category);
    }

    public void deleteIngredientCategory(String id){
        if (ingredientsCategories == null)
            return;

        for (IngredientCategory category : ingredientsCategories) {
            if (category.getId().equals(id)){
                ingredientsCategories.remove(category);
                return;
            }
        }
    }

}
