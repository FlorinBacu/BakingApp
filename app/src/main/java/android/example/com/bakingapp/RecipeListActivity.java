package android.example.com.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.example.com.bakingapp.Concepts.DetailAdapter;
import android.example.com.bakingapp.Concepts.IdleResource;
import android.example.com.bakingapp.Concepts.Ingredient;
import android.example.com.bakingapp.Concepts.Step;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;


import android.example.com.bakingapp.Concepts.DataLoader;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * An activity representing a list of recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements DataLoader.DelayerCallback{

    private static final String EXTRA_RECIPE_NAME ="com.example.android.bakingapp.EXTRA_RECIPE_NAME" ;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    Intent mRecipeIntent;
    public static boolean mTwoPane;
    private static RecyclerView recyclerView;
    private static RecipeListActivity context;
    public boolean onTestData=false;
    public void addRecipeTest()
    {
        ArrayList<Step> steps=new ArrayList<Step>();
        steps.add(new Step(0,"step name","long description","videoUrl","thumbURL"));
        DataLoader.Recipe recipe=new DataLoader.Recipe(0,"test",new ArrayList<Ingredient>(),steps,0,"");
DataLoader.addRecipe(recipe);
    }
    @Nullable
    private IdleResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new IdleResource();
        }
        return mIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

/*
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeListActivity.class));
        //Trigger data update to handle the GridView widgets and force a data

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.layout.recipe_list_widget);

        //Now update all widgets

*/

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        else
            mTwoPane=false;


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recipe_list);
        assert recyclerView != null;
        RecipeListActivity.recyclerView=recyclerView;
        RecipeListActivity.context=this;

if(!onTestData) {
    try {
        getIdlingResource();
        DataLoader.load(this,this,mIdlingResource);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
else
{
    addRecipeTest();
    RecipeListActivity.setupRecyclerView();
}


    }

    public static void setupRecyclerView() {
        RecipeListActivity.recyclerView.setAdapter(new RecipeListActivity.SimpleItemRecyclerViewAdapter(RecipeListActivity.context, DataLoader.ITEMS, RecipeListActivity.mTwoPane));
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);

        int[] widgetsIds=appWidgetManager.getAppWidgetIds(new ComponentName(context,HomeWidget.class));
        // RecipeDetailWidget.updateAppWidgets(getActivity(),appWidgetManager,widgetsIds,mItem.name,mItem.toString());
        RemoteViews remoteView=new RemoteViews(context.getPackageName(), R.layout.home_widget);
        DataLoader.Recipe.show_name=true;
        remoteView.setTextViewText(R.id.list_recipes_widget,"Recipe list:\n"+DataLoader.ITEMS.toString().substring(1,DataLoader.ITEMS.toString().length()-1).replace(",",""));
        RecipeDetailWidget.updateAppWidgets(context,appWidgetManager,widgetsIds);

        appWidgetManager.updateAppWidget(widgetsIds,remoteView);

    }

    @Override
    public void onDone(ArrayList<DataLoader.Recipe> recipes) {
        //TextView testing =(TextView)findViewById(R.id.textView);
        //testing.setText("Changed");
/*
        // Create a {@link TeaAdapter}, whose data source is a list of {@link Tea}s.
        // The adapter know how to create grid items for each item in the list.
        RecyclerView gridview = (RecyclerView) findViewById(R.id.recipe_list);
        DetailAdapter adapter = new DetailAdapter(this, R.layout.recipe_detail, (ArrayList< DataLoader.Recipe>)DataLoader.ITEMS);
        gridview.setAdapter(adapter);

        // Set a click listener on that View
        RecyclerView.OnItemTouchListener oitl=new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                DataLoader.Recipe item = (DataLoader.Recipe)rv. .getItemAtPosition(position);
                // Set the intent to open the {@link OrderActivity}
                mRecipeIntent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
                String recipeName = item.name;
                mRecipeIntent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, String.valueOf(item.id));
                startActivity(mRecipeIntent);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
        gridview.addOnItemTouchListener(oitl);

              */


    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<DataLoader.Recipe> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataLoader.Recipe item = (DataLoader.Recipe) view.getTag();
                RecipeDetailFragment.currentRecipeIndex=DataLoader.ITEMS.indexOf(item);
                Timber.d("go to detail for recipe  "+item.name);
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, String.valueOf(item.id));
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, String.valueOf(item.id));

                    context.startActivity(intent);
                }
            }
        };

       public SimpleItemRecyclerViewAdapter(RecipeListActivity parent,
                                      List<DataLoader.Recipe> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(mValues.get(position).id));
            holder.mContentView.setText(mValues.get(position).name);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
