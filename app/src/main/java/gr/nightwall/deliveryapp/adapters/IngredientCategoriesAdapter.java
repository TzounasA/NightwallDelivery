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
import gr.nightwall.deliveryapp.ViewHolders.SettingsLine3_ViewHolder;
import gr.nightwall.deliveryapp.interfaces.OnIngredientCategoriesClickListener;
import gr.nightwall.deliveryapp.interfaces.OnItemTemplateClickListener;
import gr.nightwall.deliveryapp.models.shop.IngredientCategory;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;

public class IngredientCategoriesAdapter extends RecyclerView.Adapter{

    private Context context;
    private ArrayList<IngredientCategory> data;
    private OnIngredientCategoriesClickListener onItemClick;

    public IngredientCategoriesAdapter(Context context, ArrayList<IngredientCategory> data) {
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

        IngredientCategory item = data.get(position);

        holder.ivImage.setVisibility(View.INVISIBLE);

        holder.ivIcon.setVisibility(View.VISIBLE);
        holder.ivIcon.setImageResource(R.drawable.ic_category_24);

        holder.tvTitle.setText(item.getName());
        String ingredients = context.getString(R.string.options).toLowerCase();
        String secondary = String.format(Locale.getDefault(),"%d %s", item.getIngredientsCount(), ingredients);
        holder.tvSecondary.setText(secondary);

        holder.ivSecondIcon.setVisibility(View.GONE);

        holder.parentView.setOnClickListener( v -> onItemClick.onActionClick(item));
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    public void setOnItemClick(OnIngredientCategoriesClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }
}
