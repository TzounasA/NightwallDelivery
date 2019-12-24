package gr.nightwall.deliveryapp.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.interfaces.OnListChangedListener;
import gr.nightwall.deliveryapp.models.shop.Item;
import gr.nightwall.deliveryapp.view_holders.CartViewHolder;

public class CartItemsAdapter extends RecyclerView.Adapter{

    private ArrayList<Item> data;
    private Context context;
    private boolean editable;
    private OnListChangedListener onListChangedListener;

    public CartItemsAdapter(Context context, ArrayList<Item> data, boolean editable) {
        this.data = data;
        this.context = context;
        this.editable = editable;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.view_cart_item, parent, false);
        return new CartViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mHolder, int position) {
        CartViewHolder holder = (CartViewHolder) mHolder;
        Item item = data.get(position);

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        // Margin
        if (position == 0 && editable){
            int margin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
            layoutParams.setMargins(0, margin, 0, 0);

            holder.lPreview.setLayoutParams(layoutParams);
        } else if(position == data.size() - 1) {
            int margin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 32, context.getResources().getDisplayMetrics());
            layoutParams.setMargins(0, 0, 0, margin);
            holder.lPreview.setLayoutParams(layoutParams);
        } else {
            holder.lPreview.setLayoutParams(layoutParams);
        }

        setUpItem(holder, item, position);
    }

    private void setUpItem(final CartViewHolder holder, final Item item, final int position){
        //setUpRecyclerView(holder, orderedItem);

        // Image & Icon
        /*holder.ivItemIcon.setImageResource(orderedItem.getItem().getCategoryTypeIconRes());
        String image = orderedItem.getItem().getImageURL();
        if (image != null && !image.isEmpty()){
            Picasso.with(context).load(image).into(holder.ivItemImage);
            holder.ivItemImage.setVisibility(View.VISIBLE);
        } else
            holder.ivItemImage.setVisibility(View.GONE);*/

        // Delete Button
        if (editable) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(view -> removeItemAt(position));
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        // Name
        holder.tvItemName.setText(item.getName());

        String categoryName = item.getCategoryName();
        if (categoryName == null || categoryName.isEmpty()){
            holder.tvCategoryName.setVisibility(View.GONE);
        } else {
            holder.tvCategoryName.setText(categoryName);
            holder.tvCategoryName.setVisibility(View.VISIBLE);
        }

        // Discount
        /*boolean hasDiscount = false;
        if (orderedItem.getItem().getDiscount() != 0){
            hasDiscount = true;
            holder.groupDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscountPrice.setText(orderedItem.getDiscountPriceString());
        } else
            holder.groupDiscount.setVisibility(View.GONE);*/

        // Ingredients
        boolean hasIngredients = false;
        if (item.getAllIngredientsCount() > 0){
            hasIngredients = true;
            holder.groupIngredients.setVisibility(View.VISIBLE);
            holder.tvIngredientsPrice.setText(item.getIngredientsPriceString());
            holder.tvIngredients.setText(item.getAllIngredientsString());
        } else
            holder.groupIngredients.setVisibility(View.GONE);

        // Start Price
        if (hasIngredients) {
            holder.groupItemPrice.setVisibility(View.VISIBLE);
            holder.tvItemPrice.setText(item.getStartPriceString());
        } else
            holder.groupItemPrice.setVisibility(View.GONE);

        // Additional Info
        String info = item.getAdditionalInfo();
        if (info != null && !info.isEmpty()){
            holder.groupAdditionalInfo.setVisibility(View.VISIBLE);
            holder.tvAdditionalInfo.setText(info);
        } else
            holder.groupAdditionalInfo.setVisibility(View.GONE);

        // Quantity
        holder.quantityPicker.setValue(item.getQuantity());
        holder.quantityPicker.setListener(value -> onChangeQuantity(holder, item, value));

        if (editable){
            holder.quantityPicker.setEnabled(true);
            holder.quantityPicker.setAlpha(1f);
        } else {
            holder.quantityPicker.setEnabled(false);
            holder.quantityPicker.setAlpha(0.3f);
        }

        printFinalPrice(holder, item);
    }

    private void printFinalPrice(CartViewHolder holder, Item item) {
        holder.tvFinalPrice.setText(item.getTotalItemsString());
    }

    private void onChangeQuantity(CartViewHolder holder, Item item, int quantity) {
        item.setQuantity(quantity);
        printFinalPrice(holder, item);

        if (onListChangedListener != null){
            onListChangedListener.onListChanged();
        }
    }

    public void setOnListChangedListener(OnListChangedListener onListChangedListener) {
        this.onListChangedListener = onListChangedListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItemAt(int index){
        data.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, data.size());

        if (onListChangedListener != null){
            onListChangedListener.onListChanged();
        }
    }


}
