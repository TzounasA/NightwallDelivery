package gr.nightwall.deliveryapp.utils;

import android.content.Context;
import android.content.Intent;

import gr.nightwall.deliveryapp.App;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Navigation {

    public static void startActivity(Class newClass){
        Context context = App.getContext();

        context.startActivity(new Intent(context, newClass).setFlags(FLAG_ACTIVITY_NEW_TASK));
    }

}
