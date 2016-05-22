package copyeverything.tk.copyeverything;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

/**
 * This is for starting the listeners when the app starts up
 */
public class StartListeners extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent){

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){

            //Get login data
            //SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            //String email = sharedPreferences.getString(context.getString(R.string.saved_email), "");
            //String password = sharedPreferences.getString(context.getString(R.string.saved_pass), "");

            //AuthenticationActivity authenticator = new AuthenticationActivity();

            //authenticator.attemptLogin(email, password);

        }
    }
}
