package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.models.shop.Item;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.SettingsLineInput;

public class EditItemActivity extends AppCompatActivity {

    // Data
    private Item item;

    //Views
    private ViewGroup linePriority, lineName, lineDescription, lineStartPrice;

    private ViewGroup savingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        getExtra();
        init();
        setupScreen();
    }

    private void getExtra(){
        String itemJson = getIntent().getStringExtra(Consts.ITEM_EXTRA);

        item = Utils.fromJson(itemJson, Item.class);

        if (item == null){
            Navigation.errorToast(this);
            finish();
        }
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

        if (item.getName().isEmpty()){
            bar.setTitle(getString(R.string.add_item));
        }
    }

    private void initScreen() {
        savingIndicator = findViewById(R.id.savingIndicator);
        savingIndicator.setVisibility(View.GONE);
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
        setupInputs();
    }

    private void setupInputs() {
        // Priority
        linePriority = findViewById(R.id.lineItemPriority);
        new SettingsLineInput(linePriority)
                .hint(getString(R.string.priority))
                .prefill(item.getPriorityNumber())
                .inputType(InputType.TYPE_CLASS_NUMBER);

        // Name
        lineName = findViewById(R.id.lineItemName);
        new SettingsLineInput(lineName)
                .hint(getString(R.string.title))
                .prefill(item.getName())
                .iconRes(R.drawable.ic_category_24);

        // Description
        lineDescription = findViewById(R.id.lineItemDescription);
        new SettingsLineInput(lineDescription)
                .hint(getString(R.string.description))
                .prefill(item.getDescription())
                .iconRes(R.drawable.id_description);

        // Start Price
        lineStartPrice = findViewById(R.id.lineItemStartPrice);
        new SettingsLineInput(lineStartPrice)
                .hint(getString(R.string.price))
                .prefill(item.getStartPrice() + "")
                .helperText(getString(R.string.dot_for_comma))
                .iconRes(R.drawable.ic_euro);
    }

    //endregion

    //region SAVE CHANGES

    private void saveChangesFromInputs() {
        item.setPriorityNumber(Utils.getTextFromSetting(linePriority));

        item.setName(Utils.getTextFromSetting(lineName));
        item.setDescription(Utils.getTextFromSetting(lineDescription));

        float price;
        try {
            price = Utils.getFloatFromSetting(lineStartPrice);
        } catch (NumberFormatException e) {
            new SettingsLineInput(lineStartPrice)
                    .errorText(getString(R.string.error_number));
            return;
        }

        item.setStartPrice(price);

        saveChanges();
    }

    private void saveChanges() {
        savingIndicator.setVisibility(View.VISIBLE);

        OnSaveDataListener saveListener = new OnSaveDataListener() {
            @Override
            public void onSuccess() {
                savingIndicator.setVisibility(View.GONE);
                setupScreen();
                Toast.makeText(EditItemActivity.this,
                        getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(EditItemActivity.this);
            }
        };

        MenuDB.saveItem(item, saveListener);
    }

    //endregion

}

// TODO SET TEMPLATE