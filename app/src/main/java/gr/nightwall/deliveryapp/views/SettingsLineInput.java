package gr.nightwall.deliveryapp.views;

import android.text.InputType;
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

    public SettingsLineInput inputType(int inputType){
        input.setInputType(inputType);
        return this;
    }

    public SettingsLineInput helperText(String helper){
        inputLayout.setHelperText(helper);
        return this;
    }

    public SettingsLineInput errorText(String error){
        inputLayout.setErrorEnabled(true);
        inputLayout.setError(error);
        return this;
    }

}
