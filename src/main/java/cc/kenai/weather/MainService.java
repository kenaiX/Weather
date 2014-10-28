package cc.kenai.weather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service {
    WeatherServiceCompat serviceCompat = new WeatherServiceCompat();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceCompat.create(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceCompat.destroy(this);
    }
}
