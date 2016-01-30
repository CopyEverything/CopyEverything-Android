package copyeverything.tk.copyeverything;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Nathan on 2016-01-30.
 */
public class StartListeners extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent){
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent serviceIntent = new Intent(context, IncomingDataListener.class);
            context.startService(serviceIntent);
        }
    }
}
