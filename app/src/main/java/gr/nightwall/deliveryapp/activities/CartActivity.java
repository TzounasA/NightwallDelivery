package gr.nightwall.deliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.adapters.CartItemsAdapter;
import gr.nightwall.deliveryapp.database.BusinessManagementDB;
import gr.nightwall.deliveryapp.database.CartManager;
import gr.nightwall.deliveryapp.database.interfaces.OnGetItemListener;
import gr.nightwall.deliveryapp.interfaces.OnListChangedListener;
import gr.nightwall.deliveryapp.models.shop.Item;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.utils.Utils;

public class CartActivity extends AppCompatActivity {

    // Data
    private CartManager cartManager;
    private ArrayList<Item> itemsInCart;
    private double minPrice;

    // Views
    private ViewGroup savingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
        loadMinPrice();
    }

    private void init() {
        savingIndicator = findViewById(R.id.savingIndicator);
        savingIndicator.setVisibility(View.GONE);

        cartManager = new CartManager();
        itemsInCart = cartManager.getItems();

        findViewById(R.id.btnCartBack).setOnClickListener(v -> onBackPressed());

    }

    private void loadMinPrice(){
        savingIndicator.setVisibility(View.VISIBLE);

        OnGetItemListener getDataListener = new OnGetItemListener() {
            @Override
            public <T> void onSuccess(T item) {
                Double min = (Double) item;

                if (min == null || min < 0){
                    savingIndicator.setVisibility(View.GONE);
                    Navigation.errorToast(CartActivity.this);
                    return;
                }

                minPrice = min;
                savingIndicator.setVisibility(View.GONE);
                setup();
            }

            @Override
            public void onFail() {
                savingIndicator.setVisibility(View.GONE);
                Navigation.errorToast(CartActivity.this);
            }
        };

        BusinessManagementDB.getMinimumOrderPrice(getDataListener);
    }

    private void setup(){
        setUpContinueButton();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        CartItemsAdapter adapter = new CartItemsAdapter(this, itemsInCart, true);
        adapter.setOnListChangedListener(this::onListChanged);

        RecyclerView rvCartItems = findViewById(R.id.rvCartItems);
        Utils.initRecyclerView(this, rvCartItems, adapter);
    }

    private void onListChanged(){
        cartManager.save();

        if(cartManager.getItemsCount() <= 0){
            finish();
        }

        setUpContinueButton();
    }

    private void setUpContinueButton() {
        TextView tvCartPrice = findViewById(R.id.tvCartPrice);
        tvCartPrice.setText(cartManager.getTotalPriceString());

        if (cartManager.getTotalPrice() >= minPrice){
            setupCanContinue();
        } else {
            setupCannotContinue();
        }
    }

    private void setupCanContinue() {
        // Items
        String itemsFormat = getString(R.string.total_x_items);
        String items = String.format(Locale.getDefault(), itemsFormat, cartManager.getItemsCount());

        TextView tvContinueTitle = findViewById(R.id.tvContinueTitle);
        tvContinueTitle.setText(items);

        // Continue
        findViewById(R.id.tvContinueBody).setAlpha(1f);

        // Click
        findViewById(R.id.lContinueOrder).setOnClickListener(view -> continueOrder());
    }

    private void setupCannotContinue() {
        // Money remaining
        double remains = minPrice - cartManager.getTotalPrice();
        String remainsFormat = getString(R.string.x_to_min_order_price);
        String remain = String.format(Locale.getDefault(), remainsFormat, remains);

        TextView tvContinueTitle = findViewById(R.id.tvContinueTitle);
        tvContinueTitle.setText(remain);

        // Continue
        findViewById(R.id.tvContinueBody).setAlpha(0.3f);

        // Click
        findViewById(R.id.lContinueOrder).setOnClickListener(view -> {/* Do nothing */});
    }

    private void continueOrder() {

    }


}
