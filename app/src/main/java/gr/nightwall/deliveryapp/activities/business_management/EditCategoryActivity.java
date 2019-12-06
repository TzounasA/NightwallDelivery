package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.ItemsAdapter;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnGetListListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Category;
import gr.nightwall.deliveryapp.models.shop.Item;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.CustomDialog;
import gr.nightwall.deliveryapp.views.SettingsLineInput;
import gr.nightwall.deliveryapp.views.SettingsLineSwitch;

public class EditCategoryActivity extends AppCompatActivity {

    // Data
    private Category category;
    private ArrayList<Item> items;

    // Views
    private ViewGroup linePriority, lineName, lineDescription;

    private ViewGroup savingIndicator;

    private ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        getExtra();
        init();
        setupScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadItems();
    }

    private void getExtra() {
        String categoryJson = getIntent().getStringExtra(Consts.ITEM_EXTRA);

        category = Utils.fromJson(categoryJson, Category.class);
        if (category == null) {
            Navigation.errorToast(this);
            finish();
        }
    }

    private void loadItems() {
        savingIndicator.setVisibility(View.VISIBLE);

        OnGetListListener getDataListener = new OnGetListListener() {
            @Override
            public <T> void onSuccess(ArrayList<T> list) {
                items = new ArrayList<>();

                for (T item : list) {
                    Item newItem = (Item) item;

                    if (newItem != null) {
                        items.add(newItem);
                    }
                }

                savingIndicator.setVisibility(View.GONE);
                setupItems();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(EditCategoryActivity.this);
            }
        };

        MenuDB.getItemsOfCategory(category.getId(), getDataListener);
    }

    //region INITIALIZATION

    private void init() {
        initToolbar();
        initScreen();
        initActionButtons();
    }

    private void initToolbar() {
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            return;
        }

        bar.setDisplayHomeAsUpEnabled(true);

        if (category.getName().isEmpty()) {
            bar.setTitle(getString(R.string.add_category));
        }
    }

    private void initScreen() {
        savingIndicator = findViewById(R.id.savingIndicator);
        savingIndicator.setVisibility(View.GONE);
    }

    private void initActionButtons() {
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

    private void setupScreen() {
        setupInputs();
    }

    private void setupInputs() {
        // Auto operation
        int textRes = category.isVisible() ? R.string.category_is_visible_body_on
                : R.string.category_is_visible_body_off;

        SettingsLineSwitch liveLine = new SettingsLineSwitch(findViewById(R.id.lineCategoryVisibility))
                .title(getString(R.string.category_is_visible_title))
                .secondaryText(getString(textRes))
                .checked(category.isVisible());

        liveLine.switchCheckedChangeListener((sw, checked) -> {
            liveLine.secondaryText(getString(textRes));

            setCategoryVisible(checked);
        });

        // Priority
        linePriority = findViewById(R.id.lineCategoryPriority);
        new SettingsLineInput(linePriority)
                .hint(getString(R.string.priority))
                .prefill(category.getPriorityNumber())
                .inputType(InputType.TYPE_CLASS_NUMBER);

        // Name
        lineName = findViewById(R.id.lineCategoryName);
        new SettingsLineInput(lineName)
                .hint(getString(R.string.title))
                .prefill(category.getName())
                .iconRes(R.drawable.ic_category_24);

        // Description
        lineDescription = findViewById(R.id.lineCategoryDescription);
        new SettingsLineInput(lineDescription)
                .hint(getString(R.string.description))
                .prefill(category.getDescription())
                .iconRes(R.drawable.id_description);
    }

    private void setupItems() {
        adapter = new ItemsAdapter(this, Utils.sortItems(items), false);
        adapter.setOnItemClick(new OnSettingClickListener() {
            @Override
            public void onActionClick(int index) {
                openItem(items.get(index));
            }

            @Override
            public void onDeleteClick(int index) {
                onDeleteItem(index);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvItems);
        Utils.initRecyclerView(this, recyclerView, adapter);
    }

    //endregion

    //region ACTIONS

    private void openItem(Item item) {
        Navigation.startActivity(EditItemActivity.class, Utils.toJson(item));
    }

    public void addNewItem(View view) {
        String priority = Utils.getPriorityFromListCount(items.size());
        openItem(new Item(category.getId(), priority));
    }

    private void onDeleteItem(int index) {
        LinearLayout dialogHolder = findViewById(R.id.dialogHolder);

        new CustomDialog(this, CustomDialog.DialogType.TEXT, dialogHolder)
                .setTitle(this, R.string.delete_item_title)
                .setText(this, R.string.delete_item_body)
                .setBtnPositiveText(getString(R.string.delete))
                .setBtnNegativeText(getString(R.string.cancel))
                .setOnPositiveClickListener(dialog -> deleteItem(index))
                .setPositiveTextColorResource(getResources().getColor(R.color.colorError))
                .setPositiveRippleColorResource(R.color.colorError)
                .build(this)
                .show();
    }

    private void deleteItem(int index) {
        savingIndicator.setVisibility(View.VISIBLE);

        OnSaveDataListener saveListener = new OnSaveDataListener() {
            @Override
            public void onSuccess() {
                savingIndicator.setVisibility(View.GONE);
                Toast.makeText(EditCategoryActivity.this,
                        getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();

                adapter.removeItemAt(index);
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(EditCategoryActivity.this);
            }
        };

        MenuDB.deleteItem(items.get(index), saveListener);
    }

    private void setCategoryVisible(boolean isVisible) {
        category.setVisible(isVisible);
        saveChanges();
    }

    //endregion

    //region SAVE CHANGES

    private void saveChangesFromInputs() {
        category.setPriorityNumber(Utils.getTextFromSetting(linePriority));

        category.setName(Utils.getTextFromSetting(lineName));
        category.setDescription(Utils.getTextFromSetting(lineDescription));

        saveChanges();
    }

    private void saveChanges() {
        savingIndicator.setVisibility(View.VISIBLE);

        OnSaveDataListener saveListener = new OnSaveDataListener() {
            @Override
            public void onSuccess() {
                savingIndicator.setVisibility(View.GONE);
                setupScreen();
                Toast.makeText(EditCategoryActivity.this,
                        getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(EditCategoryActivity.this);
            }
        };

        MenuDB.saveCategory(category, saveListener);
    }

    //endregion

}
