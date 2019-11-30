package gr.nightwall.deliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.activities.business_management.BusinessManagementActivity;
import gr.nightwall.deliveryapp.utils.Navigation;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init(){
        findViewById(R.id.btnProfileBack).setOnClickListener(v -> onBackPressed());

        setUpProfile();
    }

    private void setUpProfile(){
        LinearLayout lProfileNavigation = findViewById(R.id.lProfileNavigation);

        // Manage shop
        ViewGroup btnManageShop = (ViewGroup) lProfileNavigation.getChildAt(0);
        setUpProfileNavigationItem(btnManageShop,
                R.drawable.ic_manager_24,
                getString(R.string.manage_shop),
                getString(R.string.manage_shop_description));
        btnManageShop.setOnClickListener(v -> manageShop());

        // Edit profile
        ViewGroup btnEditProfile = (ViewGroup) lProfileNavigation.getChildAt(1);
        setUpProfileNavigationItem(btnEditProfile,
                R.drawable.ic_edit_24,
                getString(R.string.edit_profile),
                getString(R.string.edit_profile_description));
        btnEditProfile.setOnClickListener(v -> editProfile());

        // Order history
        ViewGroup btnOrderHistory = (ViewGroup) lProfileNavigation.getChildAt(2);
        setUpProfileNavigationItem(btnOrderHistory,
                R.drawable.ic_my_orders,
                getString(R.string.order_history),
                getString(R.string.order_history_description));
        btnOrderHistory.setOnClickListener(v -> viewOrderHistory());

    }

    private void setUpProfileNavigationItem(ViewGroup item, int iconRes, String title, String description){
        AppCompatImageView ivIcon = item.findViewById(R.id.ivProfileNavIcon);
        ivIcon.setImageResource(iconRes);

        TextView tvTitle = item.findViewById(R.id.tvProfileNavTitle);
        tvTitle.setText(title);

        TextView tvDescription = item.findViewById(R.id.tvProfileNavDescription);
        tvDescription.setText(description);
    }

    // ACTIONS
    private void manageShop(){
        Navigation.startActivity(BusinessManagementActivity.class);
    }

    private void editProfile(){

    }

    private void viewOrderHistory(){

    }
}
