package android.example.com.bakingapp;

import android.app.Activity;
import android.example.com.bakingapp.RecycleLists.IngredientAdapter;
import android.example.com.bakingapp.RecycleLists.StepAdapter;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.example.com.bakingapp.Concepts.DataLoader;

import java.net.URI;
import java.net.URL;

/**
 * A fragment representing a single recipe detail screen.
 * This fragment is either contained in a {@link recipeListActivity}
 * in two-pane mode (on tablets) or a {@link recipeDetailActivity}
 * on handsets.
 */
public class recipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DataLoader.Recipe mItem;
    private IngredientAdapter ingredientAdapter;
    private RecyclerView recycleIngredient;
    private RecyclerView recycleStep;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public recipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DataLoader.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(String.valueOf(mItem.servings));

            ((ImageView)rootView.findViewById(R.id.recipe_image)).setImageURI(Uri.parse(mItem.image));
            ingredientAdapter = new IngredientAdapter(this, mItem.ingredients);
           recycleIngredient = (RecyclerView) rootView.findViewById(R.id.ingredient_list);
            assert  recycleIngredient != null;
            recycleIngredient.setAdapter(ingredientAdapter);
            recycleStep=(RecyclerView)rootView.findViewById(R.id.step_list);
            assert recycleStep!=null;
            recycleStep.setAdapter(new StepAdapter(this,mItem.steps));
        }

        return rootView;
    }
}
