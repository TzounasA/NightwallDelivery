package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.IngredientsAdapter;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Ingredient;
import gr.nightwall.deliveryapp.models.shop.IngredientCategory;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.SettingsLineInput;

import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA;
import static gr.nightwall.deliveryapp.utils.Consts.ITEM_EXTRA_2;

public class EditIngredientActivity extends AppCompatActivity {

    // Data
    private ItemTemplate template;
    private IngredientCategory category;
    private Ingredient ingredient;
    private int ingredientIndex;

    //Views
    private ViewGroup lineName, linePrice;

    private ViewGroup savingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);

        getExtra();
        init();
        setupScreen();
    }

    private void getExtra(){
        String templateJson = getIntent().getStringExtra(Consts.ITEM_EXTRA);
        String categoryJson = getIntent().getStringExtra(ITEM_EXTRA_2);
        String ingredientJson = getIntent().getStringExtra(Consts.ITEM_EXTRA_3);
        ingredientIndex = getIntent().getIntExtra(Consts.ITEM_INT_EXTRA, -1);

        template = Utils.fromJson(templateJson, ItemTemplate.class);
        category = Utils.fromJson(categoryJson, IngredientCategory.class);
        ingredient = Utils.fromJson(ingredientJson, Ingredient.class);

        if (template== null || category == null || ingredient == null){
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

        if (ingredient.getName().isEmpty()){
            bar.setTitle(getString(R.string.add_ingredient));
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
        // Name
        lineName = findViewById(R.id.lineIngredientName);
        new SettingsLineInput(lineName)
                .hint(getString(R.string.title))
                .prefill(ingredient.getName())
                .iconRes(R.drawable.ic_arrow_right_24);

        // Price
        linePrice = findViewById(R.id.lineIngredientPrice);
        new SettingsLineInput(linePrice)
                .hint(getString(R.string.price))
                .prefill(ingredient.getPrice() + "")
                .helperText(getString(R.string.dot_for_comma))
                .iconRes(R.drawable.ic_euro);
    }

    //endregion

    //region SAVE CHANGES

    private void saveChangesFromInputs(){
        ingredient.setName(Utils.getTextFromSetting(lineName));

        double price;
        try {
            price = Utils.getDoubleFromSetting(linePrice);
        } catch (NumberFormatException e) {
            new SettingsLineInput(linePrice)
                    .errorText(getString(R.string.error_number));
            return;
        }

        ingredient.setPrice(price);

        saveChanges();
    }

    private void saveChanges() {
        category.saveIngredient(ingredientIndex, ingredient);
        template.saveIngredientCategory(category);

        savingIndicator.setVisibility(View.VISIBLE);

        OnSaveDataListener saveListener = new OnSaveDataListener() {
            @Override
            public void onSuccess() {
                savingIndicator.setVisibility(View.GONE);
                Toast.makeText(EditIngredientActivity.this,
                        getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();

                returnResult();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(EditIngredientActivity.this);
            }
        };

        MenuDB.saveItemTemplate(template, saveListener);
    }

    private void returnResult(){
        Intent intent = new Intent();
        intent.putExtra(ITEM_EXTRA, Utils.toJson(template));
        intent.putExtra(ITEM_EXTRA_2, Utils.toJson(category));

        setResult(RESULT_OK, intent);
        finish();
    }

    //endregion

}
