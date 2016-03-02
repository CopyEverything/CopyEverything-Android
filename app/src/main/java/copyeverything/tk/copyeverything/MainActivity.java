package copyeverything.tk.copyeverything;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Nathan on 2016-01-30.
 */
public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the login form.
        mTextView = (TextView) findViewById(R.id.txtCopies);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/LobsterTwo-Bold.otf");
        mTextView.setTypeface(titleFont);
    }
}
