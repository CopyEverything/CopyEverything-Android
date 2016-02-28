package copyeverything.tk.copyeverything;

import android.app.Application;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nathan on 2016-01-30.
 */
public class IncomingDataListener extends Service {

    public static String lastReceivedString = "";
    private Socket mSocket;

    @Override
    public void onCreate() {
        SocketDataBase database = (SocketDataBase) getApplication();
        mSocket = database.getSocket();
        mSocket.on("new server copy", getPaste);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy(){

    }
    private Emitter.Listener getPaste = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String a = (String)args[0];
            if(lastReceivedString.equalsIgnoreCase(a)){
                return;
            }
            Log.w("Got Data", a);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", a);
            clipboard.setPrimaryClip(clip);
            lastReceivedString = a;
        }
    };
}
