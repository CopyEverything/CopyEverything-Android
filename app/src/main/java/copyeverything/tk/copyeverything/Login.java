package copyeverything.tk.copyeverything;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import io.fabric.sdk.android.Fabric;

public class Login extends Activity implements LoaderCallbacks<Cursor> {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    /*private static final String TWITTER_KEY = "oiDW4pNWdQA8PTtWnQb5dL3Hz";
    private static final String TWITTER_SECRET = "oAOLnyT8AeTRvMIWg1fbsmffGf2cSm2wSf3IoAOD4WmnBq237l";
    private TwitterAuthClient mTwitterAuthClient;
    private Button mTwitterLoginButton;

    //Facebook stuff
    private CallbackManager callbackManager;
    private Button mFacebookLoginButton;*/

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private LinearLayout mLoginForm;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mLoginButton;

    Boolean mIsAuth = false;
    TextView mAuthTextView;
    Handler mHandle = new Handler();
    Runnable animateAuthText = new Runnable() {
        @Override
        public void run() {

            if(mAuthTextView == null)
                mAuthTextView = (TextView) findViewById(R.id.txtAuthenticating);

            if(mAuthTextView.getVisibility() != View.VISIBLE)
                return;

            String txt = mAuthTextView.getText().toString();

            int dotCount = 0;
            for( int i=0; i<txt.length(); i++ ) {
                if( txt.charAt(i) == '.' ) {
                    dotCount++;
                }
            }

            if (dotCount < 3) {
                txt = txt.trim() + ".";
                while (txt.length() < 17) {txt += ' ';} //Adds whitespace to always produce 17 character string
            }
            else
                txt = "Authenticating   ";

            mAuthTextView.setText(txt);

            if (!mIsAuth)
                mHandle.postDelayed(animateAuthText, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        mTwitterAuthClient = new TwitterAuthClient();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Login.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });*/

        setContentView(R.layout.activity_login_bolden);

        /*mTwitterLoginButton = (Button) findViewById(R.id.twitter_login_button);
        mTwitterLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            mTwitterAuthClient.authorize((Activity) getApplicationContext(), new com.twitter.sdk.android.core.Callback<TwitterSession>() {

                @Override
                public void success(Result<TwitterSession> twitterSessionResult) {
                    // The TwitterSession is also available through:
                    // Twitter.getInstance().core.getSessionManager().getActiveSession()
                    TwitterSession session = twitterSessionResult.data;
                    // TODO: Remove toast and use the TwitterSession's userID
                    // with your app's user model
                    String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(TwitterException e) {
                    Log.d("TwitterKit", "Login with Twitter failure", e);
                }
            });

            }
        });

        mFacebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
        mFacebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Login method
                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
            }
        });*/

        // Set up the login form.
        mLoginForm = (LinearLayout) findViewById(R.id.login_form);
        mAuthTextView = (TextView) findViewById(R.id.txtAuthenticating);
        TextView txtCopyView = (TextView) findViewById(R.id.titleCopy);
        TextView txtEverythingView = (TextView) findViewById(R.id.titleEverything);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/GT Pressura Bold.ttf");
        txtCopyView.setTypeface(titleFont);
        txtEverythingView.setTypeface(titleFont);

        Typeface maisonNeueLight = Typeface.createFromAsset(getAssets(), "fonts/MaisonNeue-Light.ttf");
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView.setTypeface(maisonNeueLight);
        mPasswordView.setTypeface(maisonNeueLight);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Animation clickAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bolden_button_click);
                view.startAnimation(clickAnimation);

                Handler h = new Handler();
                Runnable r = new Runnable() {
                    public void run() {
                        view.clearAnimation();
                    }
                };
                h.postDelayed(r, 150);

                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                attemptLogin();
            }
        });

        //Debug
        mEmailView.setText(R.string.test_email);
        mPasswordView.setText(R.string.test_password);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            //If empty go to register page
            //Uri registerPageUri = Uri.parse("https://copyeverything.tk");
            //Intent redirectIntent = new Intent(Intent.ACTION_VIEW, registerPageUri);
            //startActivity(redirectIntent);
            focusView = mEmailView;
            mEmailView.setError(getString(R.string.error_field_required));
            cancel = true;

        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;

        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            Authenticator mAuthManager = new Authenticator(this, new Authenticator.LoginCallback() {
                @Override
                public void success() {
                    mIsAuth = true;
                    saveCredentials(email, password);
                    Context ctx = getApplicationContext();

                    Intent IncomingDataListener = new Intent(ctx, IncomingDataListener.class);
                    ctx.startService(IncomingDataListener);
                    Intent CopyListener = new Intent(ctx, CopyListener.class);
                    ctx.startService(CopyListener);

                    //Go to MainActivity
                    Intent intent = new Intent(ctx, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void failure(String errorMessage) {
                    Toast.makeText(Login.this, errorMessage, Toast.LENGTH_LONG).show();
                    mLoginForm.setVisibility(View.VISIBLE);
                    mAuthTextView.setText(R.string.txt_auth_default);
                    mAuthTextView.setVisibility(View.GONE);
                }
            });
            mAuthManager.attemptLogin(email, password);

            mLoginForm.setVisibility(View.GONE);
            mAuthTextView.setVisibility(View.VISIBLE);

            mHandle.postDelayed(animateAuthText, 500);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    //For twitter login
    /*@Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        mTwitterAuthClient.onActivityResult(requestCode, responseCode, intent);
    }*/

    private void saveCredentials(String email, String password) {
        //Saves email and password in plain text
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //TODO: Save AuthTokens vs plaintext info
        editor.putString(getString(R.string.saved_email), email);
        editor.putString(getString(R.string.saved_pass), password);
        editor.apply();
    }
}

