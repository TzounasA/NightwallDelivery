package gr.nightwall.deliveryapp.activities.business_management;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.utils.Navigation;
import gr.nightwall.deliveryapp.views.SettingsLine;
import gr.nightwall.deliveryapp.views.SettingsLineText;

public class BusinessManagementActivity extends AppCompatActivity {

    private LinearLayout lBusinessSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_management);

        init();
    }

    private void init(){
        lBusinessSettings = findViewById(R.id.lBusinessSettings);

        initToolbar();
        setUpButtons();
    }

    private void initToolbar() {
        ActionBar bar = getSupportActionBar();
        if (bar == null){
            return;
        }
        bar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpButtons() {
        // BUSINESS SETTINGS
        ViewGroup businessSettings = (ViewGroup) lBusinessSettings.getChildAt(0);

        new SettingsLineText(businessSettings)
                .title(getString(R.string.edit_business_settings_title))
                .secondaryText(getString(R.string.edit_business_settings_description))
                .mainClickAction(v -> editBusinessSettings())
                .iconRes(R.drawable.ic_manager_24);

        // TEMPLATES
        ViewGroup templates = (ViewGroup) lBusinessSettings.getChildAt(1);

        new SettingsLineText(templates)
                .title(getString(R.string.edit_templates_title))
                .secondaryText(getString(R.string.edit_templates_description))
                .mainClickAction(v -> editTemplates())
                .iconRes(R.drawable.ic_category_24);

        // MENU
        ViewGroup menu = (ViewGroup) lBusinessSettings.getChildAt(2);

        new SettingsLineText(menu)
                .title(getString(R.string.edit_menu_title))
                .secondaryText(getString(R.string.edit_menu_description))
                .mainClickAction(v -> editMenu())
                .iconRes(R.drawable.ic_menu_24);

    }

    // ACTIONS
    private void editBusinessSettings(){
        Navigation.startActivity(BusinessSettingsActivity.class);
    }

    private void editTemplates(){
        Navigation.startActivity(ItemTemplatesActivity.class);
    }

    private void editMenu(){

    }

}
