package gr.nightwall.deliveryapp.models.management;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.models.Phone;
import gr.nightwall.deliveryapp.models.Time;

public class BusinessSettings {

    // Variables
    private String businessName;
    private String address, email;
    private ArrayList<Phone> phones = new ArrayList<>();

    private Time activeHoursStart, activeHoursEnd;
    private int averageWaitingTime;
    private double minimumOrderPrice;

    private String facebookUsername, instagramUsername, website;

    @Exclude
    private FirstTimeOffer firstTimeOffer;

    private boolean autoOperation;


    /* = = = = = = = = = = = = = = = = *
     *           CONSTRUCTORS          *
     * = = = = = = = = = = = = = = = = */

    public BusinessSettings() {
        this("Νέο κατάστημα");
    }

    public BusinessSettings(String businessName) {
        this.businessName = businessName;
        address = "";
        email = "";

        activeHoursStart = new Time(0, 0);
        activeHoursEnd = new Time(0, 0);
        averageWaitingTime = 30;
        minimumOrderPrice = 0;

        facebookUsername = "";
        instagramUsername = "";
        website = "";

        autoOperation = true;
    }

    /* = = = = = = = = = = = = = = = = *
     *              SETTERS            *
     * = = = = = = = = = = = = = = = = */

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActiveHoursStart(Time activeHoursStart) {
        this.activeHoursStart = activeHoursStart;
    }

    public void setActiveHoursEnd(Time activeHoursEnd) {
        this.activeHoursEnd = activeHoursEnd;
    }

    public void setAverageWaitingTime(int minutes) {
        this.averageWaitingTime = minutes;
    }

    public void setMinimumOrderPrice(double minimumOrderPrice) {
        this.minimumOrderPrice = minimumOrderPrice;
    }

    public void setAutoOperation(boolean autoOperation) {
        this.autoOperation = autoOperation;
    }

    public void setFacebookUsername(String facebookUsername) {
        this.facebookUsername = facebookUsername;
    }

    public void setInstagramUsername(String instagramUsername) {
        this.instagramUsername = instagramUsername;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Exclude
    public void setFirstTimeOffer(FirstTimeOffer firstTimeOffer) {
        this.firstTimeOffer = firstTimeOffer;
    }


    // Edit Phones
    public void setPhones(ArrayList<Phone> phones) {
        this.phones = phones;
    }

    public void addPhone(Phone phone){
        if (phones == null) phones = new ArrayList<>();

        phones.add(phone);
    }

    public void editPhoneAt(int index, Phone phone){
        if (phones == null || phones.size() <= index) return;

        phones.set(index, phone);
    }

    public boolean removePhone(Phone phone){
        if (phones == null) return false;

        return phones.remove(phone);
    }

    public Phone removePhoneAt(int index){
        if (phones == null || phones.size() <= index) return null;

        return phones.remove(index);
    }


    /* = = = = = = = = = = = = = = = = *
     *              GETTERS            *
     * = = = = = = = = = = = = = = = = */

    public String getBusinessName() {
        return businessName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Phone> getPhones() {
        return phones;
    }

    public Time getActiveHoursStart() {
        return activeHoursStart;
    }

    public Time getActiveHoursEnd() {
        return activeHoursEnd;
    }

    public int getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public double getMinimumOrderPrice() {
        return minimumOrderPrice;
    }

    public boolean isAutoOperation() {
        return autoOperation;
    }

    public String getFacebookUsername() {
        return facebookUsername;
    }

    public String getInstagramUsername() {
        return instagramUsername;
    }

    public String getWebsite() {
        return website;
    }

    @Exclude
    public FirstTimeOffer getFirstTimeOffer() {
        return firstTimeOffer;
    }

    // Extra Getters
    @Exclude
    public String getActiveHours(){
        return activeHoursStart.getTimeString() + " - " + activeHoursEnd.getTimeString();
    }

    @Exclude
    public String getAverageWaitingTimeString() {
        return averageWaitingTime + "\'";
    }

    @Exclude
    public String getMinimumOrderPriceString() {
        return minimumOrderPrice + "€";
    }

    @Exclude
    public Phone getPhoneAt(int index){
        if (phones == null || index >= phones.size())
            return null;

        return phones.get(index);
    }

    @Exclude
    public String getFacebookUrl(){
        return "https://www.facebook.com/" + facebookUsername;
    }

    @Exclude
    public boolean hasFacebook() {
        return facebookUsername != null && ! facebookUsername.isEmpty();
    }

    @Exclude
    public String getInstagramUrl(){
        return "https://www.instagram.com/" + instagramUsername;
    }

    @Exclude
    public boolean hasInstagram() {
        return instagramUsername != null && ! instagramUsername.isEmpty();
    }

    @Exclude
    public boolean isOpenNow(){
        return isAutoOperation() && isInActiveHours();
    }

    @Exclude
    public boolean hasFirstTimeDiscount(){
        return firstTimeOffer != null;
    }


    // Can Order
    @Exclude
    public boolean canOrderThisNow(double price){
        return isOpenNow() && isOverMinimum(price);
    }

    @Exclude
    public boolean isInActiveHours(){
        Time now = Time.getNow();

        int timeStart = activeHoursStart.getHours() * 60 + activeHoursStart.getMinutes();
        int timeEnd = activeHoursEnd.getHours() * 60 + activeHoursEnd.getMinutes();

        int timeNow = now.getHours() * 60 + now.getMinutes();

        if (activeHoursStart.getHours() > activeHoursEnd.getHours()) {
            return timeNow >= timeStart || timeNow < timeEnd;
        }

        return timeNow >= timeStart && timeNow < timeEnd;
    }

    @Exclude
    public boolean isOverMinimum(double price){
        return price >= minimumOrderPrice;
    }



}
