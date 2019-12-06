package gr.nightwall.deliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.CategoriesAdapter;
import gr.nightwall.deliveryapp.adapters.ItemsAdapter;
import gr.nightwall.deliveryapp.database.CartManager;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnGetListListener;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Category;
import gr.nightwall.deliveryapp.models.shop.Item;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;

import static gr.nightwall.deliveryapp.adapters.CategoriesAdapter.CategoriesViewType.SHOP_BIG;
import static gr.nightwall.deliveryapp.adapters.CategoriesAdapter.CategoriesViewType.SHOP_SMALL;

public class MainActivity extends AppCompatActivity {


    // Data
    private ArrayList<Category> categories;
    private ArrayList<Item> allItems;
    private HashMap<String, ArrayList<Item>> items;
    private CartManager cartManager = new CartManager();

    // Views
    private ViewGroup savingIndicator;
    private TextView tvCategoryDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //region INITIALIZATION

    private void init() {
        initScreen();
        initNavigation();
        loadCategories();
    }

    private void initScreen(){
        tvCategoryDescription = findViewById(R.id.tvCategoryDescription);
    }

    private void initNavigation() {
        savingIndicator = findViewById(R.id.savingIndicator);
        savingIndicator.setVisibility(View.GONE);

        View topBar = findViewById(R.id.topBar);

        topBar.findViewById(R.id.btnBack).setVisibility(View.INVISIBLE);
        topBar.findViewById(R.id.btnProfile)
                .setOnClickListener(view -> Navigation.startActivity(ProfileActivity.class));

        TextView tvScreenTitle = topBar.findViewById(R.id.tvScreenTitle);
        tvScreenTitle.setText(getString(R.string.app_full_name));

        findViewById(R.id.lCart)
                .setOnClickListener(view -> Navigation.startActivity(CartActivity.class));
    }

    private void loadCategories(){
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

                loadItems();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(MainActivity.this);
            }
        };

        MenuDB.getAllCategories(getDataListener);
    }

    private void loadItems(){
        OnGetListListener getListListener = new OnGetListListener() {
            @Override
            public <T> void onSuccess(ArrayList<T> list) {
                allItems = new ArrayList<>();

                for (T itemT : list) {
                    Item item = (Item) itemT;

                    if (item != null){
                        allItems.add(item);
                    }
                }

                sortItems();
            }

            @Override
            public void onFail() {

            }
        };

        MenuDB.getAllItems(getListListener);
    }

    private void sortItems(){
        items = new HashMap<>();
        for (Item item : allItems) {
            String categoryId = item.getCategoryId();

            if(!items.containsKey(categoryId)){
                items.put(categoryId, new ArrayList<>());
            }

            items.get(categoryId).add(item);
        }

        savingIndicator.setVisibility(View.GONE);
        printCategories();
    }

    //endregion

    //region PRINT

    private void printCategories() {
        CategoriesAdapter adapter = new CategoriesAdapter(this, categories, SHOP_SMALL);
        adapter.setOnItemClick(new OnSettingClickListener() {
            @Override
            public void onActionClick(int index) {
                printCategory(categories.get(index));
            }

            @Override
            public void onDeleteClick(int index) { }
        });

        RecyclerView recyclerView = findViewById(R.id.rvCategories);
        Utils.initHorizontalRecyclerView(this, recyclerView, adapter);

        adapter.selectCategoryAt(0);
        printCategory(categories.get(0));
    }

    private void printCategory(Category category) {
        if(category == null || category.getId() == null)
            return;

        // Description
        String categoryDescription = category.getDescription();
        if(categoryDescription == null || categoryDescription.isEmpty()){
            tvCategoryDescription.setVisibility(View.GONE);
        }else {
            tvCategoryDescription.setVisibility(View.VISIBLE);
            tvCategoryDescription.setText(categoryDescription);
        }

        // Items
        printItems(Utils.sortItems(items.get(category.getId())));
    }

    private void printItems(ArrayList<Item> items){
        ItemsAdapter adapter = new ItemsAdapter(this, items, true);
        adapter.setOnItemClick(new OnSettingClickListener() {
            @Override
            public void onActionClick(int index) {
                openItem(items.get(index));
            }

            @Override
            public void onDeleteClick(int index) { }
        });

        RecyclerView recyclerView = findViewById(R.id.rvItems);
        Utils.initRecyclerView(this, recyclerView, adapter);
    }

    private void openItem(Item item){
        Navigation.startActivity(BuyItemDialog.class, Utils.toJson(item));
    }

    private void updateCart() {
        cartManager.update();
        if (cartManager.isEmpty()){
            findViewById(R.id.lCart).setVisibility(View.GONE);
            return;
        }

        findViewById(R.id.lCart).setVisibility(View.VISIBLE);

        TextView tvCartItems = findViewById(R.id.tvCartItems);
        tvCartItems.setText(cartManager.getItemsString());

        TextView tvCartPrice = findViewById(R.id.tvCartPrice);
        tvCartPrice.setText(cartManager.getTotalPriceString());
    }

    //endregion

}
