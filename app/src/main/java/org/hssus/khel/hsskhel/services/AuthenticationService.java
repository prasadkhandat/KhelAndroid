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
 * Created by prasadkhandat on 2/11/18.
 */

public class AuthenticationService extends IntentService {
    private static final String TAG = AuthenticationService.class.getName();
    private static final String ACTION_Authenticate = "com.Khel.service.action.Authenticate";
    private static final String ACTION_AUTHENTICATE_VERIFY = "com.Khel.service.action.AuthenticateVerify";
    //private Context context;

    public AuthenticationService() {
        super(TAG);
    }

    public static void startAuthenticationService(Context context,String username,String password) {
        //this.context = context;
        Intent intent = new Intent(context, AuthenticationService.class);
        intent.setAction(ACTION_Authenticate);
        intent.putExtra(Constant.USERNAME, username);
        intent.putExtra(Constant.PASSWORD, password);
        context.startService(intent);
    }

    public static void startAuthenticationVerifyService(Context context,String token) {
        //this.context = context;
        Intent intent = new Intent(context, AuthenticationService.class);
        intent.setAction(ACTION_AUTHENTICATE_VERIFY);
        intent.putExtra(Constant.AUTH_TOKEN, token);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_Authenticate.equals(action)) {
                handleAuthentication(intent.getStringExtra(Constant.USERNAME),intent.getStringExtra(Constant.PASSWORD));
            } else if(ACTION_AUTHENTICATE_VERIFY.equals(action)){
                handleAuthenticationVerify(intent.getStringExtra(Constant.AUTH_TOKEN));
            }
        }
    }

    private void handleAuthenticationVerify(String token){

        RetrofitHandler.getInstance().verifyToken(token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.raw().request().url().toString());
                if (response.isSuccessful() && response.code()==200) {
                    sendBroadcast(false, null, "ok",Constant.actions.KHEL_AUTH_VERIFY);
                } else {

                    sendBroadcast(true, "Unauthorized", null,Constant.actions.KHEL_AUTH_VERIFY);

                    Log.e(TAG, "handleActionFeeSurveyResult-onResponse: " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "handleActionFeeSurveyResult-onFailure: " + t.getLocalizedMessage());
                sendBroadcast(true, "Unauthorized", null,Constant.actions.KHEL_AUTH_VERIFY);
            }
        });

    }

    private void handleAuthentication(String username,String Password){

        RetrofitHandler.getInstance().authenticate(username,Password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, response.raw().request().url().toString());
                if (response.isSuccessful()) {
                    sendBroadcast(false, null, response.body(),Constant.actions.KHEL_AUTH);
                } else {

                    sendBroadcast(true, "Error getting data...", null,Constant.actions.KHEL_AUTH);

                    Log.e(TAG, "handleActionFeeSurveyResult-onResponse: " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "handleActionFeeSurveyResult-onFailure: " + t.getLocalizedMessage());
                sendBroadcast(true, "Error getting data...", null,Constant.actions.KHEL_AUTH);
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


                if(action.equals(Constant.actions.KHEL_AUTH)) {
                    JSONObject jsonObject = new JSONObject(data);
                    localIntent.putExtra(Constant.extra.RESULTS, jsonObject.toString());
                }else {
                    localIntent.putExtra(Constant.extra.RESULTS, data);
                }
            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }
        LocalBroadcastManager.getInstance(AuthenticationService.this).sendBroadcast(localIntent);
    }
}