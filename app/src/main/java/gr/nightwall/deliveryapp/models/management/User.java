package gr.nightwall.deliveryapp.models.management;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import gr.nightwall.deliveryapp.models.Address;

public class User {

    public enum Type{
        USER ("ΧΡΗΣΤΗΣ"),
        MANAGER ("ΔΙΑΧΕΙΡΙΣΤΗΣ"),
        CREW ("ΠΡΟΣΩΠΙΚΟ");

        private final String value;

        Type(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String id, email, fullName;
    private Type type;
    private List<String> ordersIds;
    private String signUpDate;

    private String profilePictureURL, phone;
    private ArrayList<Address> addresses;
    private int defaultAddressIndex;

    private boolean firstTimeDiscountInformed, firstTimeDiscountTaken;


    /* = = = = = = = = = = = = = = = *
     *          CONSTRUCTORS         *
     * = = = = = = = = = = = = = = = */

    public User() {}

    public User(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;

        id = UUID.randomUUID().toString();

        type = Type.USER;
        ordersIds = new ArrayList<>();
        signUpDate = getCurrentDate();

        phone = "";
        profilePictureURL = "";

        addresses = new ArrayList<>();
        defaultAddressIndex = 0;

    }


    /* = = = = = = = = = = = = = = = *
     *            SETTERS            *
     * = = = = = = = = = = = = = = = */

    public void setId(String id) {
        this.id = id;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDefaultAddressIndex(int defaultAddressIndex) {
        if (addresses == null || addresses.size() == 0 || addresses.size() <= defaultAddressIndex) {
            this.defaultAddressIndex = 0;
            return;
        }

        this.defaultAddressIndex = defaultAddressIndex;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setFirstTimeDiscountInformed(boolean firstTimeDiscountInformed) {
        this.firstTimeDiscountInformed = firstTimeDiscountInformed;
    }

    public void setFirstTimeDiscountTaken(boolean firstTimeDiscountTaken) {
        this.firstTimeDiscountTaken = firstTimeDiscountTaken;
    }


    /* = = = = = = = = = = = = = = = *
     *             EDITS             *
     * = = = = = = = = = = = = = = = */

    public void addOrder(String id) {
        checkIfNull();
        ordersIds.add(id);
    }

    public void addAddress(Address address) {
        if (addresses == null)
            addresses = new ArrayList<>();

        addresses.add(address);
    }

    public void replaceAddressAt(int index, Address address){
        if (addresses.size() > index || address == null)
            return;

        addresses.set(index, address);
    }

    public void removeAddressAt(int index){
        if (addresses.size() > index)
            addresses.remove(index);
    }


    /* = = = = = = = = = = = = = = = *
     *            GETTERS            *
     * = = = = = = = = = = = = = = = */

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public List<String> getOrdersIds() {
        return ordersIds;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public ArrayList<Address> getAddresses() {
        return addresses == null ? new ArrayList<Address>() : addresses;
    }

    public int getDefaultAddressIndex() {
        return defaultAddressIndex;
    }

    public Address getDefaultAddress() {
        if (addresses == null || addresses.size() == 0)
            return null;

        if (addresses.size() <= defaultAddressIndex)
            defaultAddressIndex = 0;

        return addresses.get(defaultAddressIndex);
    }

    public boolean isFirstTimeDiscountInformed() {
        return firstTimeDiscountInformed;
    }

    public boolean isFirstTimeDiscountTaken() {
        return firstTimeDiscountTaken;
    }


    /* = = = = = = = = = = = = = = = *
     *         GET MORE INFO         *
     * = = = = = = = = = = = = = = = */

    @Exclude
    public int getOrdersCount(){
        checkIfNull();
        return ordersIds.size();
    }

    @Exclude
    public String getPhoneNumberText() {
        if (phone != null && !phone.isEmpty()) {
            return "(+30) "
                    + phone.substring(0, 3) + " "
                    + phone.substring(3, 6) + " "
                    + phone.substring(6, 10);
        }
        return "";
    }


    /* = = = = = = = = = = = = = = = *
     *        PRIVATE METHODS        *
     * = = = = = = = = = = = = = = = */

    private String getCurrentDate(){
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy",
                Locale.getDefault());

        return sdf.format(date);
    }

    private void checkIfNull(){
        if (ordersIds == null)
            ordersIds = new ArrayList<>();
    }


    /* = = = = = = = = = = = = = = = *
     *            USELESS            *
     * = = = = = = = = = = = = = = = */
    public void setOrdersIds(List<String> ordersIds) {
        this.ordersIds = ordersIds;
    }

    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

}
