package to.msn.wings.realm;

import android.app.Application;
import io.realm.Realm;

public class realmapplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

    }
}
