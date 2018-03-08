package android.example.com.bakingapp.RecycleLists;

import android.example.com.bakingapp.Concepts.DataLoader;
import android.example.com.bakingapp.Concepts.Ingredient;
import android.example.com.bakingapp.R;
import android.example.com.bakingapp.recipeDetailFragment;
import android.example.com.bakingapp.recipeListActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by florinel on 08.03.2018.
 */

public class IngredientAdapter  extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private final recipeDetailFragment mParentFragment;
    private final List<Ingredient> mValues;
    public IngredientAdapter(recipeDetailFragment parentFragment,
                                         List<Ingredient> items
                                         ) {
        mValues = items;
        mParentFragment=parentFragment;
    }
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_content, parent, false);
        return new IngredientAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {
        holder.name.setText(mValues.get(position).name);
        holder.quantity.setText(String.valueOf(mValues.get(position).quantity));
        holder.measure.setText(mValues.get(position).measure);
        holder.itemView.setTag(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();

    }
    public static class ViewHolder extends  RecyclerView.ViewHolder
    {
        final TextView name;
        final TextView quantity;
        final TextView measure;

        ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_ingredient);
            quantity = (TextView) view.findViewById(R.id.quantity_ingredient);
            measure=(TextView) view.findViewById(R.id.measure_ingredient);
        }
    }
}
