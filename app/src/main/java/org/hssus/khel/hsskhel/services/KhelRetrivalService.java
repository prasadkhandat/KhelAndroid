package org.hssus.khel.hsskhel.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.hssus.khel.hsskhel.network.RetrofitHandler;
import org.hssus.khel.hsskhel.util.Constant;
import org.hssus.khel.hsskhel.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasadkhandat on 1/2/18.
 */

public class KhelRetrivalService extends IntentService {
    private static final String TAG = KhelRetrivalService.class.getName();
    private static final String ACTION_GAME_LIST = "com.sikkanet.service.action.GAME_LIST";
    private static final String ACTION_GAME_DETAILS = "com.sikkanet.service.action.GAME_DETIALS";
    private static Context objContext;
    private static final String EXTRA_DATA = "com.sikkanet.service.extra.DATA";

    public KhelRetrivalService() {
        super(TAG);
    }

    public static void startActionKhelRetrivalResult(Context context,String frm,String itn,String aud,String mnp,String mxp) {
        objContext = context;
        Intent intent = new Intent(context, KhelRetrivalService.class);
        intent.setAction(ACTION_GAME_LIST);
        intent.putExtra(Constant.KHEL_FORMATION,frm);
        intent.putExtra(Constant.KHEL_INTENSITY,itn);
        intent.putExtra(Constant.KHEL_AUDIANCE,aud);
        intent.putExtra(Constant.KHEL_MIN,mnp);
        intent.putExtra(Constant.KHEL_MAX,mxp);
        //intent.putExtra(EXTRA_DATA, zipCode);
        context.startService(intent);
    }

    public static void startActionKhelDetailsRetrivalResult(Context context,String game_id) {
        objContext = context;
        Intent intent = new Intent(context, KhelRetrivalService.class);
        intent.setAction(ACTION_GAME_DETAILS);
        intent.putExtra(EXTRA_DATA, game_id);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GAME_LIST.equals(action)) {
                String frm=intent.getStringExtra(Constant.KHEL_FORMATION).toLowerCase().equals("all")?null:intent.getStringExtra(Constant.KHEL_FORMATION);
                String itn=intent.getStringExtra(Constant.KHEL_INTENSITY).toLowerCase().equals("all")?null:intent.getStringExtra(Constant.KHEL_INTENSITY);
                String aud=intent.getStringExtra(Constant.KHEL_AUDIANCE).toLowerCase().equals("all")?null:intent.getStringExtra(Constant.KHEL_AUDIANCE);
                String mnp=intent.getStringExtra(Constant.KHEL_MIN).toLowerCase().equals("all")?null:intent.getStringExtra(Constant.KHEL_MIN);
                String mxp=intent.getStringExtra(Constant.KHEL_MAX).toLowerCase().equals("all")?null:intent.getStringExtra(Constant.KHEL_MAX);
                handleActionFeeSurveyResult(frm,itn,aud,mnp,mxp);
            } else if (ACTION_GAME_DETAILS.equals(action)){
                handleActionGameDetailsResult(intent.getStringExtra(KhelRetrivalService.EXTRA_DATA));
            }
        }
    }

    private void handleActionFeeSurveyResult(String frm,String itn,String aud,String mnp,String mxp) {

        RetrofitHandler.getInstance().getUserDetails(frm,itn,aud,mnp,mxp).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.raw().request().url().toString());
                if (response.isSuccessful()) {
                    sendBroadcast(false, null, response.body(),Constant.actions.KHEL_GET_DATA_ACTION);
                } else {

                    sendBroadcast(true, "Error getting data...", null,Constant.actions.KHEL_GET_DATA_ACTION);

                    Log.e(TAG, "handleActionFeeSurveyResult-onResponse: " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "handleActionFeeSurveyResult-onFailure: " + t.getLocalizedMessage());
                sendBroadcast(true, "Error getting data...", null,Constant.actions.KHEL_GET_DATA_ACTION);
            }
        });
    }

    private void handleActionGameDetailsResult(String game_id) {

        RetrofitHandler.getInstance().getGameDetails(game_id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.raw().request().url().toString());
                if (response.isSuccessful()) {
                    sendBroadcast(false, null, response.body(),Constant.actions.KHEL_DETAILS_ACTION);
                } else {

                    sendBroadcast(true, "Error getting data...", null,Constant.actions.KHEL_DETAILS_ACTION);

                    Log.e(TAG, "handleActionFeeSurveyResult-onResponse: " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "handleActionFeeSurveyResult-onFailure: " + t.getLocalizedMessage());
                sendBroadcast(true, "Error getting data...", null,Constant.actions.KHEL_DETAILS_ACTION);
            }
        });
    }

    private void sendBroadcast(boolean isError, String errorMessage, String data,String action) {
        Intent localIntent = new Intent(action);
        try {
            localIntent.putExtra(Constant.extra.ERROR, isError);
            if (Util.isNotNullAndNotEmpty(errorMessage)) {
                localIntent.putExtra(Constant.extra.ERROR_MESSAGE, errorMessage);
            }else {


                JSONObject jsonObject = new JSONObject();
                if (action.equals(Constant.actions.KHEL_GET_DATA_ACTION)) {

                    JSONArray jsonArray = new JSONArray(data);
                    jsonObject.put(Constant.DATA, jsonArray);
                }
                else {
                    //JSONObject jsonArray = new JSONObject(data);
                    //jsonObject.put(Constant.DATA, jsonArray);
                    jsonObject = new JSONObject(data);
                }
                localIntent.putExtra(Constant.extra.RESULTS, jsonObject.toString());
            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }
        LocalBroadcastManager.getInstance(KhelRetrivalService.this).sendBroadcast(localIntent);
    }
}
