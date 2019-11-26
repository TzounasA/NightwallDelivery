package gr.nightwall.deliveryapp.view_holders;

import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;


import com.nex3z.flowlayout.FlowLayout;

import gr.nightwall.deliveryapp.R;

public class ItemIngredientViewHolder extends RecyclerView.ViewHolder{

    public AppCompatTextView tvOptionName;
    public RadioGroup lSingleOptions;
    public FlowLayout lMultipleOptions;


    public ItemIngredientViewHolder(View itemView) {
        super(itemView);

        tvOptionName = itemView.findViewById(R.id.tvOptionName);
        lSingleOptions = itemView.findViewById(R.id.lSingleOptions);
        lMultipleOptions = itemView.findViewById(R.id.lMultipleOptions);
    }
}
