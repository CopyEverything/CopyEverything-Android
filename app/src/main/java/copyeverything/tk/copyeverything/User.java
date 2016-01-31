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
    public static boolean loggedIn = false;

    public static void setInfo(AuthData authData){
        Uid = authData.getUid();
        Token = authData.getToken();
        loggedIn = true;
    }
}
