package copyeverything.tk.copyeverything;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by James on 2016-07-10.
 */
public class HistoryTab extends Fragment {

    private ArrayList<Clip> clipboardHistory;
    private ClipHistoryAdapter adapter;

    //Setup clip receiver for updates
    private BroadcastReceiver clipReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_history, container, false);

        //Setup Clipboard History
        clipboardHistory = new ArrayList<>();

        //Set Array adapter for history list
        ListView history = (ListView) v.findViewById(R.id.clipHistory);
        adapter = new ClipHistoryAdapter(getActivity().getApplicationContext(), R.id.clipHistory, clipboardHistory);
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

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(clipReceiver,
                new IntentFilter(CopyListener.CLIP_RESULT)
        );
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(clipReceiver);
        super.onStop();
    }

    public void addClipToHistory(Clip c) {
        clipboardHistory.add(c);
    }
}
