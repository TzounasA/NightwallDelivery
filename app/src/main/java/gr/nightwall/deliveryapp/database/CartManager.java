package gr.nightwall.deliveryapp.database;

import java.util.ArrayList;
import java.util.Locale;

import gr.nightwall.deliveryapp.App;
import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.models.shop.Cart;
import gr.nightwall.deliveryapp.models.shop.Item;

public class CartManager {

    private Cart cart;

    public CartManager() {
        cart = LocalDBHelper.getCart();
    }

    public void update(){
        cart = LocalDBHelper.getCart();
    }

    public void save(){
        LocalDBHelper.saveCart(cart);
    }

    // GETTERS

    public ArrayList<Item> getItems(){
        return cart.getItemsInCart();
    }

    public Item getItemAt(int index){
        if (getItems().size() <= index)
            return null;

        return getItems().get(index);
    }

    public int getItemsCount(){
        int count = 0;
        for (Item item : getItems()) {
            count += item.getQuantity();
        }

        return count;
    }

    public String getItemsString(){
        int itemsCount = getItemsCount();

        switch (itemsCount){
            case 0:
                return App.getContext().getString(R.string.cart_is_empty);
            case 1:
                return App.getContext().getString(R.string.one_item);
            default:
                String format = App.getContext().getString(R.string.x_items);
                return String.format(Locale.getDefault(), format, itemsCount);
        }


    }

    public double getTotalPrice(){
        double finalPrice = 0;

        for (Item item : getItems()) {
            finalPrice += item.getFinalPrice() * item.getQuantity();
        }

        return finalPrice;
    }

    public String getTotalPriceString(){
        return String.format(Locale.getDefault(), "%.2f€", getTotalPrice());
    }

    public boolean isEmpty(){
        return cart.isEmpty();
    }

    public boolean hasItems(){
        return !isEmpty();
    }

    // EDIT

    public void addItem(Item item){
        cart.add(item);
        save();
    }

    public void removeItemAt(int index){
        cart.removeAt(index);
        save();
    }

    public void removeItem(Item item){
        cart.remove(item);
        save();
    }

    public void increaseQuantity(int index){
        cart.increaseQuantity(index);
        save();
    }

    public void decreaseQuantity(int index){
        cart.decreaseQuantity(index);
        save();
    }

    public void emptyCart(){
        cart.empty();
        save();
    }

}
