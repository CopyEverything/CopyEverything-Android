package copyeverything.tk.copyeverything;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class Authenticator {

    Activity hActivity;
    Socket mSocket;
    Boolean isAuth = false;

    //Timeout handler
    TimerTask hTimeout = new TimerTask() {
        @Override
        public void run() {
            if(!isAuth) {
                //Abort Login
                mSocket.close();
                callback.failure("Authentication Timed Out");
            }
        }
    };

    interface LoginCallback {
        void success();
        void failure(String errorMessage);
    }
    LoginCallback callback;

    Authenticator(Activity activity, LoginCallback cb) {
        hActivity = activity;
        callback = cb;

        //Get the database socket
        SocketDataBase database = (SocketDataBase) hActivity.getApplication();
        mSocket = database.getSocket();
    }

    public void attemptLogin(String username, String password) {
        try {
            //Connect to socket
            mSocket.connect();

            //Listen for authentication response
            mSocket.on("auth resp", handleAuthentication);

            //Put email and password to JSON
            JSONObject authdata = new JSONObject();
            authdata.put("username", username);
            authdata.put("password", password);

            //Send authentication data
            mSocket.emit("auth", authdata);

            Timer timer = new Timer();
            timer.schedule(hTimeout, 20000);

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
                    callback.success();
                }
                else {
                    mSocket.close();
                    callback.failure("Authentication Failed: Invalid Credentials.");
                }
            }
            catch (Exception e) {
                Log.d("error", e.getMessage(), e);
                mSocket.close();
                callback.failure("Authentication Failed: Unknown Error.");
            }
        }
    };
}

