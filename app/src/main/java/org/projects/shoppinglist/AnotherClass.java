package org.projects.shoppinglist;

import android.app.Application;
import com.firebase.client.Firebase;


/**
 * Created by Gabor on 2016.05.12..
 */
public class AnotherClass extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

    }
}
