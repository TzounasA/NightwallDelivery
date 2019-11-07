package gr.nightwall.deliveryapp.models.shop;

import androidx.annotation.NonNull;
import gr.nightwall.deliveryapp.models.Address;
import gr.nightwall.deliveryapp.models.Date;
import gr.nightwall.deliveryapp.models.management.FirstTimeOffer;

import java.util.ArrayList;
import java.util.Locale;

public class Order {
    private static final int HOURS_TO_BE_RECENT = 2;

    public enum Status {
        NEW ("ΝΕΑ"),
        RECEIVED ("ΕΓΙΝΕ ΑΠΟΔΟΧΗ"),
        SHIPPING ("ΣΤΟ ΔΡΟΜΟ"),
        DONE ("ΟΛΟΚΛΗΡΩΘΗΚΕ"),
        CANCELED ("ΑΚΥΡΩΘΗΚΕ");

        private final String value;

        Status(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private int notificationID;
    private String id, userId, fullName, phone;
    private Address address;
    private Date date;
    private ArrayList<Item> items;
    private Status status;

    private String additionalInfo;
    private String cancelingReason;

    private double finalPrice;

    private FirstTimeOffer firstTimeOffer;


    /* = = = = = = = = = = = = = = = *
     *          CONSTRUCTORS         *
     * = = = = = = = = = = = = = = = */

    public Order() {}

    public Order(String userId, String fullName, Address address, String phone,
                 @NonNull ArrayList<Item> items) {
        this.userId = userId;
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.items = items;

        notificationID = Date.getTimeStamp();

        date = Date.getNow();
        id = generateId(userId);
        status = Status.NEW;

        additionalInfo = "";

        for (Item item : items) {
            finalPrice += item.getFinalPrice() * item.getQuantity();
        }
    }


    /* = = = = = = = = = = = = = = = *
     *            SETTERS            *
     * = = = = = = = = = = = = = = = */

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCancelingReason(String cancelingReason) {
        this.cancelingReason = cancelingReason;
    }

    public void setFirstTimeOffer(FirstTimeOffer firstTimeOffer) {
        this.firstTimeOffer = firstTimeOffer;
    }

    /* = = = = = = = = = = = = = = = *
     *            GETTERS            *
     * = = = = = = = = = = = = = = = */

    public String getId() {
        return id;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public String getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public String getFullName() {
        return fullName;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Status getStatus() {
        return status;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public String getCancelingReason() {
        return cancelingReason;
    }

    public FirstTimeOffer getFirstTimeOffer() {
        return firstTimeOffer;
    }

    /* = = = = = = = = = = = = = = = *
     *         GET MORE INFO         *
     * = = = = = = = = = = = = = = = */

    public String getPhoneNumberText() {
        if (phone != null && !phone.isEmpty()) {
            return "(+30) "
                    + phone.substring(0, 3) + " "
                    + phone.substring(3, 6) + " "
                    + phone.substring(6, 10);
        }
        return "";
    }

    @NonNull
    public String getFinalPriceString(){
        return String.format(Locale.getDefault(), "%.2f €", getFinalPrice());
    }

    public double getActualIncome(){
        if (status == Status.DONE)
            return finalPrice;
        else
            return 0;
    }

    public String getItemsString(){
        StringBuilder itemsBuilder = new StringBuilder();

        if (items == null || items.size() == 0)
            return "";

        int i = 0;
        for (Item item : items){
            if (i != 0) itemsBuilder.append("\n");
            itemsBuilder.append("• ").append(item.getName());

            if (item.getQuantity() > 1)
                itemsBuilder.append(" (x").append(item.getQuantity()).append(")");

            i++;
        }

        return itemsBuilder.toString();
    }

    public String getDateString() {
        return date.getDateAndTimeString();
    }

    public boolean isRecent(){
        if (HOURS_TO_BE_RECENT == -1)
            return true;

        Date now = Date.getNow();
        int difference = Date.getHoursDifference(getDate(), now);
        return difference >= 0 && difference < HOURS_TO_BE_RECENT;
    }


    /* = = = = = = = = = = = = = = = *
     *        PRIVATE METHODS        *
     * = = = = = = = = = = = = = = = */

    private String generateId(String username){
        return String.format("%s_%s", date.getDateForId(), username);
    }


}
