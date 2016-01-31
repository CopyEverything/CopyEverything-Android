package copyeverything.tk.copyeverything;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * Created by Nathan on 2016-01-30.
 */
public class IncomingDataListener extends IntentService {

    public static String lastRecievedString = "";

    public IncomingDataListener(){
        super("IncomingDataListener");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Query queryRef = FireBaseData.fire.orderByChild("timestamp").limitToLast(1);

        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                String a = snapshot.getChildren().iterator().next().getValue().toString();

                //Paste p = a.getValue(Paste.class);
                    if(lastRecievedString.equalsIgnoreCase(a)){
                        return;
                    }
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", a);
                    clipboard.setPrimaryClip(clip);
                    lastRecievedString = a;
                    //Log.w("Test", p.getContent());


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.w("Test", "Disconnected");

            }
        });
    }
}
