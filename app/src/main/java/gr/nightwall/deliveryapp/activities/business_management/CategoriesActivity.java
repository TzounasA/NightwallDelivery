package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.CategoriesAdapter;
import gr.nightwall.deliveryapp.adapters.ItemTemplatesAdapter;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnGetListListener;
import gr.nightwall.deliveryapp.database.interfaces.OnSaveDataListener;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Category;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.views.CustomDialog;

import static gr.nightwall.deliveryapp.adapters.CategoriesAdapter.CategoriesViewType.EDIT;

public class CategoriesActivity extends AppCompatActivity {

    // Data
    private ArrayList<Category> categories;

    //Views
    private ViewGroup savingIndicator;

    private CategoriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        load();
    }

    private void load() {
        savingIndicator.setVisibility(View.VISIBLE);

        OnGetListListener getDataListener = new OnGetListListener() {
            @Override
            public <T> void onSuccess(ArrayList<T> list) {
                categories = new ArrayList<>();

                for (T item : list) {
                    Category category = (Category) item;

                    if (category != null){
                        categories.add(category);
                    }
                }

                savingIndicator.setVisibility(View.GONE);
                printList();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(CategoriesActivity.this);
            }
        };

        MenuDB.getAllCategories(getDataListener);
    }

    //region INITIALIZATION

    private void init() {
        initToolbar();
        initScreen();
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //endregion

    //region PRINT PAGE

    private void printList(){
        adapter = new CategoriesAdapter(this, categories, EDIT);
        adapter.setOnItemClick(new OnSettingClickListener() {
            @Override
            public void onActionClick(int index) {
                openCategory(categories.get(index));
            }

            @Override
            public void onDeleteClick(int index) {
                onDeleteCategory(index);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvCategories);
        Utils.initRecyclerView(this, recyclerView, adapter);
    }

    //endregion

    //region ACTIONS

    private void openCategory(Category category) {
        Navigation.startActivity(EditCategoryActivity.class, Utils.toJson(category));
    }

    public void onAddNewCategory(View view) {
        String priority = Utils.getPriorityFromListCount(categories.size());
        openCategory(new Category(priority));
    }

    private void onDeleteCategory(int index) {
        LinearLayout dialogHolder = findViewById(R.id.dialogHolder);

        new CustomDialog(this, CustomDialog.DialogType.TEXT, dialogHolder)
                .setTitle(this, R.string.delete_category_title)
                .setText(this, R.string.delete_category_body)
                .setBtnPositiveText(getString(R.string.delete))
                .setBtnNegativeText(getString(R.string.cancel))
                .setOnPositiveClickListener(dialog -> deleteCategory(index))
                .setPositiveTextColorResource(getResources().getColor(R.color.colorError))
                .setPositiveRippleColorResource(R.color.colorError)
                .build(this)
                .show();
    }

    private void deleteCategory(int index) {
        savingIndicator.setVisibility(View.VISIBLE);

        OnSaveDataListener saveListener = new OnSaveDataListener() {
            @Override
            public void onSuccess() {
                savingIndicator.setVisibility(View.GONE);
                Toast.makeText(CategoriesActivity.this,
                        getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();

                adapter.removeItemAt(index);
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(CategoriesActivity.this);
            }
        };

        MenuDB.deleteCategory(categories.get(index), saveListener);
    }

    //endregion

}
