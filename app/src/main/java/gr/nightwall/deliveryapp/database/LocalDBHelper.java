package gr.nightwall.deliveryapp.database;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import gr.nightwall.deliveryapp.App;
import gr.nightwall.deliveryapp.models.Phone;
import gr.nightwall.deliveryapp.models.Time;
import gr.nightwall.deliveryapp.models.management.BusinessSettings;
import gr.nightwall.deliveryapp.models.management.User;
import gr.nightwall.deliveryapp.models.shop.Cart;

import static gr.nightwall.deliveryapp.utils.Consts.PREF_BUSINESS_SETTINGS;
import static gr.nightwall.deliveryapp.utils.Consts.PREF_CART;
import static gr.nightwall.deliveryapp.utils.Consts.PREF_USER;

public class LocalDBHelper {

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }

    // USER
    public static boolean isLoggedIn() {
        return getUser() != null;
    }

    static void saveUser(User user) {
        SharedPreferences.Editor editor = getPreferences().edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(PREF_USER, json);
        editor.apply();
    }

    static User getUser() {
        Gson gson = new Gson();

        String json = getPreferences().getString(PREF_USER, null);
        return gson.fromJson(json, User.class);
    }

    // CART
    static void saveCart(Cart cart) {
        SharedPreferences.Editor editor = getPreferences().edit();

        Gson gson = new Gson();
        String json = gson.toJson(cart);
        editor.putString(PREF_CART, json);
        editor.apply();
    }

    static Cart getCart() {
        Gson gson = new Gson();

        String json = getPreferences().getString(PREF_CART, null);
        if (json == null){
            return new Cart();
        }
        return gson.fromJson(json, Cart.class);
    }

    // BUSINESS SETTINGS
    public static void saveBusinessSettings(BusinessSettings businessSettings) {
        SharedPreferences.Editor editor = getPreferences().edit();

        Gson gson = new Gson();
        String json = gson.toJson(businessSettings);
        editor.putString(PREF_BUSINESS_SETTINGS, json);
        editor.apply();
    }

    public static BusinessSettings getBusinessSettings() {
        String json = getPreferences().getString(PREF_BUSINESS_SETTINGS, null);

        if (json == null){
            return initBusiness();
        }

        Gson gson = new Gson();
        return gson.fromJson(json, BusinessSettings.class);
    }

    private static BusinessSettings initBusiness(){
        BusinessSettings settings = new BusinessSettings("Nightwall Delivery");

        settings.setActiveHoursStart(new Time(18, 0));
        settings.setActiveHoursEnd(new Time(2, 0));
        settings.setAverageWaitingTime(30);
        settings.setMinimumOrderPrice(5.0);

        settings.setAddress("Ανεξαρτησίας **");
        settings.addPhone(new Phone("6940000000", Phone.PhoneType.MOBILE));
        settings.addPhone(new Phone("2651000000", Phone.PhoneType.WORK));
        settings.setEmail("nightwall.gr@gmail.com");

        settings.setFacebookUsername("nightwall.gr");
        settings.setInstagramUsername("nightwall.gr");
        settings.setWebsite("nightwall.gr");

        return settings;
    }

}
