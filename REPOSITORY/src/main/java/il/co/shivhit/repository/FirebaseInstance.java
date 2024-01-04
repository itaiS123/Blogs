package il.co.shivhit.repository;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseInstance {
    private static volatile FirebaseInstance _instance = null;
    public static FirebaseApp app;

    private FirebaseInstance(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("blogs-d240b")
                .setApplicationId("1:691632121757:android:33d71c8b821e14e28c7174")
                .setApiKey("AIzaSyBPKcwEHtl4V3_jbE-aP-_96yAlNuolgKI")
                .setStorageBucket("blogs-d240b.appspot.com")
                .build();

        app = FirebaseApp.initializeApp(context, options);
    }
    public static FirebaseInstance instance(Context context) {
        if (_instance == null) {  // 1st check
            synchronized (FirebaseInstance.class) {
                if (_instance == null){ // 2nd check
                    _instance = new FirebaseInstance(context);
                }
            }
        }
        return _instance;
    }
}

