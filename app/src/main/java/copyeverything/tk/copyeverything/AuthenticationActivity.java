package copyeverything.tk.copyeverything;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class AuthenticationActivity extends Activity {

    Socket mSocket;
    Boolean isAuth = false;
    String mEmail;
    String mPassword;

    TimerTask hTimer = new TimerTask() {
        @Override
        public void run() {
            if(!isAuth) {
                //Abort Login
                runLoginIntent("Authentication Timed Out");
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Bundle data = getIntent().getExtras();

        mEmail = data.getString("email");
        mPassword = data.getString("password");

        attemptLogin(mEmail, mPassword);

        Timer timer = new Timer();
        timer.schedule(hTimer, 20000);
    }

    public void attemptLogin(String username, String password) {
        try {
            //Connect to socket
            SocketDataBase database = (SocketDataBase) getApplication();
            mSocket = database.getSocket();
            mSocket.connect();

            //Listen for authentication response
            mSocket.on("auth resp", handleAuthentication);

            //Put email and password to JSON
            JSONObject authdata = new JSONObject();
            authdata.put("username", username);
            authdata.put("password", password);

            //Send authentication data
            mSocket.emit("auth", authdata);

        } catch (Exception e) {
            Log.e("error", e.getMessage(), e);
        }
    }

    private Emitter.Listener handleAuthentication = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            try {
                //Perform your action - like adding the message to your adapter and showing it in your chat list
                Log.d("response", args[0].toString());

                JSONArray response = (JSONArray) args[0];

                if(response.getBoolean(0)) {

                    isAuth = true;
                    Context ctx = getApplicationContext();
                    saveCredentials();

                    Intent IncomingDataListener = new Intent(ctx, IncomingDataListener.class);
                    ctx.startService(IncomingDataListener);
                    Intent CopyListener = new Intent(ctx, CopyListener.class);
                    ctx.startService(CopyListener);

                    //Go to MainActivity
                    Intent intent = new Intent(ctx, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    runLoginIntent("Error: Authentication Failed");
                }
            }
            catch (Exception e) {
                Log.d("error", e.getMessage(), e);
            }
        }
    };

    private void runLoginIntent(String errorMessage) {

        Intent back = new Intent(getApplicationContext(), Login.class);
        back.putExtra("error", errorMessage);
        startActivity(back);
    }

    private void saveCredentials() {
        //Saves email and password in plain text
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //TODO: Save AuthTokens vs plaintext info
        editor.putString(getString(R.string.saved_email), mEmail);
        editor.putString(getString(R.string.saved_pass), mPassword);
        editor.apply();
    }
}

