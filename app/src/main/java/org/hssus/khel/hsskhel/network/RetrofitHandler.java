package org.hssus.khel.hsskhel.network;

import org.hssus.khel.hsskhel.models.KhelModel;
import org.hssus.khel.hsskhel.models.SignUpModel;
import org.hssus.khel.hsskhel.util.Constant;
import org.hssus.khel.hsskhel.util.StringConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prasadkhandat on 1/2/18.
 */

public class RetrofitHandler {

    private KhelApi khelApi;
    private Retrofit khelRetrofit;

    private static RetrofitHandler ourInstance = new RetrofitHandler();
    public static RetrofitHandler getInstance() {
        return ourInstance;
    }

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .build();

    private RetrofitHandler() {


        khelRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        khelApi = khelRetrofit.create(KhelApi.class);
    }

    public Call<String> getUserDetails(String frm, String itn, String aud, String mnp, String mxp){
        return khelApi.getUserDetails(frm,itn,aud,mnp,mxp);
    }

    public Call<String> getGameDetails(String gameid){
        return khelApi.getGameDetails(gameid);
    }

    public Call<String> authenticate(String username,String password){
        return khelApi.authenticate("password",username,password);
    }

    public Call<String> verifyToken(String token){
        return khelApi.verifyToken("bearer "+token);
    }

    public Call<String> postNewGame(String token,KhelModel model){
        return khelApi.postNewGame("bearer "+token,model);
    }

    public Call<String> SignUP(SignUpModel user){
        return khelApi.SignUP(user);
    }
}
