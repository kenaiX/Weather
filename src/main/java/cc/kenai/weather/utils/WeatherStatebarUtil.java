package cc.kenai.weather.utils;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.kenai.function.time.XTime;

import cc.kenai.meizu.MZNotification;
import cc.kenai.weather.MainBroadcastEvent;
import cc.kenai.weather.R;
import cc.kenai.weather.pojos.WeatherPojo;

public class WeatherStatebarUtil {

    public final static int ID = 1012110;

    public final static void show_statebar(Context context, WeatherPojo weatherPojo) {
        //数据创建
        MainWeather mainWeather = new MainWeather(context, weatherPojo);
        Fn tomorry = new Fn(weatherPojo.fc.f2);
        Fn second = new Fn(weatherPojo.fc.f3);

        //notification创建
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(
                MainBroadcastEvent.BROADCAST_STATEBAR_SHOWINFO), 0);

        Notification notification = new NotificationCompat.Builder(context).setSmallIcon(mainWeather.smallIco)
                .setLargeIcon(mainWeather.largeIco)
                .setContentTitle(mainWeather.contentTitle).setContentText(mainWeather.contentText).setTicker(mainWeather.ticker)
                .setOngoing(true).setContentIntent(pi)
                .addAction(tomorry.smallIco, tomorry.contentText, null)
                .addAction(second.smallIco, second.contentText, null)
                .build();

        //魅族通知栏处理
        MZNotification.internalApp(notification);

