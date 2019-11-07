package gr.nightwall.deliveryapp.models.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart {

    private List<Item> itemsInCart;

    // CONSTRUCTOR
    public Cart() {}

    // EDIT
    public void addItem(Item item){
        checkIfNull();
        itemsInCart.add(item);
    }

    public void removeItemAt(int index){
        checkIfNull();
        itemsInCart.remove(index);
    }

    public void removeItem(Item item){
        checkIfNull();
        itemsInCart.remove(item);
    }

    public void increaseQuantity(int index){
        checkIfNull();
        if (getItemsCount() > index)
            itemsInCart.get(index).increaseQuantity();
    }

    public void decreaseQuantity(int index){
        checkIfNull();
        if (getItemsCount() > index)
            itemsInCart.get(index).decreaseQuantity();
    }

    public void emptyCart(){
        checkIfNull();
        itemsInCart.clear();
    }

    // GETTERS
    public ArrayList<Item> getItemsInCart() {
        checkIfNull();
        return (ArrayList<Item>) itemsInCart;
    }

    public Item getItemAt(int index){
        checkIfNull();
        if (getItemsCount() > index)
            return itemsInCart.get(index);
        return null;
    }

    public int getItemsCount(){
        checkIfNull();
        int count = 0;
        for (Item item : itemsInCart) {
            count += item.getQuantity();
        }
        return count;
    }

    public double getTotalPrice(){
        double finalPrice = 0;

        for (Item item : itemsInCart) {
            finalPrice += item.getFinalPrice() * item.getQuantity();
        }

        return finalPrice;
    }

    public String getTotalPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", getTotalPrice());
    }

    public boolean isEmpty(){
        return itemsInCart == null || itemsInCart.isEmpty();
    }

    // OTHER
    private void checkIfNull(){
        if (itemsInCart == null)
            itemsInCart = new ArrayList<>();
    }

}
