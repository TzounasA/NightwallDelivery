package gr.nightwall.deliveryapp.database;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.database.interfaces.OnGetDataListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.models.Phone;
import gr.nightwall.deliveryapp.models.Time;
import gr.nightwall.deliveryapp.models.management.BusinessSettings;
import gr.nightwall.deliveryapp.models.management.User;

import static gr.nightwall.deliveryapp.database.FirebaseDB.Reference.BUSINESS_SETTINGS;

public class BusinessManagementDB {

    // Business Settings
    public static void saveBusinessSettings(BusinessSettings settings, OnSaveDataListener onSaveDataListener){
        FirebaseDB.saveData(BUSINESS_SETTINGS, settings, onSaveDataListener);
    }

    public static void getBusinessSettings(OnGetDataListener onGetDataListener){
        FirebaseDB.getData(BUSINESS_SETTINGS, BusinessSettings.class, onGetDataListener);
    }

    // Users
    public static void saveUser(User user){

    }

    public static ArrayList<User> getAllUsers(){
        return null;
    }

    public static User getUserByID(String id){
        return null;
    }

}
