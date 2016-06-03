package copyeverything.tk.copyeverything;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketDataBase extends Application {

    private Socket mSocket;
    {
        try {
            IO.Options options = new IO.Options();
            options.multiplex = false;
            options.secure = true;

            //TODO: Convert to copyeverythingapp.com website (when up)
            mSocket = IO.socket("https://copyeverything.tk", options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

}
