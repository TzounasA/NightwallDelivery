package gr.nightwall.deliveryapp.view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import gr.nightwall.deliveryapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder{

    public CardView card;
    public TextView tvTitle, tvSubtitle;
    public AppCompatImageView ivIcon;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        card = itemView.findViewById(R.id.card);

        tvTitle = itemView.findViewById(R.id.tvCategoryTitle);
        tvSubtitle = itemView.findViewById(R.id.tvCategorySubtitle);
        ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
    }
}
