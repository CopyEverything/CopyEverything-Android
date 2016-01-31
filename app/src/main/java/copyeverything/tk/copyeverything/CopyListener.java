package copyeverything.tk.copyeverything;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.Firebase;


/**
 * Created by Nathan on 2016-01-30.
 */


public class CopyListener extends Service {

    public static String currentContents = "";
    private Firebase fire;


    public void startCopyListener() {
        Log.w("Test", "Something Started");
        Firebase.setAndroidContext(this);
        fire = new Firebase("https://vivid-inferno-6279.firebaseio.com/");
        ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                addToClipboard();
            }
        };
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
    }

    private void addToClipboard() {

        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip() && IncomingDataListener.isComingDown != true) {
            ClipData cd = cb.getPrimaryClip();
            if(cd != null){
                currentContents = cd.getItemAt(0).toString();
                Log.w("Test", cd.getItemAt(0).toString());
                fire.child("test").setValue(cd.getItemAt(0));
            }


        }
    }



    public void onCreate(){
        Log.w("Test", "Something First");
        startCopyListener();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
