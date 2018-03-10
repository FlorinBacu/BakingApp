package android.example.com.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.example.com.bakingapp.Concepts.Ingredient;
import android.example.com.bakingapp.Concepts.Step;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;


import android.example.com.bakingapp.Concepts.DataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static boolean mTwoPane;
    private static RecyclerView recyclerView;
    private static RecipeListActivity context;
    public boolean onTestData=false;
    public void addRecipeTest()
    {
        DataLoader.ITEMS.add(new DataLoader.Recipe(0,"test",new ArrayList<Ingredient>(),new ArrayList<Step>(),0,""));
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
        DataLoader.load(this);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
else
{
    addRecipeTest();
}
        //RecipeListActivity.setupRecyclerView();

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
