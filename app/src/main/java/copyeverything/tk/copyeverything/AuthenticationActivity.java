package copyeverything.tk.copyeverything;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nathan on 2016-01-30.
 */

public class AuthenticationActivity extends AppCompatActivity {

    public User user = new User();
    //private RetrieveUserToken token;
    Socket mSocket;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Bundle data = getIntent().getExtras();

        String mEmail = data.getString("email");
        String mPassword = data.getString("password");

        //Connect to socket
        SocketDataBase database = (SocketDataBase) getApplication();
        mSocket = database.getSocket();
        mSocket.connect();

        //Listen for authentication response
        mSocket.on("auth resp", handleAuthentication);

        attemptLogin(mEmail, mPassword);
        //token = new RetrieveUserToken(mEmail, mPassword);
        //token.execute();

        //authenticate(mEmail, mPassword);
    }

    private void attemptLogin(String username, String password) {
        try {
            //Put email and password to JSON
            JSONObject authdata = new JSONObject();
            authdata.put("username", username);
            authdata.put("password", password);

            //Send authentication data
            mSocket.emit("auth", authdata);
        }
        catch (Exception e) {
            Log.e("error",e.getMessage(),e);
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
                    //Go to MainActivity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Intent IncomingDataListener = new Intent(getApplicationContext(), IncomingDataListener.class);
                    getApplicationContext().startService(IncomingDataListener);
                    Intent CopyListener = new Intent(getApplicationContext(), CopyListener.class);
                    getApplicationContext().startService(CopyListener);
                    return;
                }
                else {
                    Intent back = new Intent(getApplicationContext(), Login.class);
                    startActivity(back);
                    return;
                }
            }
            catch (Exception e) {
                Log.d("error", e.getMessage(), e);
            }
        }
    };

    private void handleTokenRequest(String response) {
        JSONArray obj = null;

        String uid = "";
        String token = "";
        try {
            obj = new JSONArray(response);
            Boolean success = obj.getBoolean(0);

            if(success) {
                uid = obj.getString(1);
                token = obj.getString(2);
            }
            else{
                Intent back = new Intent(getApplicationContext(), Login.class);
                startActivity(back);
                return;
            }
        }
        catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return;
        }

        user.setInfo(token, uid);
        SharedPreferences settings = getSharedPreferences("CopyEverythingTemp", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(uid,token).apply();
        Firebase.setAndroidContext(this);
        FireBaseData.authUser();
        Intent test = new Intent(this, IncomingDataListener.class);
        this.startService(test);
        Intent nextTest = new Intent(this, CopyListener.class);
        this.startService(nextTest);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

