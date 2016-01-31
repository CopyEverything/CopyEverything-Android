package copyeverything.tk.copyeverything;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Nathan on 2016-01-30.
 */

public class AuthenticationActivity extends Activity {

    public User user = new User();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Bundle data = getIntent().getExtras();

        String mEmail = data.getString("email");
        String mPassword = data.getString("password");

        Authenticate(mEmail, mPassword);
    }

    public void IsAuthenticated() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Authenticate(String mEmail, String mPassword) {
        Firebase.setAndroidContext(getApplicationContext());
        Firebase ref = new Firebase("https://vivid-inferno-6279.firebaseio.com/");
        ref.authWithPassword(mEmail, mPassword,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authentication just completed successfully :)
                        user.setInfo(authData);
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Token", authData.getToken());
                        editor.putString("UID", authData.getUid());
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        Toast.makeText(getApplicationContext(), "Denied", Toast.LENGTH_SHORT);

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
