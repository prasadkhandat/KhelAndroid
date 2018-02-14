package org.hssus.khel.hsskhel.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.hssus.khel.hsskhel.models.ErrorMessage;
import org.hssus.khel.hsskhel.models.SignUpModel;
import org.hssus.khel.hsskhel.network.RetrofitHandler;
import org.hssus.khel.hsskhel.util.Constant;
import org.hssus.khel.hsskhel.util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasadkhandat on 2/14/18.
 */

public class SingupService extends IntentService {
    private static final String TAG = SingupService.class.getName();
    private static final String ACTION_SIGN_UP = "com.sikkanet.service.action.SIGNUP";
    private static Context objContext;

    public SingupService() {
        super(TAG);
    }

    public static void startSingupService(Context context, String user) {
        objContext = context;
        Intent intent = new Intent(context, SingupService.class);
        intent.setAction(ACTION_SIGN_UP);
        intent.putExtra(Constant.USERNAME, user);
        //intent.putExtra(EXTRA_DATA, zipCode);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SIGN_UP.equals(action)) {
                handleActionUploadNewGame(SignUpModel.fromJson(intent.getStringExtra(Constant.USERNAME)));
            }
        }
    }

    private void handleActionUploadNewGame(SignUpModel model) {

        RetrofitHandler.getInstance().SignUP(model).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    sendBroadcast(false, null, "ok");
                } else {

                    ErrorMessage message;
                    try {
                        message = ErrorMessage.fromJson(response.errorBody().string().toString());
                    }catch (Exception ex){message= new ErrorMessage();
                        message.setMessage("Error while posting data");}
                    sendBroadcast(true, message.getMessage(), null);

                    Log.e(TAG, "handleActionFeeSurveyResult-onResponse: " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sendBroadcast(true, "Error with server", null);
            }
        });

    }

    private void sendBroadcast(boolean isError, String errorMessage, String data) {
        Intent localIntent = new Intent(Constant.actions.KHEL_SIGN_UP);
        try {
            localIntent.putExtra(Constant.extra.ERROR, isError);
            if (Util.isNotNullAndNotEmpty(errorMessage)) {
                localIntent.putExtra(Constant.extra.ERROR_MESSAGE, errorMessage);
            } else {


                localIntent.putExtra(Constant.extra.RESULTS, data);

            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }
        LocalBroadcastManager.getInstance(SingupService.this).sendBroadcast(localIntent);
    }
}