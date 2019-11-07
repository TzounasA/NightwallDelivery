package gr.nightwall.deliveryapp.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import gr.nightwall.deliveryapp.R;

public class UtilsActions {


    public static void makeCall(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));

        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
        else
            copyToClipboard(context, "phone", phone);
    }

    public static void sendEmail(Context context, String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Email from the App");

        intent.setType("message/rfc822");

        intent = Intent.createChooser(intent, context.getString(R.string.sent_email_via));
        context.startActivity(intent);
    }

    public static void openOnWeb(Context context, String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void copyToClipboard(Context context, String label, String s){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label , s);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getString(R.string.copied) + "\n" + s, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Error: Can\'t copy to clipboard", Toast.LENGTH_SHORT).show();
        }

    }


    /* = = = = = = = = = = = = = = = *
     *           FACEBOOK            *
     * = = = = = = = = = = = = = = = */

    public static void openFacebook(Context context, String username) {
        String url = "https://www.facebook.com/" + username;

        Intent intent = newFacebookIntent(context.getPackageManager(), url);
        if (intent != null)
            context.startActivity(intent);
        else
            openOnWeb(context, url);
    }

    private static Intent newFacebookIntent(PackageManager pm, String url) {
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                return new Intent(Intent.ACTION_VIEW, uri);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            return null;
        }

        return null;
    }


    /* = = = = = = = = = = = = = = = *
     *          INSTAGRAM            *
     * = = = = = = = = = = = = = = = */

    public static void openInstagram(Context context, String username) {
        String url = "https://www.instagram.com/" + username;
        Intent intent = newInstagramProfileIntent(context.getPackageManager(), username);
        if (intent != null)
            context.startActivity(intent);
        else
            openOnWeb(context, url);

    }

    private static Intent newInstagramProfileIntent(PackageManager pm, String username) {
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                return intent;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            return null;
        }
        return null;
    }
}
