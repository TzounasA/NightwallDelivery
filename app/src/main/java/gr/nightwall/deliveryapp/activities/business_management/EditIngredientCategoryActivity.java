package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.IngredientCategoriesAdapter;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.models.shop.IngredientCategory;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.SettingsLineInput;

import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA;

public class EditIngredientCategoryActivity extends AppCompatActivity {

    // Data
    private ItemTemplate template;
    private IngredientCategory category;

    //Views
    private ViewGroup linePriority, lineName, lineDescription;
    private AppCompatCheckBox cbRequired;
    private AppCompatRadioButton rbSingleOption, rbMultipleOptions;

    private ViewGroup savingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient_category);

        getExtra();
        init();
        setupScreen();
    }

    private void getExtra(){
        String templateJson = getIntent().getStringExtra(Consts.ITEM_EXTRA);
        String categoryJson = getIntent().getStringExtra(Consts.ITEM_EXTRA_2);

        template = Utils.fromJson(templateJson, ItemTemplate.class);
        category = Utils.fromJson(categoryJson, IngredientCategory.class);
        if (template == null || category == null){
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

        if (category.getName().isEmpty()){
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
        setupInputs();
        setupIngredients();
    }

    private void setupInputs() {
        // Priority
        linePriority = findViewById(R.id.lineIngredientCategoryPriority);
        new SettingsLineInput(linePriority)
                .hint(getString(R.string.priority))
                .prefill(category.getPriorityNumber())
                .inputType(InputType.TYPE_CLASS_NUMBER);

        // Required
        cbRequired = findViewById(R.id.cbRequired);
        cbRequired.setChecked(category.isRequired());

        // Name
        lineName = findViewById(R.id.lineIngredientCategoryName);
        new SettingsLineInput(lineName)
                .hint(getString(R.string.title))
                .prefill(category.getName())
                .iconRes(R.drawable.ic_category_24);

        // Description
        lineDescription = findViewById(R.id.lineIngredientCategoryDescription);
        new SettingsLineInput(lineDescription)
                .hint(getString(R.string.description))
                .prefill(category.getDescription());

        // Options
        rbSingleOption = findViewById(R.id.rbSingleOption);
        rbMultipleOptions = findViewById(R.id.rbMultipleOptions);

        if (category.getOptions() == IngredientCategory.Options.SINGLE)
            rbSingleOption.setChecked(true);
        else
            rbMultipleOptions.setChecked(true);
    }

    private void setupIngredients() {
        // TODO SETUP INGREDIENTS
    }

    //endregion

    //region SAVE CHANGES

    private void saveChangesFromInputs(){
        // TODO SAVE THE INPUTS

        saveChanges();
    }

    private void saveChanges() {
        template.saveIngredientCategory(category);

        savingIndicator.setVisibility(View.VISIBLE);

        OnSaveDataListener saveListener = new OnSaveDataListener() {
            @Override
            public void onSuccess() {
                savingIndicator.setVisibility(View.GONE);
                Toast.makeText(EditIngredientCategoryActivity.this,
                        getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();

                returnResult();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(EditIngredientCategoryActivity.this);
            }
        };

        MenuDB.saveItemTemplate(template, saveListener);
    }

    private void returnResult(){
        Intent intent = new Intent();
        intent.putExtra(ITEM_EXTRA, Utils.toJson(template));

        setResult(RESULT_OK, intent);
        finish();
    }

    //endregion

}
