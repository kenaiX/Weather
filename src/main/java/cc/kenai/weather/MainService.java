package cc.kenai.weather;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.kenai.function.message.XLog;
import com.kenai.function.setting.XSetting;

import cc.kenai.weather.utils.WeatherLock;

public class MainService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XSetting.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        if (weatherLock != null) {
            weatherLock.xDestroy();
            weatherLock = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.xLog("1111");
        XSetting.getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        if (XSetting.xget_boolean(this, "weather_mode")) {
            new MainXService(this).xCreate();
        }
        if (XSetting.xget_boolean(this, "weather_lock_mode")) {
            weatherLock = new WeatherLock(this);
            weatherLock.xCreate();
        } else {
            stopSelf();
        }
    }

    private WeatherLock weatherLock;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("weather_lock_mode")) {
            if (weatherLock != null) {
                weatherLock.xDestroy();
                weatherLock = null;
            }
            if (XSetting.xget_boolean(this, "weather_lock_mode")) {
                weatherLock = new WeatherLock(this);
                weatherLock.xCreate();
            } else {
                stopSelf();
            }
        }
    }
}
