package gr.nightwall.deliveryapp.models.shop;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Locale;
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
     *            SETTERS            *
     * = = = = = = = = = = = = = = = */

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredientsCategories(ArrayList<IngredientCategory> ingredientsCategories) {
        this.ingredientsCategories = ingredientsCategories;
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
     *         MORE GETTERS          *
     * = = = = = = = = = = = = = = = */

    @Exclude
    public int getIngredientCategoriesCount(){
        if (ingredientsCategories == null)
            return 0;

        return ingredientsCategories.size();
    }


    /* = = = = = = = = = = = = = = = *
     *             EDITS             *
     * = = = = = = = = = = = = = = = */

    public void saveIngredientCategory(IngredientCategory category){
        // First item
        if (ingredientsCategories == null) {
            ingredientsCategories = new ArrayList<>();
            ingredientsCategories.add(category);
            return;
        }

        // New item
        int index = getIngredientCategoryIndex(category.getId());
        if (index == -1){
            ingredientsCategories.add(category);
            return;
        }

        // Existing item
        ingredientsCategories.set(index, category);
    }

    public void deleteIngredientCategory(String id){
        if (ingredientsCategories == null)
            return;

        IngredientCategory category = getIngredientCategoryById(id);

        if (category != null)
            ingredientsCategories.remove(category);
    }

    public IngredientCategory getIngredientCategoryById(String id){
        for (IngredientCategory category : ingredientsCategories) {
            if (category.getId().equals(id)){
                return category;
            }
        }

        return null;
    }

    private int getIngredientCategoryIndex(String categoryId){
        int index = 0;
        for (IngredientCategory category : ingredientsCategories) {
            if (category.getId().equals(id)){
                return index;
            }

            index ++;
        }

        return -1;
    }

    @Exclude
    public String getPriorityForNewIngredientCategory(){
        int priority = (getIngredientCategoriesCount() + 1) * 10;

        return String.format(Locale.getDefault(), "%03d", priority);
    }

}