        //notification发送
        NotificationManagerCompat.from(context).notify("weatherstatebar", WeatherStatebarUtil.ID, notification);
    }

    public final static void cancel_statebar(Context context) {
        NotificationManagerCompat.from(context).cancel("weatherstatebar", WeatherStatebarUtil.ID);
    }


    static class MainWeather {
        public int smallIco;
        public Bitmap largeIco;
        public String contentTitle, contentText;
        public final String ticker = "新的天气预报";

        public MainWeather(Context context, WeatherPojo weatherPojo) {
            String weatherMay, windMay, temMay;
            if (isDay()) {
                if (weatherPojo.fc.f1.w_d.equals(weatherPojo.fc.f1.w_n)) {
                    weatherMay = weatherPojo.fc.f1.w_d;
                } else {
                    weatherMay = weatherPojo.fc.f1.w_d + "转" + weatherPojo.fc.f1.w_n;
                }
                if (weatherPojo.fc.f1.fx_d.equals(weatherPojo.fc.f1.fx_n)) {
                    windMay = weatherPojo.fc.f1.fx_d;
                } else {
                    windMay = weatherPojo.fc.f1.fx_d + "转" + weatherPojo.fc.f1.fx_n;
                }
                if (weatherPojo.fc.f1.h.equals(weatherPojo.fc.f1.l)) {
                    temMay = weatherPojo.fc.f1.h;
                } else {
                    temMay = weatherPojo.fc.f1.h + "~" + weatherPojo.fc.f1.l;
                }
            } else {
                weatherMay = weatherPojo.fc.f1.w_n;
                windMay = weatherPojo.fc.f1.fx_n;
                temMay = "~" + weatherPojo.fc.f1.l;
            }

            if (!windMay.contains("风")) {
                windMay = windMay + "风";
            }


            String nowWind;
            if (weatherPojo.now.fl.contains("风")) {
                nowWind = weatherPojo.now.fl;
            } else {
                nowWind = weatherPojo.now.fl + "风";
            }


            smallIco = getSmallIcon(weatherMay);

            contentTitle = weatherMay + " " + nowWind + " " + weatherPojo.now.wd + "℃";
            if (weatherPojo.en != null) {
                contentText = temMay + " " + windMay + " 空气:" + weatherPojo.en.ql + "(" + weatherPojo.en.aqi + ")";
            } else {
                contentText = temMay + " " + windMay;
            }

            largeIco = BitmapFactory.decodeResource(context.getResources(), getLargeIcon(weatherMay));
        }
    }

    static class Fn {
        public int smallIco;
        public String contentText;

        public Fn(WeatherPojo.Fc.Fn fn) {
            String weatherMay;
            if (fn.w_d.equals(fn.w_n)) {
                weatherMay = fn.w_d;
            } else {
                weatherMay = fn.w_d + "转" + fn.w_n;
            }
            smallIco = getSmallIcon(weatherMay);

            contentText = weatherMay + " " + fn.h + "~" + fn.l;

        }
    }

    public static boolean isDay() {
        return true;
    }

    static int getSmallIcon(String weatherMay) {
        int smallIco;
        if (weatherMay.contains("雹")) {
            smallIco = R.drawable.weather_statubar_hail;
        } else if (weatherMay.contains("雪")) {
            if (weatherMay.contains("大雪")) {
                smallIco = R.drawable.weather_statubar_snow_l;
            } else if (weatherMay.contains("雨")) {
                smallIco = R.drawable.weather_statubar_rain_snow;
            } else {
                smallIco = R.drawable.weather_statubar_snow_s;
            }
        } else if (weatherMay.contains("霜")) {
            smallIco = R.drawable.weather_statubar_snow_storm;
        } else if (weatherMay.contains("雨")) {
            if (weatherMay.contains("晴")) {
                smallIco = R.drawable.weather_statubar_sun_rain;
            } else if (weatherMay.contains("雷")) {
                smallIco = R.drawable.weather_statubar_rain_t;
            } else if (weatherMay.contains("大雨")) {
                smallIco = R.drawable.weather_statubar_rain_l;
            } else if (weatherMay.contains("中雨")) {
                smallIco = R.drawable.weather_statubar_rain_m;
            } else if (weatherMay.contains("小雨")) {
                smallIco = R.drawable.weather_statubar_rain_s;
            } else {
                smallIco = R.drawable.weather_statubar_rain_s;
            }
        } else if (weatherMay.contains("云") || weatherMay.contains("阴")) {

            if (weatherMay.contains("晴")) {
                if (XTime.gettime_partly(4) <= 17) {
                    smallIco = R.drawable.weather_statubar_sun_cloudy;
                } else {
                    smallIco = R.drawable.weather_statubar_sun_cloudy_night;
                }
            } else {
                smallIco = R.drawable.weather_statubar_cloudy;
            }
        } else if (weatherMay.contains("风")) {
            if (XTime.gettime_partly(4) <= 17) {
                smallIco = R.drawable.weather_statubar_fog;
            } else {
                smallIco = R.drawable.weather_statubar_fog_night;
            }
        } else if (weatherMay.contains("晴")) {
            if (XTime.gettime_partly(4) <= 17) {
                smallIco = R.drawable.weather_statubar_sun;
            } else {
                smallIco = R.drawable.weather_statubar_sun_night;
            }
        } else {
            smallIco = R.drawable.logo_flyme;
        }
        return smallIco;
    }

    static int getLargeIcon(String weatherMay) {
        int largeIco;
        if (weatherMay.contains("雹")) {
            largeIco = R.drawable.weather_lock_hail;
        } else if (weatherMay.contains("雪")) {
            if (weatherMay.contains("大雪")) {
                largeIco = R.drawable.weather_lock_snow_l;
            } else if (weatherMay.contains("雨")) {
                largeIco = R.drawable.weather_lock_rain_snow;
            } else {
                largeIco = R.drawable.weather_lock_snow_s;
            }
        } else if (weatherMay.contains("霜")) {
            largeIco = R.drawable.weather_lock_snow_storm;
        } else if (weatherMay.contains("雨")) {
            if (weatherMay.contains("晴")) {
                largeIco = R.drawable.weather_lock_sun_rain;
            } else if (weatherMay.contains("雷")) {
                largeIco = R.drawable.weather_lock_rain_t;
            } else if (weatherMay.contains("大雨")) {
                largeIco = R.drawable.weather_lock_rain_l;
            } else if (weatherMay.contains("中雨")) {
                largeIco = R.drawable.weather_lock_rain_m;
            } else if (weatherMay.contains("小雨")) {
                largeIco = R.drawable.weather_lock_rain_s;
            } else {
                largeIco = R.drawable.weather_lock_rain_s;
            }
        } else if (weatherMay.contains("云") || weatherMay.contains("阴")) {

            if (weatherMay.contains("晴")) {
                if (XTime.gettime_partly(4) <= 17) {
                    largeIco = R.drawable.weather_lock_sun_cloudy;
                } else {
                    largeIco = R.drawable.weather_lock_sun_cloudy_night;
                }
            } else {
                largeIco = R.drawable.weather_lock_cloudy;
            }
        } else if (weatherMay.contains("风")) {
            if (XTime.gettime_partly(4) <= 17) {
                largeIco = R.drawable.weather_lock_fog;
            } else {
                largeIco = R.drawable.weather_lock_fog_night;
            }
        } else if (weatherMay.contains("晴")) {
            if (XTime.gettime_partly(4) <= 17) {
                largeIco = R.drawable.weather_lock_sun;
            } else {
                largeIco = R.drawable.weather_lock_sun_night;
            }
        } else {
            largeIco = R.drawable.weather_lock_other;
        }
        return largeIco;
    }
}
