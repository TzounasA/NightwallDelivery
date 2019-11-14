package gr.nightwall.deliveryapp.activities.business_management;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.IngredientCategoriesAdapter;
import gr.nightwall.deliveryapp.adapters.ItemTemplatesAdapter;
import gr.nightwall.deliveryapp.database.BusinessManagementDB;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.Phone;
import gr.nightwall.deliveryapp.models.Time;
import gr.nightwall.deliveryapp.models.shop.IngredientCategory;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.CustomDialog;
import gr.nightwall.deliveryapp.views.SettingsLineInput;
import gr.nightwall.deliveryapp.views.SettingsLineSwitch;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA;
import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA_2;

public class EditItemTemplateActivity extends AppCompatActivity {
    private static final int ACTIVITY_RESULT = 1;

    // Data
    private ItemTemplate template;

    //Views
    private ViewGroup lineTemplateName;

    private ViewGroup savingIndicator;

    private IngredientCategoriesAdapter adapter;

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

            template = Utils.fromJson(templateJson, ItemTemplate.class);
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

        if (template.getName().isEmpty()){
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
        setupIngredientCategories();
    }

    private void setupName() {
        lineTemplateName = findViewById(R.id.lineTemplateName);
        new SettingsLineInput(lineTemplateName)
                .hint(getString(R.string.title))
                .prefill(template.getName())
                .iconRes(R.drawable.ic_category_24);
    }

    private void setupIngredientCategories(){
        adapter = new IngredientCategoriesAdapter(this, template.getIngredientsCategories());
        adapter.setOnItemClick(new OnSettingClickListener() {
            @Override
            public void onActionClick(int index) {
                openIngredientCategory(template.getIngredientCategoryAt(index));
            }

            @Override
            public void onDeleteClick(int index) {
                onDeleteIngredientCategory(index);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvIngredientCategories);
        Utils.initRecyclerView(this, recyclerView, adapter);
    }

    //endregion

    //region ACTIONS

    private void openIngredientCategory(IngredientCategory ingredientCategory){
        Intent intent = new Intent(this, EditIngredientCategoryActivity.class);
        intent.putExtra(ITEM_EXTRA, Utils.toJson(template));
        intent.putExtra(ITEM_EXTRA_2, Utils.toJson(ingredientCategory));

        startActivityForResult(intent, ACTIVITY_RESULT);
    }

    public void addNewIngredientCategory(View v){
        String priority = template.getPriorityForNewIngredientCategory();
        IngredientCategory ingredientCategory = new IngredientCategory(priority);

        openIngredientCategory(ingredientCategory);
    }

    private void onDeleteIngredientCategory(int index) {
        LinearLayout dialogHolder = findViewById(R.id.dialogHolder);

        new CustomDialog(this, CustomDialog.DialogType.TEXT, dialogHolder)
                .setTitle(this, R.string.delete_ingredient_category_title)
                .setText(this, R.string.delete_ingredient_category_body)
                .setBtnPositiveText(getString(R.string.delete))
                .setBtnNegativeText(getString(R.string.cancel))
                .setOnPositiveClickListener(dialog -> deleteIngredientCategory(index))
                .setPositiveTextColorResource(getResources().getColor(R.color.colorError))
                .setPositiveRippleColorResource(R.color.colorError)
                .build(this)
                .show();
    }

    private void deleteIngredientCategory(int index) {
        adapter.removeItemAt(index);
        saveChanges();
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
