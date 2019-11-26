package gr.nightwall.deliveryapp.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import gr.nightwall.deliveryapp.R;
import gr.nightwall.deliveryapp.interfaces.OnIngredientsListener;
import gr.nightwall.deliveryapp.models.shop.Ingredient;
import gr.nightwall.deliveryapp.models.shop.IngredientCategory;
import gr.nightwall.deliveryapp.view_holders.ItemIngredientViewHolder;

public class ItemIngredientsAdapter extends RecyclerView.Adapter{

    private ArrayList<IngredientCategory> data;
    private ArrayList<String> selectedIngredientsIDs;
    private Context context;
    private OnIngredientsListener onIngredientsListener;

    public ItemIngredientsAdapter(Context context, ArrayList<IngredientCategory> data, ArrayList<String> selectedIngredientsIDs) {
        this.context = context;
        this.data = data;
        this.selectedIngredientsIDs = new ArrayList<>(selectedIngredientsIDs);

        if (this.selectedIngredientsIDs == null){
            this.selectedIngredientsIDs = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.layout_item_ingredient, parent, false);
        return new ItemIngredientViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mHolder, final int position) {
        final ItemIngredientViewHolder holder = (ItemIngredientViewHolder) mHolder;

        IngredientCategory category = data.get(position);

        if (category.getOptions() == IngredientCategory.Options.SINGLE)
            setUpSingleOptionGroup(holder, category);
        else
            setUpMultipleOptionsGroup(holder, category);

        // Name
        holder.tvOptionName.setText(category.getName());
    }

    // SINGLE OPTION
    private void setUpSingleOptionGroup(ItemIngredientViewHolder holder, final IngredientCategory ingredientCategory) {
        holder.lSingleOptions.setVisibility(View.VISIBLE);
        holder.lMultipleOptions.setVisibility(View.GONE);

        int i = 0;
        for (Ingredient ingredient: ingredientCategory.getIngredients()){
            AppCompatRadioButton radioButton = createRadioButton(ingredientCategory.getName(), ingredient);
            holder.lSingleOptions.addView(radioButton);

            if (ingredientCategory.isRequired() && i == 0)
                radioButton.setChecked(true);

            radioButton.setChecked(selectedIngredientsIDs.contains(ingredient.getId()));

            i ++;
        }

    }

    private AppCompatRadioButton createRadioButton(final String option, final Ingredient ingredient) {
        // Create the Radio Button
        final AppCompatRadioButton radioButton = new AppCompatRadioButton(context);

        // Text
        radioButton.setText(ingredient.getNameWithPrice());

        // Change Checked
        radioButton.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                onIngredientsListener.onAddIngredient(option, ingredient);
            }
            else {
                onIngredientsListener.onRemoveIngredient(option, ingredient);
            }
        });

        return radioButton;
    }


    // MULTIPLE OPTIONS
    private void setUpMultipleOptionsGroup(ItemIngredientViewHolder holder, IngredientCategory option) {
        holder.lSingleOptions.setVisibility(View.GONE);
        holder.lMultipleOptions.setVisibility(View.VISIBLE);

        int i = 0;
        for (Ingredient ingredient: option.getIngredients()){
            Chip chip = createNewChip(option.getName(), ingredient);
            holder.lMultipleOptions.addView(chip);

            if (option.isRequired() && i == 0)
                chip.setChecked(true);
            i ++;
        }
    }

    private Chip createNewChip(final String option, final Ingredient ingredient){
        float dp1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                context.getResources().getDisplayMetrics());
        float dp4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                context.getResources().getDisplayMetrics());
        float dp20 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                context.getResources().getDisplayMetrics());

        final Chip chip = new Chip(context);
        chip.setCheckable(true);

        chip.setChipIconSize(dp20);
        chip.setIconStartPadding(dp4);
        chip.setCheckedIconResource(R.drawable.ic_check_24);
        chip.setChipIconTintResource(R.color.onSurface);

        chip.setText(ingredient.getNameWithPrice());
        chip.setTextColor(context.getResources().getColor(R.color.onSurface));
        chip.setTextAppearance(context, R.style.AppTheme_TextAppearance_Chip);

        chip.setRippleColorResource(R.color.colorAccentLight);
        chip.setChipBackgroundColorResource(R.color.black_10);
        chip.setChipStrokeColorResource(R.color.onSurface);
        chip.setChipStrokeWidth(dp1);

        chip.setOnClickListener(view -> {
            if (!chip.isChecked())
                uncheckChip(chip, option, ingredient);
            else
                checkChip(chip, option, ingredient);
        });

        if (selectedIngredientsIDs.contains(ingredient.getId())){
            checkChip(chip, option, ingredient);
        }

        return chip;
    }

    private void checkChip(Chip chip, final String option, Ingredient ingredient){
        chip.setTextColor(context.getResources().getColor(R.color.onSecondary));
        chip.setChipBackgroundColorResource(R.color.colorAccent);
        chip.setChipStrokeWidth(0);

        chip.setChecked(true);

        onIngredientsListener.onAddIngredient(option, ingredient);
    }

    private void uncheckChip(Chip chip, final String option, Ingredient ingredient){
        float dp1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                context.getResources().getDisplayMetrics());

        chip.setTextColor(context.getResources().getColor(R.color.onSurface));
        chip.setChipBackgroundColorResource(R.color.black_10);
        chip.setChipStrokeWidth(dp1);

        chip.setChecked(false);

        onIngredientsListener.onRemoveIngredient(option, ingredient);
    }


    // Ingredients Listener
    public void setOnIngredientsListener(OnIngredientsListener onIngredientsListener) {
        this.onIngredientsListener = onIngredientsListener;
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }
}
