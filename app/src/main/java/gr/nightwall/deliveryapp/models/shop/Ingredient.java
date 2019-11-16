package gr.nightwall.deliveryapp.models.shop;

import com.google.firebase.database.Exclude;

import java.util.Locale;

import gr.nightwall.deliveryapp.utils.Utils;

public class Ingredient {

    private String id, name;
    private float price;

    // For orders
    private boolean selected;

    /* = = = = = = = = = = = = = = = *
     *          CONSTRUCTORS         *
     * = = = = = = = = = = = = = = = */

    public Ingredient() {
        id = Utils.generateID();
        name = "";
        price = 0;
    }

    /* = = = = = = = = = = = = = = = *
     *            SETTERS            *
     * = = = = = = = = = = = = = = = */

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public float getPrice() {
        return price;
    }

    public boolean isSelected() {
        return selected;
    }

    /* = = = = = = = = = = = = = = = *
     *         GET MORE INFO         *
     * = = = = = = = = = = = = = = = */

    @Exclude
    public String getPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", price);
    }

    @Exclude
    public String getPriceFormatted(){
        return String.format(Locale.getDefault(), "%.2f", price);
    }

    @Exclude
    public String getNameWithPrice(){
        if (price == 0)
            return name;

        return String.format("%s (+ %s)", name, getPriceString());
    }



}
