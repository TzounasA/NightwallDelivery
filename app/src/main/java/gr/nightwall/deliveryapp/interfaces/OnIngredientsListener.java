package gr.nightwall.deliveryapp.interfaces;

import gr.nightwall.deliveryapp.models.shop.Ingredient;

public interface OnIngredientsListener {
    void onAddIngredient(String ingredientCategoryName, Ingredient ingredient);
    void onRemoveIngredient(String ingredientCategoryName, Ingredient ingredient);
}
