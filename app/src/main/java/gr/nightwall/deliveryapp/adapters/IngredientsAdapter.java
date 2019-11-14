package gr.nightwall.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.view_holders.SettingsLine1_ViewHolder;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter{

    private Context context;
    private ArrayList<Ingredient> data;
    private OnSettingClickListener onItemClick;

    public IngredientsAdapter(Context context, ArrayList<Ingredient> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.layout_settings_line_1, parent, false);

        return new SettingsLine1_ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mHolder, int position) {
        SettingsLine1_ViewHolder holder = (SettingsLine1_ViewHolder) mHolder;

        Ingredient item = data.get(position);

        holder.ivImage.setVisibility(View.INVISIBLE);

        holder.ivIcon.setVisibility(View.VISIBLE);
        holder.ivIcon.setImageResource(R.drawable.ic_arrow_right_24);

        if(item.getPrice() == 0)
            holder.tvTitle.setText(item.getName());
        else
            holder.tvTitle.setText(item.getNameWithPrice());

        holder.ivSecondIcon.setVisibility(View.VISIBLE);
        holder.ivSecondIcon.setIcon(context.getResources().getDrawable(R.drawable.ic_delete_24));
        holder.ivSecondIcon.setIconTintResource(R.color.colorError);
        holder.ivSecondIcon.setOnClickListener(v -> onItemClick.onDeleteClick(position));

        holder.parentView.setOnClickListener(v -> onItemClick.onActionClick(position));
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    public void setOnItemClick(OnSettingClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void removeItemAt(int index){
        data.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, data.size());
    }
}
