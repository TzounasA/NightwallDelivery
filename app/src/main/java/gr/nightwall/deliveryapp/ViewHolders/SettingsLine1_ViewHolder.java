package gr.nightwall.deliveryapp.ViewHolders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;
import gr.nightwall.deliveryapp.R;

public class SettingsLine1_ViewHolder extends RecyclerView.ViewHolder {

    public View parentView;

    public CircleImageView ivImage;
    public AppCompatImageView ivIcon;

    public TextView tvTitle;

    public MaterialButton ivSecondIcon;

    public SettingsLine1_ViewHolder(@NonNull View itemView) {
        super(itemView);

        parentView = itemView;

        ivImage = itemView.findViewById(R.id.ivImage);
        ivIcon = itemView.findViewById(R.id.ivIcon);

        tvTitle = itemView.findViewById(R.id.tvTitle);

        ivSecondIcon = itemView.findViewById(R.id.ivSecondIcon);
    }
}
