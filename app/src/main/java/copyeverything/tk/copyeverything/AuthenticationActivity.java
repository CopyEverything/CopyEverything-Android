package copyeverything.tk.copyeverything;

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

/**
 * Created by Nathan on 2016-01-30.
 */

public class AuthenticationActivity extends AppCompatActivity {

    public User user = new User();
    private RetrieveUserToken token;
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

        token = new RetrieveUserToken(mEmail, mPassword);
        token.execute();

        //authenticate(mEmail, mPassword);
    }

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



    class RetrieveUserToken extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        RetrieveUserToken(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                //Send authentication data
                mSocket.emit("auth", mEmail, mPassword);

                //Listen for response
                mSocket.on("auth", handleAuthentication);

                return null;
            }
            catch (Exception e) {
                Log.e("error",e.getMessage(),e);
                return null;
            }
            /*try {
                URL url = new URL("http://copyeverything.tk/auth.php");
                String urlParameters = "email=" + URLEncoder.encode(mEmail, "UTF-8") + "&pass=" + URLEncoder.encode(mPassword, "UTF-8");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", "Mozilla 5.0");
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                //Write the POST request
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = urlConnection.getResponseCode();
                System.out.println("Response Code : " + responseCode);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Log.d("StringBuilder", stringBuilder.toString());
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }*/
        }

        @Override
        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
            handleTokenRequest(response);
        }

        @Override
        protected void onCancelled() {

        }

        private Emitter.Listener handleAuthentication = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Toast.makeText(AuthenticationActivity.this, "Received server response", Toast.LENGTH_SHORT).show();
                //Perform your action - like adding the message to your adapter and showing it in your chat list
            }
        };
    }
}

