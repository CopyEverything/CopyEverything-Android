package copyeverything.tk.copyeverything;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.CustomView;
import com.gc.materialdesign.views.ScrollView;

import java.util.ArrayList;

/**
 * Created by Nathan on 2016-01-30.
 */
public class MainActivity extends Activity {

    private Context ctx;
    private TextView mTextView;

    private ArrayList<Clip> clipboardHistory;
    private ClipHistoryAdapter adapter;

    //Setup clip receiver for updates
    private BroadcastReceiver clipReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = getApplicationContext();

        // Modify connected text font
        mTextView = (TextView) findViewById(R.id.txtConnected);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/LobsterTwo-Bold.otf");
        mTextView.setTypeface(titleFont);

        //Setup Clipboard History
        clipboardHistory = new ArrayList<>();

        //Set Array adapter for history list
        ListView history = (ListView) findViewById(R.id.clipHistory);
        adapter = new ClipHistoryAdapter(ctx, R.id.clipHistory, clipboardHistory);
        history.setAdapter(adapter);

        //Setup clip receiver
        clipReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle data = intent.getExtras();
                String clipID = data.getString("id");
                String content = data.getString("content");
                long timestamp = data.getLong("ts");
                String device = data.getString("device");

                Clip clip = new Clip<>(clipID, content, timestamp, device);
                clipboardHistory.add(clip);

                adapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(clipReceiver,
                new IntentFilter(CopyListener.CLIP_RESULT)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(clipReceiver);
        super.onStop();
    }

    public void addClipToHistory(Clip c) {
        clipboardHistory.add(c);
    }
}
