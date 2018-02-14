package org.hssus.khel.hsskhel.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.hssus.khel.hsskhel.R;
import org.hssus.khel.hsskhel.activity.MainActivity;
import org.hssus.khel.hsskhel.adapters.MyAdapter;
import org.hssus.khel.hsskhel.models.KhelListModel;
import org.hssus.khel.hsskhel.services.KhelRetrivalService;
import org.hssus.khel.hsskhel.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameListFragment extends Fragment {

    private class KhelReceiver extends BroadcastReceiver {
        private KhelReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.actions.KHEL_GET_DATA_ACTION:
                    //dismissProgressDialog();
                    if (!intent.getBooleanExtra(Constant.extra.ERROR, false)) {

                        data = null;
                        data = KhelListModel.fromJson(intent.getStringExtra(Constant.extra.RESULTS));
                        if(data!=null) {
                            Log.d(TAG, intent.getStringExtra(Constant.extra.RESULTS));
                            setAdapter();
                        }
                        //else
                            //showToast("Error retrieving data..");
                    } else {
                        //showToast(intent.getStringExtra(Constant.extra.ERROR_MESSAGE));
                    }
                    break;
            }
        }
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private KhelReceiver khelReceiver;
    private TextView txtFilter;
    private LinearLayout gameListLayout,filterLayout;
    private Button btnAppyFilter;
    KhelListModel data;
    LocalBroadcastManager localBroadcastManager;

    private RadioGroup formationGroup,intensityGroup,audianceGroup,minimumParticipantsGroup,maximumParticipantsGroup;

    public static String TAG=MainActivity.class.getName();


    private OnFragmentInteractionListener mListener;

    public GameListFragment() {
        // Required empty public constructor
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
        mAdapter = new MyAdapter(getContext(),khelNames,khelDescription,khelIds);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void setLayoutObjects(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.khellists);
        txtFilter=view.findViewById(R.id.txtFilter);
        gameListLayout=view.findViewById(R.id.gameList);
        filterLayout=view.findViewById(R.id.filterLayout);
        btnAppyFilter=view.findViewById(R.id.btnAppyFilter);

        formationGroup =(RadioGroup)view.findViewById(R.id.formationGroup);
        intensityGroup=(RadioGroup)view.findViewById(R.id.intensityGroup);
        audianceGroup=(RadioGroup)view.findViewById(R.id.audianceGroup);
        minimumParticipantsGroup=(RadioGroup)view.findViewById(R.id.minimumParticipantsGroup);
        maximumParticipantsGroup=(RadioGroup)view.findViewById(R.id.maximumParticipantsGroup);

        toggleView();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getData();

        txtFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView();
            }
        });

        btnAppyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();
                toggleView();

            }
        });
    }

    private void toggleView(){
        if(gameListLayout.getVisibility()==View.GONE) {
            gameListLayout.setVisibility(View.VISIBLE);
            filterLayout.setVisibility(View.GONE);
        }else{
            gameListLayout.setVisibility(View.GONE);
            filterLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initBroadcastReceiver() {
        khelReceiver = new KhelReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());

        IntentFilter khelServiceFilter = new IntentFilter(Constant.actions.KHEL_GET_DATA_ACTION);

        localBroadcastManager.registerReceiver(khelReceiver, khelServiceFilter);
    }

    private void getData(){

        int radioButtonID = formationGroup.getCheckedRadioButtonId();
        RadioButton formationButton = formationGroup.findViewById(radioButtonID);
        //Toast.makeText(getContext(),"Uploading "+radioButton.getText().toString(),Toast.LENGTH_LONG).show();

        radioButtonID = intensityGroup.getCheckedRadioButtonId();
        RadioButton intensityRadio = intensityGroup.findViewById(radioButtonID);

        radioButtonID = audianceGroup.getCheckedRadioButtonId();
        RadioButton audianceButton = audianceGroup.findViewById(radioButtonID);

        radioButtonID = minimumParticipantsGroup.getCheckedRadioButtonId();
        RadioButton minBtn = minimumParticipantsGroup.findViewById(radioButtonID);

        radioButtonID = maximumParticipantsGroup.getCheckedRadioButtonId();
        RadioButton maxBtn = maximumParticipantsGroup.findViewById(radioButtonID);



        KhelRetrivalService.startActionKhelRetrivalResult(getContext(),formationButton.getText().toString(),intensityRadio.getText().toString(),audianceButton.getText().toString(),minBtn.getText().toString(),maxBtn.getText().toString());
    }


    // TODO: Rename and change types and number of parameters
    public static GameListFragment newInstance() {
        GameListFragment fragment = new GameListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBroadcastReceiver();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_game_list, container, false);
        setLayoutObjects(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
