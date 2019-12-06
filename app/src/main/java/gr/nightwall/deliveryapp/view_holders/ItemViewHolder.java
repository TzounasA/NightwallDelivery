package gr.nightwall.deliveryapp.view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import gr.nightwall.deliveryapp.R;

public class ItemViewHolder extends RecyclerView.ViewHolder{

    public View parentView;
    public TextView tvIndex, tvTitle, tvDescription, tvPrice;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        parentView = itemView.findViewById(R.id.parentView);

        tvIndex = itemView.findViewById(R.id.tvItemIndex);
        tvTitle = itemView.findViewById(R.id.tvItemTitle);
        tvDescription = itemView.findViewById(R.id.tvItemDescription);
        tvPrice = itemView.findViewById(R.id.tvItemPrice);
    }
}
