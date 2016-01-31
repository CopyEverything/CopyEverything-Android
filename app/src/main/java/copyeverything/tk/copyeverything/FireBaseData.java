package copyeverything.tk.copyeverything;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Nathan on 2016-01-31.
 */
public class FireBaseData {
    public static Firebase fire;
    public static boolean isAuth;

    FireBaseData (){
        isAuth = false;
    }

    public static void authUser(){
        fire = new Firebase("https://vivid-inferno-6279.firebaseio.com/");
        fire = fire.child("copies").child(User.getUid());
        fire.authWithCustomToken(User.getToken(),
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        User.setInfo(authData);
                        isAuth = true;
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        // Something went wrong :(
                    }});
    }


}
