package gr.nightwall.deliveryapp.view_holders;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import de.hdodenhof.circleimageview.CircleImageView;
import gr.nightwall.deliveryapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout lPreview;

    public AppCompatImageView ivItemIcon;
    public CircleImageView ivItemImage;
    public AppCompatTextView tvItemName;
    public MaterialButton btnDelete;

    public Group groupItemPrice, groupIngredients, groupAdditionalInfo;
    public AppCompatTextView tvCategoryName, tvItemPrice, tvAdditionalInfo,
            tvIngredientsPrice, tvIngredients;

    public RecyclerView rvOfferItems;

    public ScrollableNumberPicker quantityPicker;

    public AppCompatTextView tvFinalPrice;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        lPreview = itemView.findViewById(R.id.lPreview);

        ivItemIcon = itemView.findViewById(R.id.ivItemIcon);
        ivItemImage = itemView.findViewById(R.id.ivItemImage);
        tvItemName = itemView.findViewById(R.id.tvItemName);
        btnDelete = itemView.findViewById(R.id.btnDelete);

        tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        groupItemPrice = itemView.findViewById(R.id.groupItemPrice);
        tvItemPrice = itemView.findViewById(R.id.tvItemPrice);

        groupIngredients = itemView.findViewById(R.id.groupIngredients);
        tvIngredientsPrice = itemView.findViewById(R.id.tvIngredientsPrice);
        tvIngredients = itemView.findViewById(R.id.tvIngredients);

        groupAdditionalInfo = itemView.findViewById(R.id.groupAdditionalInfo);
        tvAdditionalInfo = itemView.findViewById(R.id.tvAdditionalInfo);

        rvOfferItems = itemView.findViewById(R.id.rvOfferItems);

        quantityPicker = itemView.findViewById(R.id.quantityPicker);

        tvFinalPrice = itemView.findViewById(R.id.tvFinalPrice);
    }
}
