package gr.nightwall.deliveryapp.data;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.models.Phone;
import gr.nightwall.deliveryapp.models.Time;
import gr.nightwall.deliveryapp.models.management.BusinessSettings;
import gr.nightwall.deliveryapp.models.management.User;

public class BusinessManagementDB {

    // Business Settings
    public static void saveBusinessSettings(BusinessSettings settings){
        LocalDBHelper.saveBusinessSettings(settings);
    }

    public static BusinessSettings getBusinessSettings(){
        BusinessSettings settings = LocalDBHelper.getBusinessSettings();

        if(settings != null){
            return settings;
        }

        settings = new BusinessSettings("Nightwall Delivery");

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
