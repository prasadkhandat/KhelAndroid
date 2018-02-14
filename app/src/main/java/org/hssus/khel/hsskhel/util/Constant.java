package org.hssus.khel.hsskhel.util;

/**
 * Created by prasadkhandat on 1/2/18.
 */

public class Constant {
    public static String BASE_URL="http://khel.khandatprasad.com";
    public static String DATA="data";
    public static String GAME_ID="game_id";
    public static String USERNAME="username";
    public static String PASSWORD="password";
    public static String AUTH_DATA="auth_data";
    public static String AUTH_TOKEN="auth_token";

    public static String KHEL="khel";
    public static String KHEL_NAME="khel_name";
    public static String KHEL_DESCRIPTION="khel_description";
    public static String KHEL_FORMATION="khel_formation";
    public static String KHEL_INTENSITY="khel_intensity";
    public static String KHEL_AUDIANCE="khel_aud";
    public static String KHEL_MIN="khel_min";
    public static String KHEL_MAX="khel_max";
    public static String KHEL_LINK="khel_link";

    public interface actions
    {
        String KHEL_GET_DATA_ACTION="khel_data_action";
        String KHEL_DETAILS_ACTION="khel_details_action";
        String KHEL_AUTH="khel_auth";
        String KHEL_AUTH_VERIFY="khel_auth_verify";
        String KHEL_UPLOAD="khel_Upload";
        String KHEL_SIGN_UP="Khel_Sign_Up";
    }
    public interface extra
    {
        String ERROR="error";
        String ERROR_MESSAGE="error_message";
        String RESULTS="result";
    }
}
