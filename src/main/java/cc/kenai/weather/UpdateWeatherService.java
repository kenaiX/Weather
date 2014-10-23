package cc.kenai.weather;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.kenai.function.message.XLog;

import org.json.JSONException;

import java.util.Date;

import cc.kenai.weather.config.SharedConfig;
import cc.kenai.weather.utils.WeatherStatebar;
import cc.kenai.weather.utils.WeatherUtilsBykenai;

/**
 * Created by kenai on 13-12-9.
 */
public class UpdateWeatherService extends IntentService {
    public static final String ACTION_WEATHER_HAS_UPDATED = "cc.kenai.weather.action.weather_has_updated";

    public UpdateWeatherService() {
        super("UpdateWeatherService");
    }

    private final String Tag = "WeatherStatebar";

    @Override
    protected void onHandleIntent(Intent intent) {
//        XLog.model=true;
        String it = intent.getAction();
        XLog.xLog(Tag, "receive broadcast : " + it);
        if (it == null) {
            return;
        }
        if (it.equals(WeatherStatebar.BROADCAST_STATEBAR_SHOWSTATE)) {
            XLog.xLog(Tag, "update weather statebar");
            if (UpdateWeatherService.this.getSharedPreferences(SharedConfig.Filter, 0).getString(SharedConfig.Weather_cache, "").length() < 1) {
                try {
                    XLog.xLog(Tag, "update weather statebar step 1/2");
                    WeatherUtilsBykenai.WeatherTotal.getWeather(UpdateWeatherService.this);
                    try {
                        WeatherUtilsBykenai.WeatherNOW.getWeather_NOW(UpdateWeatherService.this);
                        WeatherUtilsBykenai.WeatherAQI.getWeather_AQI(UpdateWeatherService.this);
                    } catch (JSONException e) {
                    }
                    if (WeatherStatebar.getState(UpdateWeatherService.this) || Math.abs(getTime_with_update(UpdateWeatherService.this)) > 5 * 60 * 60 * 1000) {
                        XLog.xLog(Tag, "update weather statebar step 2/2");
                        WeatherStatebar.show_statebar(UpdateWeatherService.this);
                    } else {
                        XLog.xLog(Tag, "update weather statebar step 2.2/2");
                    }
                    WeatherStatebar.setInternetState(UpdateWeatherService.this, true);
                } catch (JSONException e) {
                    WeatherStatebar.setInternetState(UpdateWeatherService.this, false);
                    XLog.xLog(Tag, "update weather statebar-error-internet");

                }
            } else {
                try {
                    WeatherStatebar.notify_notification(UpdateWeatherService.this);
                    saveTime_with_update(this);
                } catch (JSONException e) {
                    XLog.xLog(Tag, "update weather statebar--error");
                }
            }

        } else if (it.equals(WeatherStatebar.BROADCAST_STATEBAR_CANCEL)) {
            XLog.xLog(Tag, "cancel");
            WeatherStatebar.cancel_notification(UpdateWeatherService.this);
        } else if (it.equals(UpdateReceiver.BROADCAST_UPDATE)) {
            if (XLog.model) {
                SharedPreferences shared = UpdateWeatherService.this.getSharedPreferences("testlog", 0);
                String s = shared.getString("updaterecord", "none");
                s = s + new Date() + " ;;\n";
                XLog.xLog("testlog", s);
                shared.edit().putString("updaterecord", s).commit();
            }


            XLog.xLog(Tag, "update check");
            try {
                XLog.xLog(Tag, "update step 1/3");
                WeatherUtilsBykenai.WeatherTotal.getWeather(UpdateWeatherService.this);
                try {
                    XLog.xLog(Tag, "update step 2/3");
                    WeatherUtilsBykenai.WeatherNOW.getWeather_NOW(UpdateWeatherService.this);
                    WeatherUtilsBykenai.WeatherAQI.getWeather_AQI(UpdateWeatherService.this);
                } catch (JSONException e) {
                }
                if (WeatherStatebar.getState(UpdateWeatherService.this) || Math.abs(getTime_with_update(UpdateWeatherService.this)) > 5 * 60 * 60 * 1000) {
                    XLog.xLog(Tag, "update step 3/3");
                    WeatherStatebar.show_statebar(UpdateWeatherService.this);
                } else {
                    XLog.xLog(Tag, "update step 3.2/3");
                }
                WeatherStatebar.setInternetState(UpdateWeatherService.this, true);
                getBaseContext().sendBroadcast(new Intent(ACTION_WEATHER_HAS_UPDATED));
            } catch (JSONException e) {
                WeatherStatebar.setInternetState(UpdateWeatherService.this, false);
                XLog.xLog(Tag, "update step error");
            }
//            } else {
//                if (WeatherStatebar.getState(UpdateWeatherService.this)) {
//                    try {
//                        WeatherUtilsBykenai.WeatherNOW.getWeather_NOW(UpdateWeatherService.this);
//                        try {
//                            WeatherUtilsBykenai.WeatherAQI.getWeather_AQI(UpdateWeatherService.this);
//                        } catch (JSONException e) {
//                        }
//                        WeatherStatebar.show_statebar(UpdateWeatherService.this);
//                        WeatherStatebar.setInternetState(UpdateWeatherService.this, true);
//                    } catch (JSONException e) {
//                        WeatherStatebar.setInternetState(UpdateWeatherService.this, false);
//                        XLog.xLog(Tag, "internet error");
//                    }
//                    XLog.xLog(Tag, "only update now and aqi");
//                }else{
//                    XLog.xLog(Tag, "nothing to do");
//                }
//            }
        } else if (it.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (!WeatherStatebar.getInternetState(UpdateWeatherService.this)) {
                XLog.xLog("网络发生改变，需重新获取天气情况");
                UpdateWeatherService.this.sendBroadcast(new Intent(UpdateReceiver.BROADCAST_UPDATE));
            } else {
                XLog.xLog("网络发生改变，无需重新获取天气情况");
            }
        }

    }


    private final static long getTime_with_update(Context context) {
        long time = context.getSharedPreferences(SharedConfig.Filter, 0).getLong(SharedConfig.Time_update, 0);
        long time_now = System.currentTimeMillis();
        XLog.xLog("时间差" + (time_now - time));
        return time_now - time;
    }

    private final static void saveTime_with_update(Context context) {
        context.getSharedPreferences(SharedConfig.Filter, 0).edit().putLong(SharedConfig.Time_update, System.currentTimeMillis()).commit();
        XLog.xLog("saveTime_with_update");
    }
}
