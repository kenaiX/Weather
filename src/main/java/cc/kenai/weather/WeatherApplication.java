package cc.kenai.weather;

import android.app.Application;
import android.content.SharedPreferences;

import com.kenai.function.setting.XSetting;

import cc.kenai.weather.event.FetchWeatherIfNeed;
import cc.kenai.weather.event.StateBarControl;
import cc.kenai.weather.event.UpdateStateIfNeed;
import cc.kenai.weather.pojos.WeatherPojo;
import cc.kenai.weather.utils.FetchWeatherUtil;
import cc.kenai.weather.utils.WeatherStatebarUtil;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

public class WeatherApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    public WeatherPojo weatherPojo;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new FetchWeatherIfNeed());
        UpdateReceiver.cancelUpdateBroadcast(getBaseContext());
        UpdateReceiver.sendUpdateBroadcast(getBaseContext());
        XSetting.getSharedPreferences(getBaseContext()).registerOnSharedPreferenceChangeListener(this);
    }


    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventBackgroundThread(FetchWeatherIfNeed event) {
        FetchWeatherUtil.updateWeatherIfNeed(getBaseContext(), weatherPojo);
    }


    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventMainThread(UpdateStateIfNeed event) {
        if (event != null) {
            WeatherStatebarUtil.show_statebar(getBaseContext(), event.weatherPojo);
            weatherPojo = event.weatherPojo;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventMainThread(StateBarControl.Show event) {
        //todo 验证是否会造成死循环
        if (event != null) {
            WeatherStatebarUtil.show_statebar(getBaseContext(), weatherPojo);
        } else {
            EventBus.getDefault().post(new FetchWeatherIfNeed());
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventMainThread(StateBarControl.Dismiss event) {
        WeatherStatebarUtil.cancel_statebar(getBaseContext());
    }

    @Override
    @DebugLog
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("weather_area")) {
            EventBus.getDefault().post(new FetchWeatherIfNeed());
        }
    }
}
