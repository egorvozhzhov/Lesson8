package ru.mirea.vozhzhovea.yandexmaps;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App  extends Application {
    private final String MAPKIT_API_KEY = "662eb9ba-af60-440f-bc5b-dbaff0fdbdb4";
    @Override
    public void onCreate() {
        super.onCreate();
        // Set the api key before calling initialize on MapKitFactory.
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}
