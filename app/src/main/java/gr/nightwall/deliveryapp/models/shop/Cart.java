package gr.nightwall.deliveryapp.models.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart {

    private List<Item> itemsInCart;

    // CONSTRUCTOR
    public Cart() {}

    // EDIT
    public void add(Item item){
        checkIfNull();
        itemsInCart.add(item);
    }

    public void removeAt(int index){
        checkIfNull();
        itemsInCart.remove(index);
    }

    public void remove(Item item){
        checkIfNull();
        itemsInCart.remove(item);
    }

    public void increaseQuantity(int index){
        checkIfNull();
        if (itemsInCart.size() > index)
            itemsInCart.get(index).increaseQuantity();
    }

    public void decreaseQuantity(int index){
        checkIfNull();
        if (itemsInCart.size() > index)
            itemsInCart.get(index).decreaseQuantity();
    }

    public void empty(){
        checkIfNull();
        itemsInCart.clear();
    }

    // GETTERS
    public ArrayList<Item> getItemsInCart() {
        checkIfNull();
        return (ArrayList<Item>) itemsInCart;
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
