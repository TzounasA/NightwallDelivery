package gr.nightwall.deliveryapp.utils;

public final class Consts {

    // EMAIL SENDER
    public static final String EMAIL_SENDER = "nightwall.gr@gmail.com";
    public static final String EMAIL_PASSWORD = "n69456945";

    // DATABASES
    public static final String DB_USERS             = "Users";
    public static final String DB_BUSINESS_SETTINGS = "Business Settings";
    public static final String DB_ITEM_TEMPLATES    = "Item Templates";


    public static final String DB_CATEGORIES = "Categories";
    public static final String DB_ITEMS = "Items";
    public static final String DB_INGREDIENT_CATEGORIES = "Ingredient Categories";
    public static final String DB_INGREDIENTS = "Ingredients";
    public static final String DB_ORDERS = "Orders";

    // STORAGE
    public static final String STORAGE_USERS_PROFILE_PICTURES = "Users Profile Pictures";
    public static final String STORAGE_CATEGORIES_IMAGES = "Categories Images";
    public static final String STORAGE_ITEMS_IMAGES = "Items Images";

    // PREFERENCES
    public static final String LOGGED_IN_PREF = "logged_in_status";
    public static final String PREF_USER = "user";
    public static final String PREF_CART = "cart";
    public static final String PREF_BUSINESS_SETTINGS = "business_settings";

    // EMAIL
    public static final String DOT = "_dot_";
    public static final String AT = "_at_";

    // DIMENSIONS
    public static final int PROFILE_PHOTO_WIDTH = 500;
    public static final int CATEGORY_IMAGE_WIDTH = 1000;
    public static final int ITEM_IMAGE_WIDTH = 500;

    // NOTIFICATION_CHANNEL_ID
    public static final String NOTIFICATION_CHANNEL_ID = "100";
    public static final String CHANNEL_FOREGROUND_ID = "channel_foreground";
    public static final String CHANNEL_ORDERS_ID = "channel_orders";

    // INTENTS
    public static final String ITEM_EXTRA = "item_extra";
    public static final String ITEM_EXTRA_2 = "item_extra_2";
    public static final String ITEM_EXTRA_3 = "item_extra_3";
    public static final String ITEM_INT_EXTRA = "item_int_extra";
    public static final String IMAGE_URL_EXTRA = "image_url_extra";
    public static final String OPEN_CART = "open_cart";


    private Consts(){
        throw new AssertionError();
    }
}
