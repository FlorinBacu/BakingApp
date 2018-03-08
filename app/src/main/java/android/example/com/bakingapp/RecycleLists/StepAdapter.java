package android.example.com.bakingapp.RecycleLists;

import android.example.com.bakingapp.Concepts.Ingredient;
import android.example.com.bakingapp.Concepts.Step;
import android.example.com.bakingapp.R;
import android.example.com.bakingapp.recipeDetailFragment;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by florinel on 08.03.2018.
 */

public class StepAdapter  extends RecyclerView.Adapter<StepAdapter.ViewHolder>{
    private final recipeDetailFragment mParentFragment;
    private final List<Step> mValues;
    public StepAdapter(recipeDetailFragment parentFragment,
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
        holder.video.setVideoURI(Uri.parse(mValues.get(position).videoUrl));
        holder.shortDesc.setText(mValues.get(position).shortDescription);
        holder.desc.setText(mValues.get(position).description);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder
    {
        final VideoView video;
        final TextView shortDesc;
        final TextView desc;
        public ViewHolder(View itemView) {
            super(itemView);
            video=(VideoView)itemView.findViewById(R.id.video_step);
            shortDesc=(TextView)itemView.findViewById(R.id.shortdesc_step);
            desc=(TextView)itemView.findViewById(R.id.desc_step);
        }
    }

}
