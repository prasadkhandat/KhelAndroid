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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.hssus.khel.hsskhel.R;
import org.hssus.khel.hsskhel.activity.MainActivity;
import org.hssus.khel.hsskhel.activity.SignUp;
import org.hssus.khel.hsskhel.models.AuthModel;
import org.hssus.khel.hsskhel.models.KhelListModel;
import org.hssus.khel.hsskhel.models.KhelModel;
import org.hssus.khel.hsskhel.services.AuthenticationService;
import org.hssus.khel.hsskhel.services.UploadNewGameService;
import org.hssus.khel.hsskhel.util.Constant;
import org.hssus.khel.hsskhel.util.SharedPreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadNewGame.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadNewGame#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadNewGame extends Fragment {


    private class KhelReceiver extends BroadcastReceiver {
        private KhelReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constant.actions.KHEL_UPLOAD:
                    if (!intent.getBooleanExtra(Constant.extra.ERROR, false)) {
                        Toast.makeText(getContext(), "Game Uploaded successfully...", Toast.LENGTH_SHORT).show();
                        cleanupUpload();
                    }else{
                        Toast.makeText(getContext(), "Error Uploading game", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constant.actions.KHEL_AUTH_VERIFY:
                    if (!intent.getBooleanExtra(Constant.extra.ERROR, false)) {
                        switch (verifyFrom) {
                            case 1:
                                setLayout();
                                break;
                            case 2:
                                performUpload();
                                break;
                        }

                    } else {
                        Toast.makeText(getContext(), "Session Expired", Toast.LENGTH_SHORT).show();
                        SharedPreferenceManager.getInstance().remove(Constant.AUTH_DATA);
                        setLayout();
                    }
                    break;
                case Constant.actions.KHEL_AUTH:
                    //dismissProgressDialog();
                    if (!intent.getBooleanExtra(Constant.extra.ERROR, false)) {

                        data = null;
                        data = AuthModel.fromJson(intent.getStringExtra(Constant.extra.RESULTS));

                        if (data != null) {
                            Log.d(TAG, intent.getStringExtra(Constant.extra.RESULTS));
                            SharedPreferenceManager.getInstance().putString(Constant.USERNAME, username.getText().toString());
                            SharedPreferenceManager.getInstance().putString(Constant.PASSWORD, password.getText().toString());
                            SharedPreferenceManager.getInstance().putString(Constant.AUTH_DATA, data.toJson().toString());
                            setLayout();
                        } else {
                            SharedPreferenceManager.getInstance().remove(Constant.AUTH_DATA);
                            Toast.makeText(getContext(), "Username / password is incorrect", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        SharedPreferenceManager.getInstance().remove(Constant.AUTH_DATA);
                        Toast.makeText(getContext(), "Username / password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private KhelReceiver khelReceiver;
    AuthModel data;
    LocalBroadcastManager localBroadcastManager;
    private int verifyFrom=0;

    public static String TAG=MainActivity.class.getName();

    private RadioGroup formationGroup,intensityGroup,audianceGroup,minimumParticipantsGroup,maximumParticipantsGroup;

    private OnFragmentInteractionListener mListener;
    private EditText username,password,youtubeLink,txtKhelName,txtKhelDescription;
    private Button btnLogin,btnUpload,btnRegister;
    private LinearLayout loginLayout,uploadLayout;

    public UploadNewGame() {
        // Required empty public constructor
    }

    private void setLayout() {
        AuthModel model = AuthModel.fromJson(SharedPreferenceManager.getInstance().getString(Constant.AUTH_DATA,""));
        if(model!=null && model.getAccessToken().length()>0){
            loginLayout.setVisibility(View.GONE);
            uploadLayout.setVisibility(View.VISIBLE);
        }else{
            uploadLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setLayoutObjects(View view){
        username=(EditText)view.findViewById(R.id.txtUsername);
        password=(EditText)view.findViewById(R.id.txtPassword);
        btnLogin=(Button)view.findViewById(R.id.btnLogin);
        btnUpload=(Button)view.findViewById(R.id.btnUpload);
        btnRegister=view.findViewById(R.id.btnRegister);
        loginLayout=(LinearLayout)view.findViewById(R.id.login_layout);
        uploadLayout =(LinearLayout)view.findViewById(R.id.upload_layout);

        youtubeLink=(EditText)view.findViewById(R.id.youtubeLink);
        txtKhelName=(EditText)view.findViewById(R.id.txtKhelName);
        txtKhelDescription=(EditText)view.findViewById(R.id.txtKhelDescription);

        formationGroup =(RadioGroup)view.findViewById(R.id.formationGroup);
        intensityGroup=(RadioGroup)view.findViewById(R.id.intensityGroup);
        audianceGroup=(RadioGroup)view.findViewById(R.id.audianceGroup);
        minimumParticipantsGroup=(RadioGroup)view.findViewById(R.id.minimumParticipantsGroup);
        maximumParticipantsGroup=(RadioGroup)view.findViewById(R.id.maximumParticipantsGroup);

        loginLayout.setVisibility(View.GONE);
        uploadLayout.setVisibility(View.GONE);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                verifyFrom=2;
                AuthModel model = AuthModel.fromJson(SharedPreferenceManager.getInstance().getString(Constant.AUTH_DATA,""));
                AuthenticationService.startAuthenticationVerifyService(getContext(),model.getAccessToken());
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignUp.class);
                getContext().startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                performLoginClick();
            }
        });

        setLayout();
    }

    private void performLoginClick(){
        AuthenticationService.startAuthenticationService(getContext(),username.getText().toString(),password.getText().toString());
    }

    private void performUpload(){
        verifyFrom=0;
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

        Boolean flag =true;
        if(txtKhelName.getText().toString().length()==0) {
            Toast.makeText(getContext(), "Please enter game name", Toast.LENGTH_LONG).show();
            flag=false;
        }
        else if(txtKhelDescription.getText().toString().length()==0){
            Toast.makeText(getContext(),"Please enter game description",Toast.LENGTH_LONG).show();
            flag=false;
        }
        else if(youtubeLink.getText().toString().length()==0){
            Toast.makeText(getContext(),"Please enter video link from youtube",Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(flag) {
            KhelModel model = new KhelModel();
            model.setName(txtKhelName.getText().toString());
            model.setDescription(txtKhelDescription.getText().toString());
            model.setFormation(formationButton.getText().toString());
            model.setIntensity(intensityRadio.getText().toString());
            model.setAudiance(audianceButton.getText().toString());
            model.setMinParticipants(minBtn.getText().toString());
            model.setMaxParticipants(maxBtn.getText().toString());
            model.setVideo(youtubeLink.getText().toString());


            AuthModel modelAuth = AuthModel.fromJson(SharedPreferenceManager.getInstance().getString(Constant.AUTH_DATA, ""));

            UploadNewGameService.startUploadNewGameService(getContext(), model.toJson().toString(), modelAuth.getAccessToken());
        }
    }

    private void cleanupUpload(){
        txtKhelDescription.setText("");
        txtKhelName.setText("");
        youtubeLink.setText("");
    }

    private void initBroadcastReceiver() {
        khelReceiver = new KhelReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());

        IntentFilter khelServiceFilter = new IntentFilter(Constant.actions.KHEL_AUTH);
        IntentFilter khelServiceFilterVerify = new IntentFilter(Constant.actions.KHEL_AUTH_VERIFY);
        IntentFilter khelServiceUpload = new IntentFilter(Constant.actions.KHEL_UPLOAD);

        localBroadcastManager.registerReceiver(khelReceiver, khelServiceFilter);
        localBroadcastManager.registerReceiver(khelReceiver, khelServiceFilterVerify);
        localBroadcastManager.registerReceiver(khelReceiver, khelServiceUpload);
    }

    // TODO: Rename and change types and number of parameters
    public static UploadNewGame newInstance() {
        UploadNewGame fragment = new UploadNewGame();
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
        View view=inflater.inflate(R.layout.fragment_upload_new_game, container, false);
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
