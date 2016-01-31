package copyeverything.tk.copyeverything;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.firebase.client.*;
import com.firebase.client.Firebase;

/**
 * Created by Nathan on 2016-01-30.
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
