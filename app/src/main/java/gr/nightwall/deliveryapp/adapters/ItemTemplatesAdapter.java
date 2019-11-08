package gr.nightwall.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.ViewHolders.SettingsLine1_ViewHolder;
import gr.nightwall.deliveryapp.interfaces.OnItemTemplateClickListener;
import gr.nightwall.deliveryapp.models.shop.ItemTemplate;

public class ItemTemplatesAdapter extends RecyclerView.Adapter{

    private Context context;
    private ArrayList<ItemTemplate> data;
    private OnItemTemplateClickListener onItemClick;

    public ItemTemplatesAdapter(Context context, ArrayList<ItemTemplate> data) {
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

        ItemTemplate item = data.get(position);

        holder.ivImage.setVisibility(View.INVISIBLE);

        holder.ivIcon.setVisibility(View.VISIBLE);
        holder.ivIcon.setImageResource(R.drawable.ic_category_24);

        holder.tvTitle.setText(item.getName());

        holder.ivSecondIcon.setVisibility(View.GONE);

        holder.parentView.setOnClickListener( v -> onItemClick.onActionClick(item));
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    public void setOnItemClick(OnItemTemplateClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }
}
