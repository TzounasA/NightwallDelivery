package gr.nightwall.deliveryapp.data;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.models.shop.Cart;
import gr.nightwall.deliveryapp.models.shop.Order;

class OrdersManagementDB {

    // Orders
    static void saveOrder(Order order){

    }

    static ArrayList<Order> getAllOrders(){
        return null;
    }

    static Order getOrderByID(String id){
        return null;
    }

    // Cart
    static void saveCart(Cart cart){
        LocalDBHelper.saveCart(cart);
    }

    static Cart getCart(){
        return LocalDBHelper.getCart();
    }

}
