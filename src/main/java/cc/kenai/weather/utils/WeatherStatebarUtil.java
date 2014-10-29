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

import cc.kenai.meizu.MZFlymeVersion;
import cc.kenai.meizu.MZNotification;
import cc.kenai.weather.MainBroadcastEvent;
import cc.kenai.weather.R;
import cc.kenai.weather.pojos.WeatherPojo;
import hugo.weaving.DebugLog;

public class WeatherStatebarUtil {

    public final static int ID = 1012110;

    @DebugLog
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
        NotificationManagerCompat.from(context).notify(WeatherStatebarUtil.ID, notification);
    }

    @DebugLog
    public final static void cancel_statebar(Context context) {
        NotificationManagerCompat.from(context).cancel(WeatherStatebarUtil.ID);
    }


    static class MainWeather {
        public int smallIco;
        public Bitmap largeIco;
        public String contentTitle, contentText;
        public final String ticker = "新的天气预报";

        public MainWeather(Context context, WeatherPojo weatherPojo) {
            WeatherFnString weatherString = new WeatherFnString(weatherPojo.fc.f1);

            String weatherMay = weatherString.getWeatherMay();
            String windSpeedMay = weatherString.getWindSpeedMay();
            String temMay = weatherString.getTemMay();


            String nowWind;
            if (weatherPojo.now.fl.contains("风")) {
                nowWind = weatherPojo.now.fl;
            } else {
                nowWind = weatherPojo.now.fl + "风";
            }


            smallIco = getSmallIcon(weatherMay);

            contentTitle = weatherMay + " " + nowWind + " " + weatherPojo.now.wd + "℃   " + weatherPojo.now.ct;
            if (weatherPojo.en.x != null) {
                contentText = temMay + " " + windSpeedMay + " 空气:" + weatherPojo.en.ql + "(" + weatherPojo.en.aqi + ")";
            } else {
                contentText = temMay + " " + windSpeedMay + " 湿度:" + weatherPojo.now.sd;
            }

            largeIco = BitmapFactory.decodeResource(context.getResources(), getLargeIcon(weatherMay));
        }
    }

    static class Fn {
        public int smallIco;
        public String contentText;

        public Fn(WeatherPojo.Fc.Fn fn) {
            WeatherFnString weatherString = new WeatherFnString(fn);

            String weatherMay = weatherString.getWeatherMay();

            smallIco = getSmallIcon(weatherMay);

            contentText = weatherMay + " " + fn.h + "~" + fn.l;

        }
    }


    static int getSmallIcon(String weatherMay) {
        int smallIco;

        if (MZFlymeVersion.isFlyme4()) {
            if (weatherMay.contains("雹")) {
                smallIco = R.drawable.weather_statubar_hail_flyme4;
            } else if (weatherMay.contains("雪")) {
                if (weatherMay.contains("大雪")) {
                    smallIco = R.drawable.weather_statubar_snow_l_flyme4;
                } else if (weatherMay.contains("雨")) {
                    smallIco = R.drawable.weather_statubar_rain_snow_flyme4;
                } else {
                    smallIco = R.drawable.weather_statubar_snow_s_flyme4;
                }
            } else if (weatherMay.contains("霜")) {
                smallIco = R.drawable.weather_statubar_snow_storm_flyme4;
            } else if (weatherMay.contains("雨")) {
                if (weatherMay.contains("晴")) {
                    smallIco = R.drawable.weather_statubar_sun_rain_flyme4;
                } else if (weatherMay.contains("雷")) {
                    smallIco = R.drawable.weather_statubar_rain_t_flyme4;
                } else if (weatherMay.contains("大雨")) {
                    smallIco = R.drawable.weather_statubar_rain_l_flyme4;
                } else if (weatherMay.contains("中雨")) {
                    smallIco = R.drawable.weather_statubar_rain_m_flyme4;
                } else if (weatherMay.contains("小雨")) {
                    smallIco = R.drawable.weather_statubar_rain_s_flyme4;
                } else {
                    smallIco = R.drawable.weather_statubar_rain_s_flyme4;
                }
            } else if (weatherMay.contains("云") || weatherMay.contains("阴")) {

                if (weatherMay.contains("晴")) {
                    if (XTime.gettime_partly(4) <= 17) {
                        smallIco = R.drawable.weather_statubar_sun_cloudy_flyme4;
                    } else {
                        smallIco = R.drawable.weather_statubar_sun_cloudy_night_flyme4;
                    }
                } else {
                    smallIco = R.drawable.weather_statubar_cloudy_flyme4;
                }
            } else if (weatherMay.contains("风")) {
                if (XTime.gettime_partly(4) <= 17) {
                    smallIco = R.drawable.weather_statubar_fog_flyme4;
                } else {
                    smallIco = R.drawable.weather_statubar_fog_night_flyme4;
                }
            } else if (weatherMay.contains("晴")) {
                if (XTime.gettime_partly(4) <= 17) {
                    smallIco = R.drawable.weather_statubar_sun_flyme4;
                } else {
                    smallIco = R.drawable.weather_statubar_sun_night_flyme4;
                }
            } else {
                smallIco = R.drawable.weather_statubar_other_flyme4;
            }
        } else {
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
                smallIco = R.drawable.weather_statubar_other;
            }
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
