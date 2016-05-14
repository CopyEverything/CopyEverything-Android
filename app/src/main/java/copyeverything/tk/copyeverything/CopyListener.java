package copyeverything.tk.copyeverything;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;


/**
 * Created by Nathan on 2016-01-30.
 */


public class CopyListener extends Service {

    public static String lastSentString = "";
    private Socket mSocket;

    public LocalBroadcastManager broadcaster;
    static final public String CLIP_RESULT = "CopyListener.CLIP_HISTORY_UPDATED";

    public void startCopyListener() {
        ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                ClipData cd;
                ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (cb.hasPrimaryClip()) {
                    cd = cb.getPrimaryClip();
                    if(cd != null) {
                        //Currently we can only handle text
                        //TODO: support other clip types
                        String strClip = cd.getItemAt(0).getText().toString();
                        Clip<String> clip = new Clip<>(strClip);
                        //Check that listener isn't picking up the same clip twice
                        if(lastSentString.equalsIgnoreCase(strClip)){
                            return;
                        }
                        pushToDataBase(strClip);
                        pushToHistory(clip);
                    }
                }
            }
        };
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
    }

    private void pushToDataBase(String clip) {
        if(mSocket.connected()) {
            mSocket.emit("new client copy", clip);
            lastSentString = clip;
        }
    }

    private void pushToHistory(Clip c) {

        Intent intent = new Intent(CLIP_RESULT);

        //Set extras
        intent.putExtra("id", c.clipId.toString());
        intent.putExtra("content", c.content.toString());
        intent.putExtra("ts", c.timestamp);
        intent.putExtra("device", c.device);

        //Send to activity
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public void onCreate(){
        SocketDataBase database = (SocketDataBase) getApplication();
        mSocket = database.getSocket();

        //Set up broadcaster
        broadcaster = LocalBroadcastManager.getInstance(this);

        startCopyListener();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
