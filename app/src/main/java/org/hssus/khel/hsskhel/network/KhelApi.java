package org.hssus.khel.hsskhel.network;

import org.hssus.khel.hsskhel.models.KhelModel;
import org.hssus.khel.hsskhel.models.SignUpModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by prasadkhandat on 1/2/18.
 */

public interface KhelApi {

    @GET("/v1/games")
    Call<String> getUserDetails(@Query("frm")String frm,@Query("itn") String itn,@Query("aud") String aud,@Query("mnp") String mnp,@Query("mxp") String mxp);

    @GET("/v1/games")
    Call<String> getGameDetails(@Query("gameid")String gameid);

    @FormUrlEncoded
    @POST("/v1/authenticate")
    Call<String> authenticate(@Field("grant_type") String grant_type,@Field("username")String username,@Field("password")String password);

    @GET("/v1/VerifyToken")
    Call<String> verifyToken(@Header("Authorization")String token);

    @POST("/v1/games")
    Call<String> postNewGame(@Header("Authorization")String token, @Body KhelModel model);

    @POST("/v1/Account")
    Call<String> SignUP(@Body SignUpModel user);
}
