package copyeverything.tk.copyeverything;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import java.net.URISyntaxException;

/**
 * Created by Nathan on 2016-02-19.
 */
public class SocketDataBase extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://copyeverything.tk:80");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

}
