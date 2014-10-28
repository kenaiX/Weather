package cc.kenai.weather;

import android.app.Application;

import cc.kenai.weather.event.UpdateWeatherIfNeed;
import cc.kenai.weather.pojos.WeatherPojo;
import cc.kenai.weather.utils.FetchWeatherUtil;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

public class WeatherApplication extends Application {
    public WeatherPojo weatherPojo;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }


    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventBackgroundThread(UpdateWeatherIfNeed event) {
        FetchWeatherUtil.updateWeatherIfNeed(getBaseContext(), weatherPojo);
    }


}
