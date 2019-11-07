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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import androidx.core.content.ContextCompat;
import gr.nightwall.deliveryapp.R;

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
     *         GET DATABASES         *
     * = = = = = = = = = = = = = = = */

    private static DatabaseReference getDatabase(String db){
        return FirebaseDatabase.getInstance().getReference(db);
    }

    public static DatabaseReference getUsersDatabase(){
        return getDatabase(Consts.DB_USERS);
    }

    public static DatabaseReference getBusinessSettingsDatabase(){
        return getDatabase(Consts.DB_BUSINESS_SETTINGS);
    }

    public static DatabaseReference getCategoriesDatabase(){
        return getDatabase(Consts.DB_CATEGORIES);
    }

    public static DatabaseReference getItemsDatabase(){
        return getDatabase(Consts.DB_ITEMS);
    }

    public static DatabaseReference getIngredientCategoriesDatabase(){
        return getDatabase(Consts.DB_INGREDIENT_CATEGORIES);
    }

    public static DatabaseReference getIngredientsDatabase(){
        return getDatabase(Consts.DB_INGREDIENTS);
    }

    public static DatabaseReference getOrdersDatabase(){
        return getDatabase(Consts.DB_ORDERS);
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
     *            DIALOGS            *
     * = = = = = = = = = = = = = = = */

    public static void initDialog(final Context context, final View dialog){
//        CardView cardDialog = dialog.findViewById(R.id.cardDialog);
//        View shadowBack = dialog.findViewById(R.id.shadowBack);
//
//        // Can't click through
//        cardDialog.setClickable(true);
//        cardDialog.setFocusable(true);
//
//        // Shadow Click
//        shadowBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                closeDialog(context, dialog);
//            }
//        });
//
//        // Button Negative
//        MaterialButton btnNegative = dialog.findViewById(R.id.btnNegative);
//        if (btnNegative != null)
//            btnNegative.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Utils.closeDialog(context, dialog);
//                }
//            });
    }

    public static void openDialog(final View dialog){
//        CardView cardDialog = dialog.findViewById(R.id.cardDialog);
//        View shadowBack = dialog.findViewById(R.id.shadowBack);
//
//        // Animation
//        YoYo.with(Techniques.FadeInUp)
//                .duration(300)
//                .onStart(new YoYo.AnimatorCallback() {
//                    @Override
//                    public void call(Animator animator) {
//                        dialog.setVisibility(View.VISIBLE);
//                    }
//                })
//                .playOn(cardDialog);
//
//        YoYo.with(Techniques.FadeIn)
//                .duration(400)
//                .interpolate(new AccelerateInterpolator())
//                .playOn(shadowBack);
    }

    public static void closeDialog(Context context, final View dialog){
//        CardView cardDialog = dialog.findViewById(R.id.cardDialog);
//        View shadowBack = dialog.findViewById(R.id.shadowBack);
//
//        // Animation
//        YoYo.with(Techniques.FadeOutDown)
//                .duration(300)
//                .onEnd(new YoYo.AnimatorCallback() {
//                    @Override
//                    public void call(Animator animator) {
//                        dialog.setVisibility(View.GONE);
//                    }
//                })
//                .playOn(cardDialog);
//
//        YoYo.with(Techniques.FadeOut)
//                .duration(300)
//                .interpolate(new DecelerateInterpolator())
//                .playOn(shadowBack);
//
//        closeSoftKeyboard(context);
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
        String id = name.trim().replace(" ", "_");
        id = id + "_" + UUID.randomUUID().toString();
        return Utils.fixDatabasePath(id);
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
