package lwinmoehein.io.myarnetmaung.Singleton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CurrentUser {
    public static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
}
