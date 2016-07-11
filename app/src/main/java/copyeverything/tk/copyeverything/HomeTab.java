package copyeverything.tk.copyeverything;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by James on 2016-07-10.
 */
public class HomeTab extends Fragment {

    TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_home, container, false);

        // Modify connected text font
        mTextView = (TextView) v.findViewById(R.id.txtConnected);
        Typeface titleFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GT Pressura Bold.ttf");
        mTextView.setTypeface(titleFont);

        return v;
    }
}
