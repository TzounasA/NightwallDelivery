package gr.nightwall.deliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.ItemIngredientsAdapter;
import gr.nightwall.deliveryapp.database.CartManager;
import gr.nightwall.deliveryapp.database.MenuDB;
import gr.nightwall.deliveryapp.database.interfaces.OnGetItemListener;
import gr.nightwall.deliveryapp.interfaces.OnIngredientsListener;
import gr.nightwall.deliveryapp.models.shop.Ingredient;
import gr.nightwall.deliveryapp.models.shop.Item;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.utils.Consts;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;

public class BuyItemDialog extends AppCompatActivity {

    // Data
    private Item item;
    private ItemTemplate template;

    //Views
    private Button btnToCart;
    private TextView tvItemPrice;

    private ViewGroup savingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item_dialog);

        getExtra();
        init();
        loadTemplate();
    }

    //region INITIALIZATION

    private void getExtra(){
        String itemJson = getIntent().getStringExtra(Consts.ITEM_EXTRA);

        item = Utils.fromJson(itemJson, Item.class);

        if (item == null){
            Navigation.errorToast(this);
            finish();
        }
    }

    private void loadTemplate() {
        if (item.getTemplateId() == null || item.getTemplateId().isEmpty())
            return;

        savingIndicator.setVisibility(View.VISIBLE);

        OnGetItemListener getDataListener = new OnGetItemListener() {
            @Override
            public <T> void onSuccess(T item) {
                template = (ItemTemplate) item;

                if (template == null){
                    savingIndicator.setVisibility(View.GONE);
                    Navigation.errorToast(BuyItemDialog.this);
                    closeDialog();
                    return;
                }

                savingIndicator.setVisibility(View.GONE);
                printOptions();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(BuyItemDialog.this);
                closeDialog();
            }
        };

        MenuDB.getItemTemplateByID(item.getTemplateId(), getDataListener);
    }

    private void init() {
        initScreenViews();

        item.setQuantity(1);

        printItem();
        updateCartText();
    }

    private void initScreenViews(){
        // Loader
        savingIndicator = findViewById(R.id.savingIndicator);
        savingIndicator.setVisibility(View.GONE);

        // Cart Button
        btnToCart = findViewById(R.id.btnToCart);
        btnToCart.setOnClickListener(view -> addToCart());

        // Quantity Picker
        ScrollableNumberPicker quantityPicker = findViewById(R.id.quantityPicker);
        quantityPicker.setListener(value -> {
            item.setQuantity(value);
            updateCartText();
        });
    }

    //endregion

    //region PRINT

    private void printItem(){
        // Title
        TextView tvTitle = findViewById(R.id.tvItemTitle);
        tvTitle.setText(item.getName());

        // Description
        TextView tvDescription = findViewById(R.id.tvItemDescription);
        String description = item.getDescription();

        if (description == null || description.isEmpty())
            tvDescription.setVisibility(View.GONE);
        else
            tvDescription.setText(description);

        // Price
        tvItemPrice = findViewById(R.id.tvItemPrice);
    }

    private void updateItemPrice() {
        tvItemPrice.setText(item.getFinalPriceString());
    }

    private void updateCartText() {
        String format = getString(R.string.add_to_cart_text);
        String btnText = String.format(format, item.getTotalItemsFormatted());

        btnToCart.setText(btnText);
    }

    private void printOptions() {
        ItemIngredientsAdapter adapter = new ItemIngredientsAdapter(this,
                template.getIngredientsCategories(), item.getSelectedIngredientsIDs());

        adapter.setOnIngredientsListener(new OnIngredientsListener() {
            @Override
            public void onAddIngredient(String ingredientCategoryName, Ingredient ingredient) {
                item.addIngredient(ingredientCategoryName, ingredient);

                updateItemPrice();
                updateCartText();
            }

            @Override
            public void onRemoveIngredient(String ingredientCategoryName, Ingredient ingredient) {
                item.removeIngredient(ingredientCategoryName, ingredient);

                updateItemPrice();
                updateCartText();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvOptions);
        Utils.initRecyclerView(this, recyclerView, adapter);
    }

    //endregion

    //region ACTIONS

    private void addToCart() {
        new CartManager().addItem(item);
        closeDialog();
    }

    public void onClose(View view) {
        closeDialog();
    }

    public void closeDialog(){
        finish();
    }

    //endregion

}
