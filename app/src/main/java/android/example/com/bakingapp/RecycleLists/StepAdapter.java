package android.example.com.bakingapp.RecycleLists;

import android.example.com.bakingapp.Concepts.Ingredient;
import android.example.com.bakingapp.Concepts.Step;
import android.example.com.bakingapp.Concepts.mySessionCallback;
import android.example.com.bakingapp.R;
import android.example.com.bakingapp.recipeDetailFragment;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.w3c.dom.Text;

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
        if(mMediaSession==null) {
            mMediaSession = new MediaSessionCompat(mParentFragment.getActivity(), TAG);
            mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setMediaButtonReceiver(null);
            mStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY |
                            PlaybackStateCompat.ACTION_PAUSE |
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
            mMediaSession.setPlaybackState(mStateBuilder.build());

            mMediaSession.setActive(true);
        }
        return new StepAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.mExoPlayer==null && !mValues.get(position).videoUrl.equals("") )
        {
            initializePlayer(Uri.parse(mValues.get(position).videoUrl),holder);
            holder.video.setPlayer( holder.mExoPlayer);
            mMediaSession.setCallback(new mySessionCallback( holder.mExoPlayer ));

        }

        holder.shortDesc.setText(mValues.get(position).shortDescription);
        holder.desc.setText(mValues.get(position).description);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    private void initializePlayer(Uri mediaUri,ViewHolder vh) {

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
        vh.mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.mParentFragment.getActivity(), trackSelector, loadControl);


            // Set the ExoPlayer.EventListener to this activity.
            vh.mExoPlayer.addListener(vh);

            // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(this.mParentFragment.getActivity(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                this.mParentFragment.getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        vh.mExoPlayer.prepare(mediaSource);
        vh.mExoPlayer.setPlayWhenReady(true);

    }



    public class ViewHolder extends  RecyclerView.ViewHolder implements ExoPlayer.EventListener
    {
        private SimpleExoPlayer mExoPlayer;
        final SimpleExoPlayerView video;
        final TextView shortDesc;
        final TextView desc;
        public ViewHolder(View itemView) {
            super(itemView);
            video=(SimpleExoPlayerView)itemView.findViewById(R.id.video_step);
            shortDesc=(TextView)itemView.findViewById(R.id.shortdesc_step);
            desc=(TextView)itemView.findViewById(R.id.desc_step);

        }



        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        mExoPlayer.getCurrentPosition(), 1f);
            } else if((playbackState == ExoPlayer.STATE_READY)){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        mExoPlayer.getCurrentPosition(), 1f);
            }

            mMediaSession.setPlaybackState(mStateBuilder.build());
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }

    }

}
