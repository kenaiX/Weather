package cc.kenai.weather;

import android.app.Service;
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

public class WeatherServiceCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Service service;
    public WeatherPojo weatherPojo;

    public void create(Service service) {
        this.service = service;
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new FetchWeatherIfNeed());
        UpdateReceiver.cancelUpdateBroadcast(service);
        UpdateReceiver.sendUpdateBroadcast(service);
        XSetting.getSharedPreferences(service).registerOnSharedPreferenceChangeListener(this);
    }

    public void destroy(Service service) {
        EventBus.getDefault().unregister(this);
        XSetting.getSharedPreferences(service).unregisterOnSharedPreferenceChangeListener(this);
        service = null;
    }


    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventBackgroundThread(FetchWeatherIfNeed event) {
        FetchWeatherUtil.updateWeatherIfNeed(service, weatherPojo);
    }


    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventMainThread(UpdateStateIfNeed event) {
        if (event != null) {
            WeatherStatebarUtil.show_statebar(service, event.weatherPojo);
            weatherPojo = event.weatherPojo;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventMainThread(StateBarControl.Show event) {
        //todo 验证是否会造成死循环
        if (event != null) {
            WeatherStatebarUtil.show_statebar(service, weatherPojo);
        } else {
            EventBus.getDefault().post(new FetchWeatherIfNeed());
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventMainThread(StateBarControl.Dismiss event) {
        WeatherStatebarUtil.cancel_statebar(service);
    }

    @Override
    @DebugLog
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("weather_area")) {
            weatherPojo = null;
            EventBus.getDefault().post(new FetchWeatherIfNeed());
        }
    }
}
