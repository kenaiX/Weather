package cc.kenai.weather.utils;

import android.content.Context;
import android.util.Log;

import com.kenai.function.setting.XSetting;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cc.kenai.weather.event.UpdateStateIfNeed;
import cc.kenai.weather.pojos.WeatherPojo;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

public class FetchWeatherUtil {
    public final static void updateWeatherIfNeed(Context context, WeatherPojo weatherPojo) {
        String area_now = XSetting.xget_string(context, "weather_area");
        //还没有天气数据
        if (weatherPojo == null) {
            updateWeather(weatherPojo, area_now);
        } else {
            if (!area_now.equals(weatherPojo.citycode)) {
                updateWeather(weatherPojo, area_now);
            } else {
                long nowTime = System.currentTimeMillis();
                if (Math.abs(weatherPojo.updatetime - nowTime) > 1000 * 60 * 30) {
                    updateWeather(weatherPojo, area_now);
                }
            }
        }
    }

    @DebugLog
    private static void updateWeather(WeatherPojo weatherPojo, String citycode) {
        String url = "http://kweather.duapp.com/rest?city=" + citycode;
        if (weatherPojo != null) {
            url = url + "&n=" + weatherPojo.now.x + "&f=" + weatherPojo.fc.x + "&e=" + weatherPojo.en.x;
        }

        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(3, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                Log.e("FetchWeather", "fetch weather, but return error code : " + response.code());
            } else {

                String result = response.body().string();

                dealWithResult(weatherPojo, citycode, result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //todo  应该根据不同情况返回不同数据，指示用户真实情况
    static String dealWithResult(WeatherPojo weatherPojo, String citycode, String result) {
        StringBuilder returnString = new StringBuilder();

        WeatherPojo pojo = WeatherPojo.formatFromString(result);

        if (pojo.fc == null && pojo.en == null && pojo.now == null) {
            returnString.append("receive none");
        } else {
            if (weatherPojo == null) {
                returnString.append("new weatherPojo");

                weatherPojo = pojo;
            } else {
                returnString.append("receive some : ");

                if (pojo.now != null) {
                    weatherPojo.now = pojo.now;
                } else {
                    returnString.append("now ");
                }
                if (pojo.en != null) {
                    weatherPojo.en = pojo.en;
                } else {
                    returnString.append("en ");
                }
                if (pojo.fc != null) {
                    weatherPojo.fc = pojo.fc;
                } else {
                    returnString.append("fc ");
                }
            }
        }

        weatherPojo.citycode = citycode;
        weatherPojo.updatetime = System.currentTimeMillis();
        EventBus.getDefault().postSticky(new UpdateStateIfNeed(weatherPojo));

        return returnString.toString();
    }

}

