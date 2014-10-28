package cc.kenai.weather;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import cc.kenai.weather.event.UpdateStateStatusIfNeed;
import cc.kenai.weather.event.UpdateWeatherIfNeed;
import cc.kenai.weather.utils.WeatherStatebarUtil;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

public class MainXService extends Service {


    @Override
    @DebugLog
    public void onCreate() {
        EventBus.getDefault().register(this);

        EventBus.getDefault().post(new UpdateWeatherIfNeed());

        UpdateReceiver.cancelUpdateBroadcast(getBaseContext());
        UpdateReceiver.sendUpdateBroadcast(getBaseContext());


    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(UpdateStateStatusIfNeed event) {

        if (event != null) {
            WeatherStatebarUtil.show_statebar(getBaseContext(), event.weatherPojo);
        }

    }
}
