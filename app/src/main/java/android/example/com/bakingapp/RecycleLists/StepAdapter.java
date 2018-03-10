package android.example.com.bakingapp.RecycleLists;

import android.content.Context;
import android.content.Intent;
import android.example.com.bakingapp.Concepts.DataLoader;
import android.example.com.bakingapp.Concepts.Step;
import android.example.com.bakingapp.R;
import android.example.com.bakingapp.RecipeListActivity;
import android.example.com.bakingapp.StepActivity;
import android.example.com.bakingapp.RecipeDetailFragment;
import android.example.com.bakingapp.StepActivityFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by florinel on 08.03.2018.
 */

public class StepAdapter  extends RecyclerView.Adapter<StepAdapter.ViewHolder>  {
    private final RecipeDetailFragment mParentFragment;
    private final List<Step> mValues;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public StepAdapter(RecipeDetailFragment parentFragment,
                             List<Step> items
    ) {
        mValues = items;
        mParentFragment=parentFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_content, parent, false);
        return new StepAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.shortDesc.setText("Step "+(position+1)+":\n"+mValues.get(position).shortDescription);
        holder.image.setImageURI(Uri.parse(mValues.get(position).thumbnailURL));
        holder.itemView.setTag(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }




    public class ViewHolder extends  RecyclerView.ViewHolder
    {
        final ImageView image;
        final TextView shortDesc;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RecipeListActivity.mTwoPane) {
                    // The detail container view will be present only in the
                    // large-screen layouts (res/values-w900dp).
                    // If this view is present, then the
                    // activity should be in two-pane mode.
                    Step step = (Step) view.getTag();
                    StepActivityFragment.currentStepIndex = DataLoader.ITEMS.get(RecipeDetailFragment.currentRecipeIndex).steps.indexOf(step);
                    Bundle arguments = new Bundle();
                    arguments.putBoolean("sent",true);
                    arguments.putString("desc",step.description);
                    arguments.putString("videoURL",step.videoUrl);
                    StepActivityFragment fragment = new StepActivityFragment();
                    fragment.setArguments(arguments);
                    if(RecipeListActivity.mTwoPane)
                    {
                        mParentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_detail_container, fragment)
                                .commit();
                    }

                                else {
                        mParentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_step, fragment)
                                .commit();
                    }
                }
                else {
                    Step step = (Step) view.getTag();
                    StepActivityFragment.currentStepIndex = DataLoader.ITEMS.get(RecipeDetailFragment.currentRecipeIndex).steps.indexOf(step);
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepActivity.class);

                    intent.putExtra("videoURL", step.videoUrl);
                    intent.putExtra("desc", step.description);
                   /* intent.putExtra("currentRecipeIndex",)
                intent.putExtra("currentStepIndex",)*/
                    context.startActivity(intent);
                }

            }
        };
        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image_step);
            shortDesc=(TextView)itemView.findViewById(R.id.shortdesc_step);
            itemView.setOnClickListener(mOnClickListener);


        }





    }

}
