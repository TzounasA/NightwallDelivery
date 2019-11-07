package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.data.BusinessManagementDB;
import gr.nightwall.deliveryapp.models.Time;
import gr.nightwall.deliveryapp.models.management.BusinessSettings;
import gr.nightwall.deliveryapp.views.SettingsLineInput;
import gr.nightwall.deliveryapp.views.SettingsLineSwitch;
import gr.nightwall.deliveryapp.views.SettingsLineText;

public class BusinessSettingsActivity extends AppCompatActivity {

    // Data
    private BusinessSettings businessSettings;

    private Time activeHoursStart, activeHoursEnd;
    private boolean autoOperation;
    private int averageWaitingTime;
    private double minimumOrderPrice;

    // Views
    private TextView tvActiveHoursStart, tvActiveHoursEnd;
    private ViewGroup lOpenNowCustom;
    private ViewGroup lAverageWaitingTime, lMinOrderPrice, lineAddress;
    private TextView tvAverageWaitingTime, tvMinOrderPrice;
    private ViewGroup linePhone1, linePhone2, lineEmail;
    private ViewGroup lineFacebook, lineInstagram, lineWebsite;

    //region CONSTRUCTOR

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_settings);

        load();
        init();
        setupScreen();
    }

    private void init() {
        initToolbar();
        initScreen();
    }

    private void load(){
        businessSettings = BusinessManagementDB.getBusinessSettings();
    }

    private void initToolbar() {
        ActionBar bar = getSupportActionBar();
        if (bar == null){
            return;
        }
        bar.setDisplayHomeAsUpEnabled(true);
    }

    private void initScreen() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //endregion

    //region SET UP SCREEN

    private void setupScreen(){
        setupHours();
        setupBasic();
        setupContact();
        setupSocialMedia();
    }

    private void setupHours() {
        tvActiveHoursStart = findViewById(R.id.tvActiveHoursStart);
        tvActiveHoursStart.setText(businessSettings.getActiveHoursStart().getTimeString());

        tvActiveHoursEnd = findViewById(R.id.tvActiveHoursEnd);
        tvActiveHoursEnd.setText(businessSettings.getActiveHoursEnd().getTimeString());

        new SettingsLineSwitch(findViewById(R.id.lineOpenNowCustom))
                .title(getString(R.string.auto_operation))
                .secondaryText(getString(R.string.auto_operation_body))
                .checked(businessSettings.isAutoOperation())
                .switchCheckedChangeListener((sw, checked) -> autoOperation = checked);
    }

    private void setupBasic() {
        tvAverageWaitingTime = findViewById(R.id.tvAverageWaitingTime);
        tvAverageWaitingTime.setText(businessSettings.getAverageWaitingTimeString());

        tvMinOrderPrice = findViewById(R.id.tvMinOrderPrice);
        tvMinOrderPrice.setText(businessSettings.getMinimumOrderPriceString());

        lineAddress = findViewById(R.id.lineAddress);
        new SettingsLineInput(lineAddress)
                .hint(getString(R.string.address))
                .prefill(businessSettings.getAddress())
                .iconRes(R.drawable.ic_location_24);
    }

    private void setupContact() {
        linePhone1 = findViewById(R.id.linePhone1);

        if (businessSettings.getPhones().size() > 0){
            new SettingsLineInput(linePhone1)
                    .hint(getString(R.string.edit_phone))
                    .prefill(businessSettings.getPhoneAt(0).getNumber())
                    .iconRes(R.drawable.ic_phone_24);
        } else{
            linePhone1.setVisibility(View.GONE);
        }

        linePhone2 = findViewById(R.id.linePhone2);
        if (businessSettings.getPhones().size() > 1){
            new SettingsLineInput(linePhone2)
                    .hint(getString(R.string.edit_phone))
                    .prefill(businessSettings.getPhoneAt(1).getNumber())
                    .iconRes(R.drawable.ic_phone_24);
        } else{
            linePhone2.setVisibility(View.GONE);
        }

        lineEmail = findViewById(R.id.lineEmail);
        new SettingsLineInput(lineEmail)
                .hint(getString(R.string.edit_email))
                .prefill(businessSettings.getEmail())
                .iconRes(R.drawable.ic_email_24);

    }

    private void setupSocialMedia() {
        lineFacebook = findViewById(R.id.lineFacebook);
        new SettingsLineInput(lineFacebook)
                .hint(getString(R.string.edit_facebook))
                .prefill(businessSettings.getFacebookUsername())
                .iconRes(R.drawable.ic_facebook);

        lineInstagram = findViewById(R.id.lineInstagram);
        new SettingsLineInput(lineInstagram)
                .hint(getString(R.string.edit_instagram))
                .prefill(businessSettings.getInstagramUsername())
                .iconRes(R.drawable.ic_instagram);

        lineWebsite = findViewById(R.id.lineWebsite);
        new SettingsLineInput(lineWebsite)
                .hint(getString(R.string.edit_website))
                .prefill(businessSettings.getEmail());


    }

    //endregion

}
