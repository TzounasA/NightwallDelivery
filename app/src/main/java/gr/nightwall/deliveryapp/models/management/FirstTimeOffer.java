package gr.nightwall.deliveryapp.models.management;

import android.content.Context;

import java.util.Locale;

import gr.nightwall.deliveryapp.R;

public class FirstTimeOffer {

    private double discountAmount, minPrice;

    public FirstTimeOffer() {
    }

    public FirstTimeOffer(double discountAmount, double minPrice) {
        this.discountAmount = discountAmount;
        this.minPrice = minPrice;
    }

    // SETTERS
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    // GETTERS
    public double getDiscountAmount() {
        return discountAmount;
    }

    public String getDiscountAmountString() {
        return String.format(Locale.getDefault(), "-%.2f €", discountAmount);
    }

    public double getMinPrice() {
        return minPrice;
    }

    public String getTitle(Context context) {
        String titleFormat = context.getString(R.string.first_time_discount_title);
        return String.format(titleFormat, discountAmount);
    }

    public String getDescription(Context context) {
        String minPriceString = "";
        if (minPrice > 0) {
            String formatMinPrice = context.getString(R.string.first_time_discount_min_price);
            minPriceString = String.format(formatMinPrice, minPrice);
        }

        String format = context.getString(R.string.first_time_discount_description);
        return String.format(format, discountAmount, minPriceString);
    }
}
