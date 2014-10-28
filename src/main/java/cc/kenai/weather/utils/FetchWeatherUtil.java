package cc.kenai.weather.utils;

import android.content.Context;

import com.kenai.function.setting.XSetting;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cc.kenai.weather.event.UpdateStateIfNeed;
import cc.kenai.weather.pojos.WeatherPojo;
import de.greenrobot.event.EventBus;

public class FetchWeatherUtil {
    public final static void updateWeatherIfNeed(Context context, WeatherPojo weatherPojo) {
        String area_now = XSetting.xget_string(context, "weather_area");
        //还没有天气数据
        if (weatherPojo == null) {
            updateWeather(area_now);
        } else {
            if (!area_now.equals(weatherPojo.citycode)) {
                updateWeather(area_now);
            } else {
                long nowTime = System.currentTimeMillis();
                if (Math.abs(weatherPojo.updatetime - nowTime) > 1000 * 60 * 30) {
                    updateWeather(area_now);
                }
            }
        }
    }

    private static void updateWeather(String citycode) {
        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(3, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url("http://kweather.duapp.com/rest?city=" + citycode)
                .build();

        try {
            Response response = client.newCall(request).execute();

            //todo  应该根据不同情况返回不同数据，指示用户真实情况

            WeatherPojo weatherPojo = WeatherPojo.formatFromString(response.body().string());

            if (weatherPojo.now != null) {
                weatherPojo.citycode = citycode;
                weatherPojo.updatetime = System.currentTimeMillis();
                EventBus.getDefault().postSticky(new UpdateStateIfNeed(weatherPojo));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

