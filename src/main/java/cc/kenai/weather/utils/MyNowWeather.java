package cc.kenai.weather.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class MyNowWeather {
    @Override
    public String toString() {
        return this.weather.toString();
    }

    public MyNowWeather(JSONObject weather) {
        this.weather = weather;
    }

    public JSONObject weather;

    public final String getCityname() throws JSONException {
        return weather.getString("city");
    }

    public final String getTemp() throws JSONException {
        return weather.getString("temp");
    }

    public final String getWind() throws JSONException {
        return weather.getString("WD") + weather.getString("WS");
    }

    public final String getSD() throws JSONException {
        return weather.getString("SD");
    }

}
