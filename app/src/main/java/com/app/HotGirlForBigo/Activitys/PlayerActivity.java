package com.app.hotgirlforbigo.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hotgirlforbigo.Model.Profile;
import com.app.hotgirlforbigo.Player.DemoApplication;
import com.app.hotgirlforbigo.Player.EventLogger;
import com.app.hotgirlforbigo.R;
import com.app.hotgirlforbigo.Utils.Icon_Manager;
import com.app.hotgirlforbigo.Utils.UtilConnect;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity implements PlaybackControlView.VisibilityListener, ExoPlayer.EventListener {

    private final String TAG = "PlayerActivity";
    private Icon_Manager icon_manager;

    SimpleExoPlayerView exoPlayerView;
    private Handler mainHandler;
    private EventLogger eventLogger;
    private TextView iconClose;
    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private AVLoadingIndicatorView avi;
    private DefaultTrackSelector trackSelector;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);

        icon_manager = new Icon_Manager();

        getSupportActionBar().hide();

        Intent intent = getIntent();
        String url = intent.getStringExtra(Profile.LIVE_URL);


        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();

        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoPlayerView);
//        exoPlayerView.setControllerVisibilityListener(this);
        exoPlayerView.setUseController(false);
        exoPlayerView.requestFocus();

        final AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setVisibility(View.GONE);

        iconClose = (TextView) findViewById(R.id.iconClose);
        iconClose.setTypeface(icon_manager.get_icons("fonts/ionicons.ttf", this));

        iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdView.setVisibility(View.GONE);
                iconClose.setVisibility(View.GONE);
            }
        });

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

        AsyncGetLink asyncGetLink = new AsyncGetLink();
        asyncGetLink.execute(url);


    }


    @Override
    public void onVisibilityChange(int visibility) {

    }

    @Override
    public void onNewIntent(Intent intent) {
//        releasePlayer();
//        shouldAutoPlay = true;
//        clearResumePosition();
//        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
//            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if ((Util.SDK_INT <= 23 || player == null)) {
//            initializePlayer();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    public void initPlayer(String url) {
        Log.d(TAG, "link video live : " + url);
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl());
        player.addListener(this);

        eventLogger = new EventLogger(trackSelector);
        player.addListener(eventLogger);
        player.setAudioDebugListener(eventLogger);
        player.setVideoDebugListener(eventLogger);
        player.setMetadataOutput(eventLogger);
        player.setPlayWhenReady(true);

        exoPlayerView.setPlayer(player);

        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri, null);
        player.prepare(mediaSource, false, false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d(TAG, "onPlayerStateChanged : " + isLoading);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d(TAG, "onPlayerStateChanged : " + playbackState + "  playbackState : " + playbackState);
        switch (playbackState) {
            case 1:

            case 2:
                if (!avi.isShown()) {
                    startAnim();
                }
                break;
            case 3:
                if (avi.isShown()) {
                    stopAnim();
                }
                break;
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d(TAG, "onPlayerError : " + error);
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((DemoApplication) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
                : uri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mainHandler, eventLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private void releasePlayer() {
        if (player != null) {
//            debugViewHelper.stop();
//            debugViewHelper = null;
//            shouldAutoPlay = player.getPlayWhenReady();
//            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
//            trackSelectionHelper = null;
            eventLogger = null;
        }
    }

    void startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }


    public class AsyncGetLink extends AsyncTask<String, String, String> {

        public AsyncGetLink() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnim();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = UtilConnect.getAPI(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && !s.trim().isEmpty()) {
                initPlayer(s);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.live_ended), Toast.LENGTH_SHORT).show();
                stopAnim();
                finish();
            }
        }

    }

}
