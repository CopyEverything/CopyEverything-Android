package copyeverything.tk.copyeverything;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.firebase.client.*;
import com.firebase.client.Firebase;

/**
 * This is for starting the listeners when the app starts up
 */
public class StartListeners extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent){
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Firebase.setAndroidContext(context);
            Intent incomingServiceIntent = new Intent(context, IncomingDataListener.class);
            context.startService(incomingServiceIntent);
            Intent copyServiceIntent = new Intent(context, CopyListener.class);
            context.startService(copyServiceIntent);
        }
    }
}
