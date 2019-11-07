package gr.nightwall.deliveryapp.models;

public class Phone {
    public enum PhoneType{MOBILE, WORK}

    private String number;
    private PhoneType phoneType;
    private String title;


    /* = = = = = = = = = = = = = = = = *
     *           CONSTRUCTORS          *
     * = = = = = = = = = = = = = = = = */

    public Phone() {}

    public Phone(String number, PhoneType phoneType) {
        this.number = number;
        this.phoneType = phoneType;
    }

    public Phone(String number, PhoneType phoneType, String title) {
        this.number = number;
        this.phoneType = phoneType;
        this.title = title;
    }

    /* = = = = = = = = = = = = = = = = *
     *              SETTERS            *
     * = = = = = = = = = = = = = = = = */

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    /* = = = = = = = = = = = = = = = = *
     *              GETTERS            *
     * = = = = = = = = = = = = = = = = */

    public String getNumber() {
        return number;
    }

    public String getNumberText() {
        if (phoneType == PhoneType.WORK) {
            return "(+30) "
                    + number.substring(0, 5) + " "
                    + number.substring(5, 7) + " "
                    + number.substring(7, 10);
        } else {
            return "(+30) "
                    + number.substring(0, 3) + " "
                    + number.substring(3, 6) + " "
                    + number.substring(6, 10);
        }
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public String getTitle() {
        return title;
    }
}
