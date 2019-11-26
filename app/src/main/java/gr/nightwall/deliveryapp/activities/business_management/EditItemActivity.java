package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.ItemIngredientsAdapter;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnGetListListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.interfaces.OnIngredientsListener;
import gr.nightwall.deliveryapp.models.shop.Ingredient;
import gr.nightwall.deliveryapp.models.shop.Item;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.SettingsLineInput;

public class EditItemActivity extends AppCompatActivity {

    // Data
    private Item item;
    private ArrayList<ItemTemplate> templates;

    //Views
    private ViewGroup linePriority, lineName, lineDescription, lineStartPrice;
    private AppCompatSpinner spinnerTemplate;

    private ViewGroup savingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        getExtra();
        init();
        loadTemplates();
    }

    private void getExtra(){
        String itemJson = getIntent().getStringExtra(Consts.ITEM_EXTRA);

        item = Utils.fromJson(itemJson, Item.class);

        if (item == null){
            Navigation.errorToast(this);
            finish();
        }
    }

    private void loadTemplates() {
        savingIndicator.setVisibility(View.VISIBLE);

        OnGetListListener getDataListener = new OnGetListListener() {
            @Override
            public <T> void onSuccess(ArrayList<T> list) {
                templates = new ArrayList<>();

                for (T item : list) {
                    ItemTemplate itemTemplate = (ItemTemplate) item;

                    if (itemTemplate != null){
                        templates.add(itemTemplate);
                    }
                }

                savingIndicator.setVisibility(View.GONE);
                setupScreen();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(EditItemActivity.this);
            }
        };

        MenuDB.getAllItemTemplates(getDataListener);
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
        setupTemplateSpinner();

        print();
    }

    private void setupTemplateSpinner() {
        // Set Names
        String[] templateNames = getTemplateNames();
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, templateNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Change template listener
        spinnerTemplate = findViewById(R.id.spinnerTemplate);
        spinnerTemplate.setAdapter(adapter);
        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onTemplateSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String[] getTemplateNames(){
        String[] names = new String[templates.size()];

        for (int i = 0; i < templates.size(); i++) {
            names[i] = templates.get(i).getName();
        }

        return names;
    }

    private int getTemplateIndex(){
        String templateId = item.getTemplateId();

        if (templateId == null || templateId.isEmpty()){
            return -1;
        }

        for (int i = 0; i < templates.size(); i++) {
            if (templates.get(i) == null){
                return -1;
            }

            if (templates.get(i).getId().equals(templateId)){
                return i;
            }
        }

        return -1;
    }

    // PRINT
    private void print(){
        printInputs();
        printCurrentTemplate();
    }

    private void printInputs() {
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
                .hint(getString(R.string.start_price))
                .prefill(item.getStartPrice() + "")
                .helperText(getString(R.string.dot_for_comma))
                .iconRes(R.drawable.ic_euro);
    }

    private void printCurrentTemplate(){
        int curTemplateIndex = getTemplateIndex();
        if (curTemplateIndex != -1){
            spinnerTemplate.setSelection(curTemplateIndex);
        }
    }

    private void printIngredientsPrice(){
        TextView tvIngredientsPrice = findViewById(R.id.tvIngredientsPrice);
        tvIngredientsPrice.setText(item.getIngredientsPriceString());
    }

    private void printTemplateIngredients(ItemTemplate template){
        ItemIngredientsAdapter adapter = new ItemIngredientsAdapter(this,
                template.getIngredientsCategories(), item.getSelectedIngredientsIDs());

        adapter.setOnIngredientsListener(new OnIngredientsListener() {
            @Override
            public void onAddIngredient(String ingredientCategoryName, Ingredient ingredient) {
                item.addIngredient(ingredientCategoryName, ingredient);
                printIngredientsPrice();
            }

            @Override
            public void onRemoveIngredient(String ingredientCategoryName, Ingredient ingredient) {
                item.removeIngredient(ingredientCategoryName, ingredient);
                printIngredientsPrice();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvSelectedIngredients);
        Utils.initRecyclerView(this, recyclerView, adapter);
    }

    //endregion

    //region ACTIONS

    private void onTemplateSelected(int index) {
        if (index >= templates.size()){
            Navigation.errorToast(this);
            return;
        }

        ItemTemplate template = templates.get(index);

        if (!item.getTemplateId().equals(template.getId())){
            item.setTemplateId(template.getId());
            item.clearIngredients();
        }

        printIngredientsPrice();
        printTemplateIngredients(template);
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
                print();
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
