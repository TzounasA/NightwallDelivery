package gr.nightwall.deliveryapp.views;

import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import gr.nightwall.deliveryapp.R;

public class SettingsLineInput extends SettingsLine {

    private TextInputLayout inputLayout;
    private TextInputEditText input;

    public SettingsLineInput(ViewGroup parentView) {
        super(parentView);

        inputLayout = parentView.findViewById(R.id.inputLayout);
        input = parentView.findViewById(R.id.input);
    }

    public SettingsLineInput prefill(String prefill){
        input.setText(prefill);
        return this;
    }

    public SettingsLineInput hint(String hint){
        inputLayout.setHint(hint);
        return this;
    }

}
