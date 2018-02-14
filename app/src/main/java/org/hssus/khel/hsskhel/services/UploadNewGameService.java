package org.hssus.khel.hsskhel.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.hssus.khel.hsskhel.models.KhelModel;
import org.hssus.khel.hsskhel.network.RetrofitHandler;
import org.hssus.khel.hsskhel.util.Constant;
import org.hssus.khel.hsskhel.util.Util;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasadkhandat on 2/14/18.
 */

public class UploadNewGameService  extends IntentService {
    private static final String TAG = UploadNewGameService.class.getName();
    private static final String ACTION_UPLOAD_GAME = "com.sikkanet.service.action.UPLOAD_GAME";
    private static Context objContext;

    public UploadNewGameService() {
        super(TAG);
    }

    public static void startUploadNewGameService(Context context,String khel,String token) {
        objContext = context;
        Intent intent = new Intent(context, UploadNewGameService.class);
        intent.setAction(ACTION_UPLOAD_GAME);
        intent.putExtra(Constant.KHEL,khel);
        intent.putExtra(Constant.AUTH_TOKEN,token);
        //intent.putExtra(EXTRA_DATA, zipCode);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_GAME.equals(action)) {
                handleActionUploadNewGame(KhelModel.fromJson(intent.getStringExtra(Constant.KHEL)),intent.getStringExtra(Constant.AUTH_TOKEN).toString());
            }
        }
    }

    private void handleActionUploadNewGame(KhelModel model,String token){

        RetrofitHandler.getInstance().postNewGame(token,model).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.code()==200) {
                    sendBroadcast(false, null, "ok",Constant.actions.KHEL_UPLOAD);
                } else {

                    sendBroadcast(true, "error", null,Constant.actions.KHEL_UPLOAD);

                    Log.e(TAG, "handleActionFeeSurveyResult-onResponse: " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                sendBroadcast(true, "error", null,Constant.actions.KHEL_UPLOAD);
            }
        });

    }

    private void sendBroadcast(boolean isError, String errorMessage, String data,String action) {
        Intent localIntent = new Intent(Constant.actions.KHEL_UPLOAD);
        try {
            localIntent.putExtra(Constant.extra.ERROR, isError);
            if (Util.isNotNullAndNotEmpty(errorMessage)) {
                localIntent.putExtra(Constant.extra.ERROR_MESSAGE, errorMessage);
            }else {


                localIntent.putExtra(Constant.extra.RESULTS, data);

            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }
        LocalBroadcastManager.getInstance(UploadNewGameService.this).sendBroadcast(localIntent);
    }
}