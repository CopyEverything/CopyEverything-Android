package copyeverything.tk.copyeverything;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.AuthData;

/**
 * Created by Nathan on 2016-01-30.
 */
public class User {

    private static String Uid;
    private static String Token;

    public static void setInfo(AuthData authData){
        Uid = authData.getUid();
        Token = authData.getToken();
    }

    public static String getToken(){
        return Token;
    }

    public static String getUid(){
        return Uid;
    }
}
