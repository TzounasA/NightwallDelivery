package gr.nightwall.deliveryapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.utils.Navigation;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //region INITIALIZATION

    private void init() {
        initNavigation();
    }

    private void initNavigation() {
        View topBar = findViewById(R.id.topBar);

        topBar.findViewById(R.id.btnProfile)
                .setOnClickListener(view -> Navigation.startActivity(ProfileActivity.class));
    }

    //endregion


}
