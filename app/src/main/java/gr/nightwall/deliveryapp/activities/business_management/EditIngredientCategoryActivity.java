package gr.nightwall.deliveryapp.activities.business_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.IngredientCategoriesAdapter;
import gr.nightwall.deliveryapp.adapters.IngredientsAdapter;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.interfaces.OnActionListener;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Ingredient;
import gr.nightwall.deliveryapp.models.shop.IngredientCategory;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.CustomDialog;
import gr.nightwall.deliveryapp.views.SettingsLineInput;

import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA;
import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA_2;
import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA_3;
import static gr.nightwall.deliveryapp.utils.Consts.ITEM_INT_EXTRA;

public class EditIngredientCategoryActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT = 2;

    // Data
    private ItemTemplate template;
    private IngredientCategory category;

    //Views
    private ViewGroup linePriority, lineName, lineDescription;
    private AppCompatCheckBox cbRequired;
    private AppCompatRadioButton rbSingleOption, rbMultipleOptions;

    private ViewGroup savingIndicator;

    private IngredientsAdapter adapter;

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
        String categoryJson = getIntent().getStringExtra(ITEM_EXTRA_2);

        template = Utils.fromJson(templateJson, ItemTemplate.class);
        category = Utils.fromJson(categoryJson, IngredientCategory.class);
        if (template == null || category == null){
            Navigation.errorToast(this);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == ACTIVITY_RESULT) {
            if (data == null)
                return;

            String templateJson = data.getStringExtra(ITEM_EXTRA);
            if (templateJson == null)
                return;

            String categoryJson = data.getStringExtra(ITEM_EXTRA_2);
            if (categoryJson == null)
                return;

            category = Utils.fromJson(categoryJson, IngredientCategory.class);
            setupScreen();
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
                .prefill(category.getDescription())
                .iconRes(R.drawable.id_description);

        // Options
        rbSingleOption = findViewById(R.id.rbSingleOption);
        rbMultipleOptions = findViewById(R.id.rbMultipleOptions);

        if (category.getOptions() == IngredientCategory.Options.SINGLE)
            rbSingleOption.setChecked(true);
        else
            rbMultipleOptions.setChecked(true);
    }

    private void setupIngredients() {
        adapter = new IngredientsAdapter(this, category.getIngredients());
        adapter.setOnItemClick(new OnSettingClickListener() {
            @Override
            public void onActionClick(int index) {
                openIngredient(index, category.getIngredientAt(index));
            }

            @Override
            public void onDeleteClick(int index) {
                onDeleteIngredient(index);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvIngredients);
        Utils.initRecyclerView(this, recyclerView, adapter);
    }

    //endregion

    //region ACTIONS

    public void openIngredient(int index, Ingredient ingredient){
        Intent intent = new Intent(this, EditIngredientActivity.class);

        intent.putExtra(ITEM_EXTRA, Utils.toJson(template));
        intent.putExtra(ITEM_EXTRA_2, Utils.toJson(category));
        intent.putExtra(ITEM_EXTRA_3, Utils.toJson(ingredient));
        intent.putExtra(ITEM_INT_EXTRA, index);

        startActivityForResult(intent, ACTIVITY_RESULT);
    }

    public void addNewIngredient(View view) {
        openIngredient(-1, new Ingredient());
    }

    private void onDeleteIngredient(int index) {
        LinearLayout dialogHolder = findViewById(R.id.dialogHolder);

        new CustomDialog(this, CustomDialog.DialogType.TEXT, dialogHolder)
                .setTitle(this, R.string.delete_ingredient_title)
                .setText(this, R.string.delete_ingredient_body)
                .setBtnPositiveText(getString(R.string.delete))
                .setBtnNegativeText(getString(R.string.cancel))
                .setOnPositiveClickListener(dialog -> deleteIngredient(index))
                .setPositiveTextColorResource(getResources().getColor(R.color.colorError))
                .setPositiveRippleColorResource(R.color.colorError)
                .build(this)
                .show();
    }

    private void deleteIngredient(int index) {
        adapter.removeItemAt(index);
    }

    //endregion

    //region SAVE CHANGES

    private void saveChangesFromInputs(){
        category.setPriorityNumber(Utils.getTextFromSetting(linePriority));
        category.setRequired(cbRequired.isChecked());

        category.setName(Utils.getTextFromSetting(lineName));
        category.setDescription(Utils.getTextFromSetting(lineDescription));

        if (rbSingleOption.isChecked()){
            category.setOptions(IngredientCategory.Options.SINGLE);
        }else{
            category.setOptions(IngredientCategory.Options.MULTIPLE);
        }

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
