package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.database.BusinessManagementDB;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.models.Phone;
import gr.nightwall.deliveryapp.models.Time;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.SettingsLineInput;
import gr.nightwall.deliveryapp.views.SettingsLineSwitch;

public class EditItemTemplateActivity extends AppCompatActivity {

    // Data
    private ItemTemplate template;

    //Views
    private ViewGroup lineTemplateName;

    private ViewGroup savingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_template);

        template = Utils.fromJson(getIntent().getStringExtra(Consts.ITEM_EXTRA), ItemTemplate.class);
        if (template == null){
            template = new ItemTemplate();
        }

        init();
        setupScreen();
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

        if (template == null){
            bar.setTitle(getString(R.string.add_item_template));
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
        setupName();
    }

    private void setupName() {
        lineTemplateName = findViewById(R.id.lineTemplateName);
        new SettingsLineInput(lineTemplateName)
                .hint(getString(R.string.title))
                .prefill(template.getName())
                .iconRes(R.drawable.ic_category_24);
    }

    //endregion

    //region SAVE CHANGES

    private void saveChangesFromInputs(){
        template.setName(getTextFromSetting(lineTemplateName));

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
                Toast.makeText(EditItemTemplateActivity.this,
                        getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(EditItemTemplateActivity.this);
            }
        };

        MenuDB.saveItemTemplate(template, saveListener);
    }

    //endregion

}
