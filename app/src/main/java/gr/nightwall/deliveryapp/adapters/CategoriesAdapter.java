package gr.nightwall.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Category;
import gr.nightwall.deliveryapp.utils.Utils;
import gr.nightwall.deliveryapp.view_holders.CategoryViewHolder;
import gr.nightwall.deliveryapp.view_holders.SettingsLine3_ViewHolder;

public class CategoriesAdapter extends RecyclerView.Adapter{

    public enum CategoriesViewType{
        EDIT,
        SHOP_SMALL,
        SHOP_BIG
    }

    private Context context;
    private ArrayList<Category> data;
    private OnSettingClickListener onItemClick;
    private CategoriesViewType viewType;

    public CategoriesAdapter(Context context, ArrayList<Category> data, CategoriesViewType viewType) {
        this.context = context;
        this.data = data;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res;
        if (this.viewType == CategoriesViewType.EDIT) {
            res = R.layout.layout_settings_line_3;
            return new SettingsLine3_ViewHolder(LayoutInflater.from( parent.getContext()).inflate(res, parent, false));
        }

        if (this.viewType == CategoriesViewType.SHOP_SMALL) {
            res = R.layout.view_category_small;
        }else{
            res = R.layout.view_category;
        }

        return new CategoryViewHolder(LayoutInflater.from( parent.getContext()).inflate(res, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mHolder, int position) {
        Category item = data.get(position);

        if(viewType == CategoriesViewType.EDIT)
            printEditMode(mHolder, item, position);
        else
            printShopMode(mHolder, item, position);
    }

    private void printShopMode(RecyclerView.ViewHolder mHolder, Category category, int position){
        CategoryViewHolder holder = (CategoryViewHolder) mHolder;

        holder.tvTitle.setText(category.getName());

        if(holder.tvSubtitle != null){
            int itemsCount = category.getItemsCount();
            String format = context.getString(R.string.x_items);
            String items = String.format(Locale.getDefault(), format, itemsCount);
            holder.tvSubtitle.setText(items);
        }

        holder.card.setOnClickListener(view -> {
            selectCategoryAt(position);
            onItemClick.onActionClick(position);
        });

        // Selected mode
        if(category.isSelected()){
            holder.card.setCardBackgroundColor(Utils.getColor(context, R.color.colorPrimary));
            holder.tvTitle.setTextColor(Utils.getColor(context, R.color.onPrimary));
            holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.onPrimary54)
                    , android.graphics.PorterDuff.Mode.SRC_IN);
        } else{
            holder.card.setCardBackgroundColor(Utils.getColor(context, R.color.colorSurface));
            holder.tvTitle.setTextColor(Utils.getColor(context, R.color.onSurface));
            holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.onSurface30)
                    , android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private void printEditMode(RecyclerView.ViewHolder mHolder, Category category, int position){
        SettingsLine3_ViewHolder holder = (SettingsLine3_ViewHolder) mHolder;

        // Image / Icon
        holder.ivImage.setVisibility(View.INVISIBLE);

        holder.ivIcon.setVisibility(View.VISIBLE);
        holder.ivIcon.setImageResource(R.drawable.ic_category_24);

        // Title
        holder.tvTitle.setText(category.getName());

        // Description
        String description = category.getDescription();
        if (description == null || description.isEmpty())
            holder.tvSecondary.setText(context.getString(R.string.no_description));
        else
            holder.tvSecondary.setText(description);

        // Clicks
        holder.ivSecondIcon.setIcon(context.getResources().getDrawable(R.drawable.ic_delete_24));
        holder.ivSecondIcon.setIconTintResource(R.color.colorError);
        holder.ivSecondIcon.setOnClickListener(v -> onItemClick.onDeleteClick(position));

        holder.parentView.setOnClickListener( v -> onItemClick.onActionClick(position));
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

    public void selectCategoryAt(int index){
        for (Category category : data) {
            category.setSelected(false);
        }
        data.get(index).setSelected(true);
        notifyDataSetChanged();
    }

}
