package org.hssus.khel.hsskhel.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.hssus.khel.hsskhel.R;
import org.hssus.khel.hsskhel.models.KhelListModel;
import org.hssus.khel.hsskhel.models.KhelModel;
import org.hssus.khel.hsskhel.services.KhelRetrivalService;
import org.hssus.khel.hsskhel.util.Constant;
import org.hssus.khel.hsskhel.util.SharedPreferenceManager;
import org.hssus.khel.hsskhel.util.YouTubeHelper;

import java.security.Provider;

public class KhelDetails extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {



    private class KhelReceiver extends BroadcastReceiver {
        private KhelReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.actions.KHEL_DETAILS_ACTION:
                    if (!intent.getBooleanExtra(Constant.extra.ERROR, false)) {

                        data = null;
                        data = KhelModel.fromJson(intent.getStringExtra(Constant.extra.RESULTS));
                        if(data!=null) {
                            Log.d(TAG, intent.getStringExtra(Constant.extra.RESULTS));
                            setData();
                        }
                        else
                            Toast.makeText(KhelDetails.this, "Error while retrieving data", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(KhelDetails.this, intent.getStringExtra(Constant.extra.ERROR_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    KhelModel data;

    public static final String TAG = KhelDetails.class.getName();
    String API_KEY = "AIzaSyBXGMqvg_pgNiNb5TnyC5n0GY5KojJPFKg";

    YouTubePlayerView youTubePlayerView;
    public String VIDEO_ID = "";
    LocalBroadcastManager localBroadcastManager;
    private KhelReceiver khelReceiver;
    private TextView txt_khel_name,txt_khel_description;

    private void initBroadcastReceiver() {
        khelReceiver = new KhelReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter khelServiceFilter = new IntentFilter(Constant.actions.KHEL_DETAILS_ACTION);



        localBroadcastManager.registerReceiver(khelReceiver, khelServiceFilter);
    }

    private void loadUIComponent(){
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeview);
        txt_khel_description=(TextView)findViewById(R.id.txt_khel_description);
        txt_khel_name=(TextView)findViewById(R.id.txt_khel_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khel_details);


        initBroadcastReceiver();
        loadUIComponent();

        Toast.makeText(this, SharedPreferenceManager.getInstance().getString("A",""),Toast.LENGTH_LONG);

        KhelRetrivalService.startActionKhelDetailsRetrivalResult(this,getIntent().getStringExtra(Constant.GAME_ID));
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        //Log.e(TAG,"Video" +showVideo);
        if (VIDEO_ID.length() > 0)
            youTubePlayer.cueVideo(YouTubeHelper.extractVideoIdFromUrl(VIDEO_ID));
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        Toast.makeText(this, "Error Intializing Youtube Player", Toast.LENGTH_LONG).show();
    }

    public void setData(){
        VIDEO_ID=data.getVideo();
        txt_khel_description.setText(data.getDescription());
        txt_khel_name.setText(data.getName());
        //Toast.makeText(this,data.getVideo(),Toast.LENGTH_SHORT);
        youTubePlayerView.initialize(API_KEY, this);
    }
}
