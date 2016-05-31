package copyeverything.tk.copyeverything;

import android.app.Application;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Nathan on 2016-02-19.
 */
public class SocketDataBase extends Application {

    private Socket mSocket;
    {
        try {
            IO.Options options = new IO.Options();
            options.multiplex = false;
            options.secure = true;

            //TODO: Convert to copyeverythingapp.com website (when up)
            mSocket = IO.socket("http://copyeverything.tk", options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

}
