package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.database.BusinessManagementDB;
import gr.nightwall.deliveryapp.database.interfaces.OnGetItemListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.models.Phone;
import gr.nightwall.deliveryapp.models.Time;
import gr.nightwall.deliveryapp.models.management.BusinessSettings;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.views.CustomDialog;
import gr.nightwall.deliveryapp.views.SettingsLineInput;
import gr.nightwall.deliveryapp.views.SettingsLineSwitch;

public class BusinessSettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    private final String TIME_START_EDITED = "time_start";
    private final String TIME_END_EDITED = "time_end";

    // Data
    private BusinessSettings businessSettings;
    private String timeEdited;

    // Views
    private ViewGroup lineAddress;
    private ViewGroup linePhone1, linePhone2, lineEmail;
    private ViewGroup lineFacebook, lineInstagram, lineWebsite;

    private ViewGroup savingIndicator;

    private LinearLayout dialogHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_settings);

        init();
        load();
    }

    private void load(){
        savingIndicator.setVisibility(View.VISIBLE);
        findViewById(R.id.lParentView).setVisibility(View.INVISIBLE);

        OnGetItemListener getDataListener = new OnGetItemListener() {
            @Override
            public <T> void onSuccess(T item) {
                businessSettings = (BusinessSettings) item;

                if (businessSettings == null){
                    savingIndicator.setVisibility(View.GONE);
                    Navigation.errorToast(BusinessSettingsActivity.this);
                    return;
                }

                savingIndicator.setVisibility(View.GONE);
                findViewById(R.id.lParentView).setVisibility(View.VISIBLE);
                setupScreen();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(BusinessSettingsActivity.this);
            }
        };

        BusinessManagementDB.getBusinessSettings(getDataListener);
    }

    //region INITIALIZATION

    private void init() {
        initToolbar();
        initScreen();
        initActionButtons();
    }

    private void initToolbar() {
        ActionBar bar = getSupportActionBar();
        if (bar == null){
            return;
        }
        bar.setDisplayHomeAsUpEnabled(true);
    }

    private void initScreen() {
        savingIndicator = findViewById(R.id.savingIndicator);
        savingIndicator.setVisibility(View.GONE);

        dialogHolder = findViewById(R.id.dialogHolder);
    }

    private void initActionButtons(){
        // Cancel
        MaterialButton btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setText(getString(R.string.cancel));
        btnCancel.setOnClickListener(v -> onBackPressed());

        // Save
        MaterialButton btnSave = findViewById(R.id.btnPositive);
        btnSave.setText(getString(R.string.save));
        btnSave.setOnClickListener(v -> saveChangesFromInputs());
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
        // Active Hours Start
        TextView tvActiveHoursStart = findViewById(R.id.tvActiveHoursStart);
        tvActiveHoursStart.setText(businessSettings.getActiveHoursStart().getTimeString());

        Time start = businessSettings.getActiveHoursStart();
        tvActiveHoursStart.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    BusinessSettingsActivity.this,
                    R.style.AppTheme_DialogTime,
                    BusinessSettingsActivity.this,
                    start.getHours(), start.getMinutes(),
                    true);

            timeEdited = TIME_START_EDITED;
            timePickerDialog.show();
        });

        // Active Hours End
        TextView tvActiveHoursEnd = findViewById(R.id.tvActiveHoursEnd);
        tvActiveHoursEnd.setText(businessSettings.getActiveHoursEnd().getTimeString());

        Time end = businessSettings.getActiveHoursEnd();
        tvActiveHoursEnd.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    BusinessSettingsActivity.this,
                    R.style.AppTheme_DialogTime,
                    BusinessSettingsActivity.this,
                    end.getHours(), end.getMinutes(),
                    true);

            timeEdited = TIME_END_EDITED;
            timePickerDialog.show();
        });

        // Auto operation
        new SettingsLineSwitch(findViewById(R.id.lineOpenNowCustom))
                .title(getString(R.string.auto_operation))
                .secondaryText(getString(R.string.auto_operation_body))
                .checked(businessSettings.isAutoOperation())
                .switchCheckedChangeListener((sw, checked) -> editAutoOperation(checked));
    }

    private void setupBasic() {
        // Average Waiting Time
        TextView tvAverageWaitingTime = findViewById(R.id.tvAverageWaitingTime);
        tvAverageWaitingTime.setText(businessSettings.getAverageWaitingTimeString());
        findViewById(R.id.lAverageWaitingTime).setOnClickListener(v -> onEditWaitingTime());

        // Min Order Price
        TextView tvMinOrderPrice = findViewById(R.id.tvMinOrderPrice);
        tvMinOrderPrice.setText(businessSettings.getMinimumOrderPriceString());
        findViewById(R.id.lMinOrderPrice).setOnClickListener(v -> onEditMinOrderPrice());

        // Address
        lineAddress = findViewById(R.id.lineAddress);
        new SettingsLineInput(lineAddress)
                .hint(getString(R.string.address))
                .prefill(businessSettings.getAddress())
                .iconRes(R.drawable.ic_location_24);
    }

    private void setupContact() {
        // Phone 1
        linePhone1 = findViewById(R.id.linePhone1);
        if (businessSettings.getPhones().size() > 0){
            new SettingsLineInput(linePhone1)
                    .hint(getString(R.string.edit_phone))
                    .inputType(InputType.TYPE_CLASS_PHONE)
                    .prefill(businessSettings.getPhoneAt(0).getNumber())
                    .iconRes(R.drawable.ic_phone_24);
        } else{
            linePhone1.setVisibility(View.GONE);
        }

        // Phone 2
        linePhone2 = findViewById(R.id.linePhone2);
        if (businessSettings.getPhones().size() > 1){
            new SettingsLineInput(linePhone2)
                    .hint(getString(R.string.edit_phone))
                    .inputType(InputType.TYPE_CLASS_PHONE)
                    .prefill(businessSettings.getPhoneAt(1).getNumber())
                    .iconRes(R.drawable.ic_phone_24);
        } else{
            linePhone2.setVisibility(View.GONE);
        }

        // E-Mail
        lineEmail = findViewById(R.id.lineEmail);
        new SettingsLineInput(lineEmail)
                .hint(getString(R.string.edit_email))
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .prefill(businessSettings.getEmail())
                .iconRes(R.drawable.ic_email_24);

    }

    private void setupSocialMedia() {
        // Facebook
        lineFacebook = findViewById(R.id.lineFacebook);
        new SettingsLineInput(lineFacebook)
                .hint(getString(R.string.edit_facebook))
                .prefill(businessSettings.getFacebookUsername())
                .iconRes(R.drawable.ic_facebook);

        // Instagram
        lineInstagram = findViewById(R.id.lineInstagram);
        new SettingsLineInput(lineInstagram)
                .hint(getString(R.string.edit_instagram))
                .prefill(businessSettings.getInstagramUsername())
                .iconRes(R.drawable.ic_instagram);

        // Website
        lineWebsite = findViewById(R.id.lineWebsite);
        new SettingsLineInput(lineWebsite)
                .hint(getString(R.string.edit_website))
                .prefill(businessSettings.getEmail());


    }

    //endregion

    //region EDITS

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Time time = new Time(hourOfDay, minute);

        switch (timeEdited) {
            case TIME_START_EDITED:
                businessSettings.setActiveHoursStart(time);
                break;
            case TIME_END_EDITED:
                businessSettings.setActiveHoursEnd(time);
                break;
        }

        saveChanges();
    }

    private void editAutoOperation(boolean checked){
        businessSettings.setAutoOperation(checked);
        saveChanges();
    }

    @SuppressLint("InflateParams")
    private void onEditWaitingTime(){
        // Custom View
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout lTimeDialog = (LinearLayout)
                inflater.inflate(R.layout.dialog_waiting_time, null);

        final ScrollableNumberPicker numberPicker = lTimeDialog.findViewById(R.id.numberPicker);
        numberPicker.setValue(businessSettings.getAverageWaitingTime());

        // Dialog
        new CustomDialog(this, CustomDialog.DialogType.CUSTOM, dialogHolder)
                .setCustomView(lTimeDialog)
                .setTitle(getString(R.string.av_wait_time))
                .setBtnPositiveText(getString(R.string.save))
                .setOnPositiveClickListener(dialog -> {
                    int time = numberPicker.getValue();
                    editAverageWaitingTime(time);
                })
                .build(this)
                .show();
    }

    private void editAverageWaitingTime(int time) {
        businessSettings.setAverageWaitingTime(time);
        saveChanges();
    }

    @SuppressLint("InflateParams")
    private void onEditMinOrderPrice(){
        // Custom View
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout lTimeDialog = (LinearLayout)
                inflater.inflate(R.layout.dialog_waiting_time, null);

        final ScrollableNumberPicker numberPicker = lTimeDialog.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setStepSize(1);
        numberPicker.setValue((int) businessSettings.getMinimumOrderPrice());

        // Dialog
        new CustomDialog(this, CustomDialog.DialogType.CUSTOM, dialogHolder)
                .setCustomView(lTimeDialog)
                .setTitle(getString(R.string.min_order_price))
                .setBtnPositiveText(getString(R.string.save))
                .setOnPositiveClickListener(dialog -> {
                    int price = numberPicker.getValue();
                    editEditMinOrderPrice(price);
                })
                .build(this)
                .show();
    }

    private void editEditMinOrderPrice(int price) {
        businessSettings.setMinimumOrderPrice(price);
        saveChanges();
    }

    //endregion

    //region SAVE CHANGES

    private void saveChangesFromInputs(){
        businessSettings.setAddress(getTextFromSetting(lineAddress));

        businessSettings.editPhoneAt(0, new Phone(getTextFromSetting(linePhone1), Phone.PhoneType.MOBILE));
        businessSettings.editPhoneAt(1, new Phone(getTextFromSetting(linePhone2), Phone.PhoneType.WORK));
        businessSettings.setEmail(getTextFromSetting(lineEmail));

        businessSettings.setFacebookUsername(getTextFromSetting(lineFacebook));
        businessSettings.setInstagramUsername(getTextFromSetting(lineInstagram));
        businessSettings.setWebsite(getTextFromSetting(lineWebsite));

        saveChanges();
    }

    private String getTextFromSetting(ViewGroup setting){
        TextInputEditText input = setting.findViewById(R.id.input);

        if (input.getText() == null){
            return "";
        }

        return input.getText().toString();
    }

    private void saveChanges() {
        savingIndicator.setVisibility(View.VISIBLE);

        OnSaveDataListener saveListener = new OnSaveDataListener() {
            @Override
            public void onSuccess() {
                savingIndicator.setVisibility(View.GONE);
                setupScreen();
                Toast.makeText(BusinessSettingsActivity.this,
                        getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(BusinessSettingsActivity.this);
            }
        };

        BusinessManagementDB.saveBusinessSettings(businessSettings, saveListener);

    }

    //endregion

}
