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
import com.github.nkzawa.socketio.client.Socket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by Nathan on 2016-01-30.
 */


public class CopyListener extends Service {

    private Socket mSocket;

    public void startCopyListener() {
        ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                pushToDataBase();
            }
        };
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
    }

    private void pushToDataBase() {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            if(cd != null){
                if(IncomingDataListener.lastReceivedString.equalsIgnoreCase(cd.getItemAt(0).getText().toString())){
                    return;
                }
                if(mSocket.connected()){
                    Log.w("Test", cd.getItemAt(0).toString());
                    mSocket.emit("new client copy", cd.getItemAt(0).getText().toString());
                }
            }
        }
    }


    @Override
    public void onCreate(){
        SocketDataBase database = (SocketDataBase) getApplication();
        mSocket = database.getSocket();
        startCopyListener();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
