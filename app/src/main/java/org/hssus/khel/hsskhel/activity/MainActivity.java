package org.hssus.khel.hsskhel.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.hssus.khel.hsskhel.Fragments.GameListFragment;
import org.hssus.khel.hsskhel.Fragments.UploadNewGame;
import org.hssus.khel.hsskhel.R;
import org.hssus.khel.hsskhel.adapters.MyAdapter;
import org.hssus.khel.hsskhel.adapters.MyPagerAdapter;
import org.hssus.khel.hsskhel.models.KhelListModel;
import org.hssus.khel.hsskhel.services.KhelRetrivalService;
import org.hssus.khel.hsskhel.util.Constant;
import org.hssus.khel.hsskhel.util.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity  implements GameListFragment.OnFragmentInteractionListener,UploadNewGame.OnFragmentInteractionListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private KhelReceiver khelReceiver;
    KhelListModel data;
    LocalBroadcastManager localBroadcastManager;

    public static String TAG=MainActivity.class.getName();
    FragmentPagerAdapter adapterViewPager;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class KhelReceiver extends BroadcastReceiver {
        private KhelReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.actions.KHEL_GET_DATA_ACTION:
                    dismissProgressDialog();
                    if (!intent.getBooleanExtra(Constant.extra.ERROR, false)) {

                        data = null;
                        data = KhelListModel.fromJson(intent.getStringExtra(Constant.extra.RESULTS));
                        if(data!=null) {
                            Log.d(TAG, intent.getStringExtra(Constant.extra.RESULTS));
                            setAdapter();
                        }
                        else
                            showToast("Error retrieving data..");
                    } else {
                        showToast(intent.getStringExtra(Constant.extra.ERROR_MESSAGE));
                    }
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferenceManager.getInstance().remove(Constant.AUTH_DATA);

        //Toast.makeText(this, SharedPreferenceManager.getInstance().getString("A",""),Toast.LENGTH_LONG).show();

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);



        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(MainActivity.this, "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        /*
        setLayoutObjects();

        initBroadcastReceiver();

        KhelRetrivalService.startActionKhelRetrivalResult(this);

*/
    }

    private void setAdapter(){

        String[] khelNames = new String[data.getData().size()];
        String[] khelDescription = new String[data.getData().size()];
        String[] khelIds = new String[data.getData().size()];

        for(int i=0;i<data.getData().size();i++){
            khelNames[i]=data.getData().get(i).getName();
            khelDescription[i]=data.getData().get(i).getDescription();
            khelIds[i]=data.getData().get(i).getId();
        }

        // specify an adapter (see also next example)

       // mAdapter = new MyAdapter(khelNames.toArray(new String[0]),khelDescription.toArray(new String[0]),khelIds.toArray(new String[0]));
        mAdapter = new MyAdapter(this,khelNames,khelDescription,khelIds);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void setLayoutObjects(){
        mRecyclerView = (RecyclerView) findViewById(R.id.khellists);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initBroadcastReceiver() {
        khelReceiver = new KhelReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter khelServiceFilter = new IntentFilter(Constant.actions.KHEL_GET_DATA_ACTION);

        localBroadcastManager.registerReceiver(khelReceiver, khelServiceFilter);
    }
}
