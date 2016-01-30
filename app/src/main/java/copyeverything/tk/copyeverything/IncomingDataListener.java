package copyeverything.tk.copyeverything;

import android.app.IntentService;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Nathan on 2016-01-30.
 */
public class IncomingDataListener extends IntentService {

    public IncomingDataListener(){
        super("IncomingDataListener");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://vivid-inferno-6279.firebaseio.com/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.w("Test", snapshot.getValue().toString());

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.w("Test", "Disconnected");;
            }
        });

    }
}
