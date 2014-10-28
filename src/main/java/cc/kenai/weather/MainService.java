package cc.kenai.weather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.ServiceCompat;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        return ServiceCompat.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceCompat.destroy(this);
    }

}
