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
            sortIngredientCategories();
            return;
        }

        // New item
        int index = getIngredientCategoryIndex(category.getId());
        if (index == -1){
            ingredientsCategories.add(category);
            sortIngredientCategories();
            return;
        }

        // Existing item
        ingredientsCategories.set(index, category);
        sortIngredientCategories();
    }

    public void deleteIngredientCategory(String id){
        if (ingredientsCategories == null)
            return;

        IngredientCategory category = getIngredientCategoryById(id);

        if (category != null)
            ingredientsCategories.remove(category);
    }

    public IngredientCategory getIngredientCategoryAt(int index){
        if (ingredientsCategories == null)
            return null;

        if (index < 0 || index >= ingredientsCategories.size())
            return null;

        return ingredientsCategories.get(index);
    }

    @Exclude
    public String getPriorityForNewIngredientCategory(){
        int priority = (getIngredientCategoriesCount() + 1) * 10;

        return String.format(Locale.getDefault(), "%03d", priority);
    }


    /* = = = = = = = = = = = = = = = *
     *        PRIVATE HELPERS        *
     * = = = = = = = = = = = = = = = */

    private IngredientCategory getIngredientCategoryById(String id){
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
            if (category.getId().equals(categoryId)){
                return index;
            }

            index ++;
        }

        return -1;
    }

    private void sortIngredientCategories(){
        IngredientCategory temp;
        if (ingredientsCategories.size() <= 1)
            return;;

        for (int x = 0; x < ingredientsCategories.size(); x++)
        {
            for (int i = 0; i < ingredientsCategories.size()-i; i++) {
                if (ingredientsCategories.get(i).getPriorityNumber().compareTo((ingredientsCategories.get(i+1).getPriorityNumber())) > 0)
                {
                    temp = ingredientsCategories.get(i);
                    ingredientsCategories.set(i, ingredientsCategories.get(i+1));
                    ingredientsCategories.set(i+1, temp);
                }
            }
        }
    }

}
