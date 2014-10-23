package cc.kenai.weather.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class MyAQIWeather {
    @Override
    public String toString() {
        return this.weather.toString();
    }

    public MyAQIWeather(JSONObject weather) {
        this.weather = weather;
    }

    public JSONObject weather;

    public final String getPosition() throws JSONException {
        return weather.getString("position");
    }

    public final String getAQI() throws JSONException {
        return weather.getString("aqi");
    }


}
