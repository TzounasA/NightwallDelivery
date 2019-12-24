package gr.nightwall.deliveryapp.utils;

import android.animation.Animator;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.google.android.material.button.MaterialButton;
import androidx.core.app.NotificationCompat;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.models.shop.Cart;
import gr.nightwall.deliveryapp.models.shop.IngredientCategory;
import gr.nightwall.deliveryapp.models.shop.Item;

public class Utils {

    public static void closeSoftKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = ((Activity) context).getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /* = = = = = = = = = = = = = = = *
     *           RESOURCES           *
     * = = = = = = = = = = = = = = = */

    public static int getColor(Context context, int colorRes){
        return context.getResources().getColor(colorRes);
    }

    /* = = = = = = = = = = = = = = = *
     *             LISTS             *
     * = = = = = = = = = = = = = = = */

    public static String getPriorityFromListCount(int count){
        int priority = (count + 1) * 10;

        return String.format(Locale.getDefault(), "%03d", priority);
    }

    public static ArrayList<Item> sortItems(ArrayList<Item> items){
        if(items == null)
            return new ArrayList<>();

        Item temp;

        for (int x = 0; x < items.size(); x++)
        {
            for (int i = 0; i < items.size()-i; i++) {
                if (items.get(i).getPriorityNumber().compareTo((items.get(i+1).getPriorityNumber())) > 0)
                {
                    temp = items.get(i);
                    items.set(i, items.get(i+1));
                    items.set(i+1, temp);
                }
            }
        }

        return items;
    }

    /* = = = = = = = = = = = = = = = *
     *      GET FIREBASE STORAGE     *
     * = = = = = = = = = = = = = = = */

    private static StorageReference getStorage(String ref){
        return FirebaseStorage.getInstance().getReference(ref);
    }

    public static StorageReference getUsersProfilePicturesStorage(){
        return getStorage(Consts.STORAGE_USERS_PROFILE_PICTURES);
    }

    public static StorageReference getCategoriesImagesStorage(){
        return getStorage(Consts.STORAGE_CATEGORIES_IMAGES);
    }

    public static StorageReference getItemsImagesStorage(){
        return getStorage(Consts.STORAGE_ITEMS_IMAGES);
    }


    /* = = = = = = = = = = = = = = = *
     *        BITMAP ACTIONS         *
     * = = = = = = = = = = = = = = = */

    public static Bitmap getCompressedImage(Bitmap bmp, int destWidth) {
        int origWidth = bmp.getWidth();
        int origHeight = bmp.getHeight();

        if(origWidth > destWidth){
            double ratio =  (double) origWidth / destWidth;
            double destHeightD = origHeight / ratio;
            int destHeight = (int) destHeightD;

            bmp = Bitmap.createScaledBitmap(bmp, destWidth, destHeight, false);
        }

        return bmp;
    }

    public static byte[] getDataFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    public static String getFileExtension(Context con, Uri uri) {
        ContentResolver cR = con.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    /* = = = = = = = = = = = = = = = *
     *         RECYCLER VIEW         *
     * = = = = = = = = = = = = = = = */

    public static void initRecyclerView(Context context,
                                        RecyclerView recyclerView, RecyclerView.Adapter adapter){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public static void initHorizontalRecyclerView(Context context,
                                        RecyclerView recyclerView, RecyclerView.Adapter adapter){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    /* = = = = = = = = = = = = = = = *
     *             TEXT              *
     * = = = = = = = = = = = = = = = */

    public static String fixDatabasePath(String s){
        return s.replace(".", "_dot_")
                .replace("#", "_No_")
                .replace("$", "_dollar_")
                .replace("[", "_LB_")
                .replace("]", "_RB_");
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }

        return true;
    }

    public static String generateID(String name){
        if (name == null || name.isEmpty()){
            return generateID();
        }

        String id = name.trim().replace(" ", "_");
        id = id + "_" + UUID.randomUUID().toString();
        return Utils.fixDatabasePath(id);
    }

    public static String generateID(){
        return UUID.randomUUID().toString();
    }

    public static String getTextFromSetting(ViewGroup setting){
        TextInputEditText input = setting.findViewById(R.id.input);

        if (input.getText() == null){
            return "";
        }

        return input.getText().toString();
    }

    public static float getFloatFromSetting(ViewGroup setting) throws NumberFormatException{
        String numberString = getTextFromSetting(setting);

        if (numberString.isEmpty()){
            throw new NumberFormatException();
        }

        return Float.parseFloat(numberString);
    }

    /* = = = = = = = = = = = = = = = *
     *             JSON              *
     * = = = = = = = = = = = = = = = */

    public static <T> String toJson(T item){
        return new Gson().toJson(item);
    }

    public static <T> T fromJson(String itemJson, Class<T> itemClass){
        return new Gson().fromJson(itemJson, itemClass);
    }


    /* = = = = = = = = = = = = = = = *
     *            TOASTS             *
     * = = = = = = = = = = = = = = = */

    public static void showShortToast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String s){
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }


    /* = = = = = = = = = = = = = = = *
     *           INTERNET            *
     * = = = = = = = = = = = = = = = */

    public static boolean networkIsAvailable(Context context){
        boolean isOnline = true;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isOnline = netInfo != null && netInfo.isConnectedOrConnecting();
        }

        return isOnline;
    }


    /* = = = = = = = = = = = = = = = *
     *      NOTIFICATION CHANNEL     *
     * = = = = = = = = = = = = = = = */

    public static void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Consts.NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static NotificationCompat.Builder createBasicNotificationBuilder(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Consts.CHANNEL_ORDERS_ID);
        builder.setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(context.getResources().getColor(R.color.colorAccent))
                //TODO.setSmallIcon(R.drawable.ic_logo_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[]{0, 400, 200, 400});

        return builder;
    }
}
