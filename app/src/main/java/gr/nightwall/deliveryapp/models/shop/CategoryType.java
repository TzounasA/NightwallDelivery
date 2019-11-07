package gr.nightwall.deliveryapp.models.shop;

import android.content.Context;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;

public class CategoryType {

    // TYPE
    public enum Type{FOOD, SNACKS, SWEETS, DRINKS, COFFEE, OFFERS}

    // VARIABLES
    private Type type;
    private String name;


    // CONSTRUCTORS

    public CategoryType() {
    }

    public CategoryType(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public static ArrayList<CategoryType> getAllTypesList(Context con){
        ArrayList<CategoryType> list = new ArrayList<>();

        String[] typesArray = con.getResources().getStringArray(R.array.category_types);
        list.add(new CategoryType(Type.FOOD, typesArray[0]));
        list.add(new CategoryType(Type.SNACKS, typesArray[1]));
        list.add(new CategoryType(Type.SWEETS, typesArray[2]));
        list.add(new CategoryType(Type.DRINKS, typesArray[3]));
        list.add(new CategoryType(Type.COFFEE, typesArray[4]));
        list.add(new CategoryType(Type.OFFERS, typesArray[5]));

        return list;
    }

    // METHODS
    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getIconRes(){
        switch (type){
            case FOOD: return R.drawable.ic_food_24;
            case SNACKS: return R.drawable.ic_snacks;
            case SWEETS: return R.drawable.ic_sweets;
            case DRINKS: return R.drawable.ic_drinks;
            case COFFEE: return R.drawable.ic_coffee_24;
            case OFFERS: return R.drawable.ic_offer_24;
            default: return R.drawable.ic_category_24;
        }
    }

    public int getID(){
        switch (type){
            case FOOD: return 0;
            case SNACKS: return 1;
            case SWEETS: return 2;
            case DRINKS: return 3;
            case COFFEE: return 4;
            case OFFERS: return 5;
            default: return -1;
        }
    }



    /* = = = = = = = = = = = = = = = *
     *            USELESS            *
     * = = = = = = = = = = = = = = = */

    public void setType(Type type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
