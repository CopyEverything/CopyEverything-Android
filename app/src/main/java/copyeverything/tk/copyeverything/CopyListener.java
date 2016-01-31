package copyeverything.tk.copyeverything;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by Nathan on 2016-01-30.
 */


public class CopyListener extends Service {



    public void startCopyListener() {
        ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                pushToFireBaseClipboard();
            }
        };
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
    }





    private void pushToFireBaseClipboard() {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            if(cd != null){
                Log.w("Test", cd.getItemAt(0).toString());
                Map<String,Object> Paste = new HashMap<String, Object>();
                Paste.put("content", cd.getItemAt(0).getText().toString());

                Paste.put("timestamp", new Date().getTime());
                FireBaseData.fire.child("" + (IncomingDataListener.idNumber + 1)).updateChildren(Paste);
                IncomingDataListener.idNumber = IncomingDataListener.idNumber + 1;
            }
        }
    }



    public void onCreate(){
        startCopyListener();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
