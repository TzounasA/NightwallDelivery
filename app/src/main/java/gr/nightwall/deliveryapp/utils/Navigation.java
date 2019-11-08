package gr.nightwall.deliveryapp.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import gr.nightwall.deliveryapp.App;
import gr.nightwall.deliveryapp.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA;

public class Navigation {

    public static void startActivity(Class newClass){
        Context context = App.getContext();

        context.startActivity(new Intent(context, newClass).setFlags(FLAG_ACTIVITY_NEW_TASK));
    }

    public static void startActivity(Class newClass, String extra){
        Context context = App.getContext();

        Intent intent = new Intent(context, newClass).setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ITEM_EXTRA, extra);

        context.startActivity(intent);
    }

    public static void errorToast(Context context){
        Toast.makeText(context, context.getString(R.string.error_something_went_wrong), Toast.LENGTH_SHORT).show();
    }

}
