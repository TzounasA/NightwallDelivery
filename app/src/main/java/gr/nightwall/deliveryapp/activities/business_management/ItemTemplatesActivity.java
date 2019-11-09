package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.ItemTemplatesAdapter;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnGetItemListener;
import gr.nightwall.deliveryapp.database.interfaces.OnGetListListener;
import gr.nightwall.deliveryapp.models.management.BusinessSettings;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;

public class ItemTemplatesActivity extends AppCompatActivity {

    // Data
    private ArrayList<ItemTemplate> templates;

    //Views
    private ViewGroup savingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_templates);

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
                templates = new ArrayList<>();

                for (T item : list) {
                    ItemTemplate itemTemplate = (ItemTemplate) item;

                    if (itemTemplate != null){
                        templates.add(itemTemplate);
                    }
                }

                savingIndicator.setVisibility(View.GONE);
                printList();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(ItemTemplatesActivity.this);
            }
        };

        MenuDB.getAllItemTemplates(getDataListener);
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
        ItemTemplatesAdapter adapter = new ItemTemplatesAdapter(this, templates);
        adapter.setOnItemClick(this::openTemplate);

        RecyclerView recyclerView = findViewById(R.id.rvItemTemplates);
        Utils.initRecyclerView(this, recyclerView, adapter);
    }

    //endregion

    //region ACTIONS

    private void openTemplate(ItemTemplate template) {
        Navigation.startActivity(EditItemTemplateActivity.class, Utils.toJson(template));
    }

    public void onAddNewItemTemplate(View view) {
        Navigation.startActivity(EditItemTemplateActivity.class);
    }

    //endregion

}
