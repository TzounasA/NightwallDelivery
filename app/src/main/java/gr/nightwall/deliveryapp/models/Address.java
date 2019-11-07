package gr.nightwall.deliveryapp.models;

import android.content.Context;

import java.util.Calendar;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.utils.Utils;

public class Address {

    private String id, street, number, area;
    private String floor, doorbellName;
    private String extraInfo;


    /* = = = = = = = = = = = = = = = = *
     *           CONSTRUCTORS          *
     * = = = = = = = = = = = = = = = = */

    public Address() {}

    public Address(String street, String number, String floor, String doorbellName) {
        this.street = street;
        this.doorbellName = doorbellName;
        this.number = number;
        this.floor = floor;

        id = "address_" + Calendar.getInstance().getTimeInMillis();

        area = "";
        extraInfo = "";
    }


    /* = = = = = = = = = = = = = = = = *
     *              SETTERS            *
     * = = = = = = = = = = = = = = = = */

    public void setStreet(String street) {
        this.street = street;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setDoorbellName(String doorbellName) {
        this.doorbellName = doorbellName;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }


    /* = = = = = = = = = = = = = = = = *
     *              GETTERS            *
     * = = = = = = = = = = = = = = = = */

    public String getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getArea() {
        return area;
    }

    public String getDoorbellName() {
        return doorbellName;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getNumber() {
        return number;
    }

    public String getFloor() {
        return floor;
    }


    /* = = = = = = = = = = = = = = = = *
     *           MORE GETTERS          *
     * = = = = = = = = = = = = = = = = */

    public String getMainAddress(){
        StringBuilder stringBuilder = new StringBuilder(street);
        stringBuilder.append(" ").append(number);

        if (area != null && !area.isEmpty())
            stringBuilder.append(", ").append(area);

        return stringBuilder.toString();
    }

    public String getFloorAndDoorbellName(Context context){
        return getFloorString(context) + ", " + doorbellName;
    }

    public String getFloorString(Context context){
        if (floor.equals("0"))
            return context.getString(R.string.floor0);

        if (Utils.isInteger(floor))
            return String.format(context.getString(R.string.x_floor), floor);

        return floor;
    }

}
