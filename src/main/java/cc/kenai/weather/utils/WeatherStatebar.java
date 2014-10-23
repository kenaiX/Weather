package cc.kenai.weather.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.kenai.function.meizu.MeizuNotification;
import com.kenai.function.message.XLog;
import com.kenai.function.time.XTime;

import org.json.JSONException;

import cc.kenai.weather.R;

public class WeatherStatebar {

    public WeatherStatebar(Context context) {
        this.context = context;
    }

    private Context context;
    public final static String BROADCAST_STATEBAR_SHOWINFO = "cc.kenai.weather.utils.WeatherStatebar.statebar_showinfo",
            BROADCAST_STATEBAR_SHOWSTATE = "cc.kenai.weather.utils.WeatherStatebar.statebar_show",
            BROADCAST_STATEBAR_CANCEL = "cc.kenai.weather.utils.WeatherStatebar.statebar_cancel";
    public final static int ID = 1012110;


    public final static void init(Context context) {
        setState(context, false);
        setInternetState(context, true);
    }

    public static boolean getState(Context context) {
        return context.getSharedPreferences("WeatherStatebar", 0).getBoolean("statebar_state", false);
    }

    public static void setState(Context context, boolean b) {
        context.getSharedPreferences("WeatherStatebar", 0).edit().putBoolean("statebar_state", b).commit();
    }

    public static boolean getInternetState(Context context) {
        return context.getSharedPreferences("WeatherStatebar", 0).getBoolean("internet_state", false);
    }

    public static void setInternetState(Context context, boolean b) {
        context.getSharedPreferences("WeatherStatebar", 0).edit().putBoolean("internet_state", b).commit();
    }


    private final static Notification createNotification(Context context) throws JSONException {
        XLog.xLog("weather_notification");
        MyWeather weather;
        weather = WeatherUtilsBykenai.WeatherTotal.getWeather_huancun(context);
        String mainWeather = weather.getWeather_string6()[0].split("，")[0].trim();
        MyNowWeather weather_now = null;
        try {
            weather_now = WeatherUtilsBykenai.WeatherNOW.getWeather_huancun_NOW(context);
        } catch (Exception e) {

        }
        MyAQIWeather weather_aqi = null;
        try {
            weather_aqi = WeatherUtilsBykenai.WeatherAQI.getWeather_huancun_AQI(context);
        } catch (Exception e) {

        }
        String contentTitle;
        String contentText;
        if (weather_now == null) {
            contentTitle = mainWeather;
            contentText = "今日:" + weather.getWeather_temp()[0] + " "
                    + weather.getWeather_wind()[0];
        } else {

            int aqi = 0;
            if (weather_aqi != null) {
                aqi = Integer.valueOf(weather_aqi.getAQI());
            }


            contentText = "风向：" + weather_now.getWind() + " 湿度：" + weather_now.getSD();
            if (aqi != 0) {
                contentTitle = weather_now.getTemp() + "℃" + " " + "AQI:" + weather_aqi.getAQI() + " " + mainWeather;
                if (aqi < 50) {
                    contentText += " 空气优";
                } else if (aqi < 100) {
                    contentText += " 空气良";
                } else if (aqi < 150) {
                    contentText += " 轻度污染";
                } else if (aqi < 200) {
                    contentText += " 中度污染";
                } else if (aqi < 300) {
                    contentText += " 重度污染";
                } else {
                    contentText += " 严重污染";
                }
            } else {
                contentTitle = weather_now.getTemp() + "℃" + " " + mainWeather;
            }
        }
        Notification notification;

        int largeIco, smallIco;
        if (mainWeather.contains("雹")) {
            smallIco = R.drawable.weather_statubar_hail;
        } else if (mainWeather.contains("雪")) {
            if (mainWeather.contains("大雪")) {
                smallIco = R.drawable.weather_statubar_snow_l;
            } else if (mainWeather.contains("雨")) {
                smallIco = R.drawable.weather_statubar_rain_snow;
            } else {
                smallIco = R.drawable.weather_statubar_snow_s;
            }
        } else if (mainWeather.contains("霜")) {
            smallIco = R.drawable.weather_statubar_snow_storm;
        } else if (mainWeather.contains("雨")) {
            if (mainWeather.contains("晴")) {
                smallIco = R.drawable.weather_statubar_sun_rain;
            } else if (mainWeather.contains("雷")) {
                smallIco = R.drawable.weather_statubar_rain_t;
            } else if (mainWeather.contains("大雨")) {
                smallIco = R.drawable.weather_statubar_rain_l;
            } else if (mainWeather.contains("中雨")) {
                smallIco = R.drawable.weather_statubar_rain_m;
            } else if (mainWeather.contains("小雨")) {
                smallIco = R.drawable.weather_statubar_rain_s;
            } else {
                smallIco = R.drawable.weather_statubar_rain_s;
            }
        } else if (mainWeather.contains("云") || mainWeather.contains("阴")) {

            if (mainWeather.contains("晴")) {
                if (XTime.gettime_partly(4) <= 17) {
                    smallIco = R.drawable.weather_statubar_sun_cloudy;
                } else {
                    smallIco = R.drawable.weather_statubar_sun_cloudy_night;
                }
            } else {
                smallIco = R.drawable.weather_statubar_cloudy;
            }
        } else if (mainWeather.contains("风")) {
            if (XTime.gettime_partly(4) <= 17) {
                smallIco = R.drawable.weather_statubar_fog;
            } else {
                smallIco = R.drawable.weather_statubar_fog_night;
            }
        } else if (mainWeather.contains("晴")) {
            if (XTime.gettime_partly(4) <= 17) {
                smallIco = R.drawable.weather_statubar_sun;
            } else {
                smallIco = R.drawable.weather_statubar_sun_night;
            }
        } else {
            smallIco = R.drawable.logo_flyme;
        }
        largeIco = R.drawable.weather_large_statubar;
        notification = new Notification(largeIco, "", 0);
        notification.icon = smallIco;
        notification.flags = Notification.FLAG_NO_CLEAR;

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(
                BROADCAST_STATEBAR_SHOWINFO), 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, pi);
        MeizuNotification.internalApp(notification);


        return notification;
    }

    public final static void notify_notification(Context context) throws JSONException {
        NotificationManager nm;
        nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(cc.kenai.weather.utils.WeatherStatebar.ID, createNotification(context));
        setState(context, true);
    }

    public final static void cancel_notification(Context context) {
        NotificationManager nm;
        nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(cc.kenai.weather.utils.WeatherStatebar.ID);
        setState(context, false);
    }

    public final static void show_statebar(Context context) {
        context.sendBroadcast(new Intent(
                cc.kenai.weather.utils.WeatherStatebar.BROADCAST_STATEBAR_SHOWSTATE));
    }

    public final static void cancel_statebar(Context context) {
        context.sendBroadcast(new Intent(
                cc.kenai.weather.utils.WeatherStatebar.BROADCAST_STATEBAR_CANCEL));
    }


}
