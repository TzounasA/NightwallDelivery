package gr.nightwall.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Ingredient;
import gr.nightwall.deliveryapp.models.shop.Item;
import gr.nightwall.deliveryapp.view_holders.ItemViewHolder;
import gr.nightwall.deliveryapp.view_holders.SettingsLine1_ViewHolder;

public class ItemsAdapter extends RecyclerView.Adapter{

    private Context context;
    private ArrayList<Item> data;
    private OnSettingClickListener onItemClick;
    private boolean shopMode;

    public ItemsAdapter(Context context, ArrayList<Item> data, boolean shopMode) {
        this.context = context;
        this.data = data;
        this.shopMode = shopMode;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(shopMode){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
            return new ItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_settings_line_1, parent, false);
            return new SettingsLine1_ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mHolder, int position) {
        Item item = data.get(position);

        if(shopMode){
            printShopMode(mHolder, item, position);
        }else{
            printEditMode(mHolder, item, position);
        }
    }

    private void printShopMode(RecyclerView.ViewHolder mHolder, Item item, int position){
        ItemViewHolder holder = (ItemViewHolder) mHolder;

        String index = String.format(Locale.getDefault(), "%02d.", position + 1);
        holder.tvIndex.setText(index);

        holder.tvTitle.setText(item.getNameWithFinalPrice());

        String description = item.getDescription();
        if(description == null || description.isEmpty()){
            holder.tvDescription.setVisibility(View.GONE);
        }else{
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(description);
        }

        holder.tvPrice.setText(item.getFinalPriceString());

        holder.parentView.setOnClickListener(v -> onItemClick.onActionClick(position));
    }

    private void printEditMode(RecyclerView.ViewHolder mHolder, Item item, int position){
        SettingsLine1_ViewHolder holder = (SettingsLine1_ViewHolder) mHolder;

        holder.ivImage.setVisibility(View.INVISIBLE);

        holder.ivIcon.setVisibility(View.VISIBLE);
        holder.ivIcon.setImageResource(R.drawable.ic_arrow_right_24);

        holder.tvTitle.setText(item.getNameWithFinalPrice());

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
