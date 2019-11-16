package gr.nightwall.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.interfaces.OnItemTemplateClickListener;
import gr.nightwall.deliveryapp.interfaces.OnSettingClickListener;
import gr.nightwall.deliveryapp.models.shop.Category;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;
import gr.nightwall.deliveryapp.view_holders.SettingsLine1_ViewHolder;
import gr.nightwall.deliveryapp.view_holders.SettingsLine3_ViewHolder;

public class CategoriesAdapter extends RecyclerView.Adapter{

    private Context context;
    private ArrayList<Category> data;
    private OnSettingClickListener onItemClick;

    public CategoriesAdapter(Context context, ArrayList<Category> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.layout_settings_line_3, parent, false);

        return new SettingsLine3_ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mHolder, int position) {
        SettingsLine3_ViewHolder holder = (SettingsLine3_ViewHolder) mHolder;

        Category item = data.get(position);

        // Image / Icon
        holder.ivImage.setVisibility(View.INVISIBLE);

        holder.ivIcon.setVisibility(View.VISIBLE);
        holder.ivIcon.setImageResource(R.drawable.ic_category_24);

        // Title
        holder.tvTitle.setText(item.getName());

        // Description
        String description = item.getDescription();
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

}
