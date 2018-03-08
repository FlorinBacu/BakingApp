package android.example.com.bakingapp.RecycleLists;

import android.content.Context;
import android.content.Intent;
import android.example.com.bakingapp.Concepts.DataLoader;
import android.example.com.bakingapp.Concepts.Step;
import android.example.com.bakingapp.Concepts.MySessionCallback;
import android.example.com.bakingapp.R;
import android.example.com.bakingapp.StepActivity;
import android.example.com.bakingapp.recipeDetailActivity;
import android.example.com.bakingapp.recipeDetailFragment;
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

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by florinel on 08.03.2018.
 */

public class StepAdapter  extends RecyclerView.Adapter<StepAdapter.ViewHolder>  {
    private final recipeDetailFragment mParentFragment;
    private final List<Step> mValues;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

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

        holder.shortDesc.setText(mValues.get(position).shortDescription);
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
                Step step = (Step) view.getTag();
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepActivity.class);

                    intent.putExtra("videoURL",step.videoUrl);
                    intent.putExtra("desc",step.description);

                    context.startActivity(intent);

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
