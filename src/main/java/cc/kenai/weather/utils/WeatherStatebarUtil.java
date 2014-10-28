package cc.kenai.weather.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.kenai.function.time.XTime;

import cc.kenai.meizu.MZNotification;
import cc.kenai.weather.R;
import cc.kenai.weather.pojos.WeatherPojo;

public class WeatherStatebarUtil {

    public final static String BROADCAST_STATEBAR_SHOWINFO = "cc.kenai.weather.utils.WeatherStatebar.statebar_showinfo";
    public final static int ID = 1012110;

    public final static void show_statebar(Context context, WeatherPojo weatherPojo) {
        String mainWeather = weatherPojo.fc.f1.w_d + weatherPojo.fc.f1.w_n;


        String contentTitle;
        String contentText;

        contentTitle = weatherPojo.now.wd;

        contentText = weatherPojo.fc.f1.h;


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
        MZNotification.notificationIcon(notification, largeIco);

        notification.icon = smallIco;
        notification.flags = Notification.FLAG_NO_CLEAR;

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(
                BROADCAST_STATEBAR_SHOWINFO), 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, pi);
        MZNotification.internalApp(notification);

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify("weatherstatebar", WeatherStatebarUtil.ID, notification);
    }

    public final static void cancel_statebar(Context context) {
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel("weatherstatebar", WeatherStatebarUtil.ID);
    }


}
