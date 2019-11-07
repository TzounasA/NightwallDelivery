package gr.nightwall.deliveryapp.views;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import gr.nightwall.deliveryapp.R;

public class SettingsLine {

    // Views
    private CircleImageView ivImage;
    private AppCompatImageView ivIcon;

    // Constructor
    public SettingsLine(ViewGroup parentView) {
        ivImage = parentView.findViewById(R.id.ivImage);
        ivIcon = parentView.findViewById(R.id.ivIcon);

        ivImage.setVisibility(View.INVISIBLE);
        ivIcon.setVisibility(View.INVISIBLE);
    }

    // Set up
    public SettingsLine imageRes(int imageRes){
        if (ivImage == null)
            return this;

        ivImage.setVisibility(View.VISIBLE);
        ivImage.setImageResource(imageRes);

        return this;
    }

    public SettingsLine iconRes(int iconRes){
        if (ivIcon == null)
            return this;

        ivIcon.setVisibility(View.VISIBLE);
        ivIcon.setImageResource(iconRes);

        return this;
    }

}
