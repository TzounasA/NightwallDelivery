package gr.nightwall.deliveryapp.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import gr.nightwall.deliveryapp.R;

public class SettingsLineSwitch extends SettingsLine {

    private TextView tvTitle, tvSecondary;
    private SwitchCompat switchSetting;

    public SettingsLineSwitch(ViewGroup parentView) {
        super(parentView);

        tvTitle = parentView.findViewById(R.id.tvTitle);
        tvSecondary = parentView.findViewById(R.id.tvSecondary);
        switchSetting = parentView.findViewById(R.id.switchSetting);

        tvSecondary.setVisibility(View.INVISIBLE);

        parentView.setOnClickListener(v -> switchSetting.toggle());
    }

    public SettingsLineSwitch title(String title){
        if (tvTitle == null)
            return this;

        tvTitle.setText(title);
        return this;
    }

    public SettingsLineSwitch secondaryText(String secondaryText){
        if (tvSecondary == null)
            return this;

        tvSecondary.setVisibility(View.VISIBLE);
        tvSecondary.setText(secondaryText);

        return this;
    }

    public SettingsLineSwitch checked(boolean checked){
        switchSetting.setChecked(checked);
        return this;
    }

    public SettingsLineSwitch switchCheckedChangeListener(CompoundButton.OnCheckedChangeListener checkedChangeListener){
        switchSetting.setOnCheckedChangeListener(checkedChangeListener);
        return this;
    }

}
