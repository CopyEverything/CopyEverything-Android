package copyeverything.tk.copyeverything;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by James on 5/8/2016.
 */

public class ClipHistoryAdapter extends ArrayAdapter<Clip> {

    public ClipHistoryAdapter (Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ClipHistoryAdapter(Context context, int resource, List<Clip> clips) {
        super(context, resource, clips);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        //Get clip
        Clip p = getItem(position);

        if (p != null) {
            TextView info = (TextView) v.findViewById(R.id.clipInfo);
            TextView content = (TextView) v.findViewById(R.id.clipContent);

            if (info != null) {
                //Get Clip Date String
                Date d = new Date();
                d.setTime(p.timestamp);
                String strDate = new SimpleDateFormat("dd/MM/yyyy").format(d);
                String strTime = new SimpleDateFormat("HH:mm:ss").format(d);
                info.setText("Copied from: " + p.device + " at " + strTime + " on " + strDate);
            }

            if (content != null) {
                content.setText(p.content.toString());
            }
        }

        return v;
    }
}
