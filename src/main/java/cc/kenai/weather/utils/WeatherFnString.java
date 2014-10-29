package cc.kenai.weather.utils;

import cc.kenai.weather.pojos.WeatherPojo;

/**
 * Created by kenai on 14/10/29.
 */
public class WeatherFnString {
    final WeatherPojo.Fc.Fn fn;
    final boolean isDay;

    public WeatherFnString(WeatherPojo.Fc.Fn fn) {
        this.fn = fn;
        this.isDay = true;
    }

    public String getWeatherMay() {
        String weatherMay;
        if (isDay) {
            if (fn.w_d.equals(fn.w_n)) {
                weatherMay = fn.w_n;
            } else {
                weatherMay = fn.w_d + "转" + fn.w_n;
            }
        } else {
            weatherMay = fn.w_n;
        }
        return weatherMay;
    }

    public String getTemMay() {
        String temMay;
        if (isDay) {
            if (fn.h.equals(fn.l)) {
                temMay = fn.l;
            } else {
                temMay = fn.h + "~" + fn.l;
            }
        } else {
            temMay = "~" + fn.l;
        }
        return temMay;
    }

    public String getWindMay() {
        String windMay;
        if (isDay) {
            if (fn.fx_d.equals(fn.fx_n)) {
                windMay = fn.fx_n;
            } else {
                windMay = fn.fx_d + "转" + fn.fx_n;
            }
        } else {
            windMay = fn.fx_n;
        }
        return windMay;
    }

    public String getWindSpeedMay() {
        String windMay;
        if (isDay) {
            if (fn.fl_d.equals(fn.fl_n)) {
                windMay = fn.fl_n;
            } else {
                windMay = fn.fl_d + "转" + fn.fl_n;
            }
        } else {
            windMay = fn.fl_n;
        }
        return windMay;
    }

}
