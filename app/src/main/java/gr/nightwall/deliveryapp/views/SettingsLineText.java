package gr.nightwall.deliveryapp.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import gr.nightwall.deliveryapp.R;

public class SettingsLineText extends SettingsLine {

    private ViewGroup parentView;

    private TextView tvTitle, tvSecondary;
    private MaterialButton btnSecondary;

    public SettingsLineText(ViewGroup parentView) {
        super(parentView);

        this.parentView = parentView;

        tvTitle = parentView.findViewById(R.id.tvTitle);
        tvSecondary = parentView.findViewById(R.id.tvSecondary);
        btnSecondary = parentView.findViewById(R.id.ivSecondIcon);

        tvSecondary.setVisibility(View.INVISIBLE);
        btnSecondary.setVisibility(View.GONE);
    }

    public SettingsLineText title(String title){
        if (tvTitle == null)
            return this;

        tvTitle.setText(title);
        return this;
    }

    public SettingsLineText secondaryText(String secondaryText){
        if (tvSecondary == null)
            return this;

        tvSecondary.setVisibility(View.VISIBLE);
        tvSecondary.setText(secondaryText);

        return this;
    }

    public SettingsLineText secondaryIconRes(int secondaryIconRes){
        if (btnSecondary == null)
            return this;

        btnSecondary.setVisibility(View.VISIBLE);
        btnSecondary.setIconResource(secondaryIconRes);

        return this;
    }

    public SettingsLineText mainClickAction(View.OnClickListener mainClickListener){
        parentView.setOnClickListener(mainClickListener);

        return this;
    }

    public SettingsLineText secondaryClickAction(View.OnClickListener secondaryClickListener){
        btnSecondary.setOnClickListener(secondaryClickListener);

        return this;
    }

}
