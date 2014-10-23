package cc.kenai.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kenai.function.message.XLog;
import com.kenai.function.setting.XSetting;

import cc.kenai.weather.utils.WeatherStatebar;

/**
 * Created by kenai on 13-12-9.
 */
public class UpdateReceiver extends BroadcastReceiver {
    private final String Tag = "WeatherStatebar";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(WeatherStatebar.BROADCAST_STATEBAR_CANCEL)) {
                Intent it = new Intent(context, UpdateWeatherService.class);
                it.setAction(intent.getAction());
                context.startService(it);
            } else {
                if (XSetting.xget_boolean(context, "weather_mode")) {
                    Intent it = new Intent(context, UpdateWeatherService.class);
                    it.setAction(intent.getAction());
                    context.startService(it);

                }
            }
        }
//        String it = intent.getAction();
//        XLog.xLog(Tag, "receive broadcast : " + it);
//        if (it == null) {
//            return;
//        }
//        if (XSetting.xget_boolean(context, "weather_mode")) {
//            if (it.equals(WeatherStatebar.BROADCAST_STATEBAR_SHOWINFO)) {
//                sendUpdateBroadcast(context);
//                XLog.xLog(Tag, "show weather info");
//                DisplayMetrics metric = new DisplayMetrics();
//                WindowManager wm = (WindowManager) context
//                        .getSystemService("window");
//                wm.getDefaultDisplay().getMetrics(metric);
//                int width = metric.widthPixels; // 屏幕宽度（像素）
//                int height = metric.heightPixels; // 屏幕高度（像素）
//                FloatActivity.showView(new WeatherInfo(context, width * 8 / 10, height * 6 / 10));
//            } else if (it.equals(WeatherStatebar.BROADCAST_STATEBAR_SHOWSTATE)) {
//                XLog.xLog(Tag, "show weather statebar");
//                if (context.getSharedPreferences(SharedConfig.Filter, 0).getString(SharedConfig.Weather_cache, "").length() < 1) {
//                    new Thread(new Runnable() {
//
//                        public void run() {
//                            try {
//                                if (XLog.model)
//                                    XLog.xLog(Tag, "update step 1");
//                                WeatherUtilsBykenai
//                                        .getWeather(context);
//                                if (WeatherStatebar.getState(context) || Math.abs(WeatherUtilsBykenai.getTime_with_update(context)) > 5 * 60 * 60 * 1000) {
//                                    XLog.xLog(Tag, "update step 2");
//                                    WeatherStatebar.show_statebar(context);
//                                } else {
//                                    XLog.xLog(Tag, "update step 2_2");
//                                }
//                                WeatherStatebar.setInternetState(context, true);
//                            } catch (JSONException e) {
//                                WeatherStatebar.setInternetState(context, false);
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                } else {
//                    try {
//                        WeatherStatebar.notify_notification(context);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            } else if (it.equals(WeatherStatebar.BROADCAST_STATEBAR_CANCEL)) {
//                XLog.xLog(Tag, "cancel");
//                WeatherStatebar.cancel_notification(context);
//            } else if (it.equals(BROADCAST_UPDATE)) {
//                if (XLog.model) {
//                    SharedPreferences shared = context.getSharedPreferences("testlog", 0);
//                    String s = shared.getString("updaterecord", "none");
//                    s = s + new Date() + " ;;\n";
//                    XLog.xLog("testlog", s);
//                    shared.edit().putString("updaterecord", s).apply();
//                }
//                int time = XTime.gettime_partly(4);
//                if (time > 5 && time < 22) {
//                    XLog.xLog(Tag, "update check");
//                    new Thread(new Runnable() {
//
//                        public void run() {
//                            try {
//                                if (XLog.model)
//                                    XLog.xLog(Tag, "update step 1");
//                                WeatherUtilsBykenai
//                                        .getWeather(context);
//                                if (WeatherStatebar.getState(context) || Math.abs(WeatherUtilsBykenai.getTime_with_update(context)) > 5 * 60 * 60 * 1000) {
//                                    XLog.xLog(Tag, "update step 2");
//                                    WeatherStatebar.show_statebar(context);
//                                } else {
//                                    XLog.xLog(Tag, "update step 2_2");
//                                }
//                                WeatherStatebar.setInternetState(context, true);
//                            } catch (JSONException e) {
//                                WeatherStatebar.setInternetState(context, false);
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                } else {
//                    XLog.xLog(Tag, "no nes to update");
//                }
//            } else if (it.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//                if (!WeatherStatebar.getInternetState(context)) {
//                    XLog.xLog("网络发生改变，需重新获取天气情况");
//                    context.sendBroadcast(new Intent(BROADCAST_UPDATE));
//                } else {
//                    XLog.xLog("网络发生改变，无需重新获取天气情况");
//                }
//            }
//        } else {
//            XLog.xLog(Tag, "cancel");
//            WeatherStatebar.cancel_notification(context);
//        }
    }


    public final static String BROADCAST_UPDATE = "cc.kenai.weather.MainXService.update";
    private static final int FLAG_CANCEL_CURRENT = 268435456;

    public static void sendUpdateBroadcast(Context context) {
        XLog.xLog("send to start update weather");
        AlarmManager xAlarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(BROADCAST_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, FLAG_CANCEL_CURRENT);
//        try {
        xAlarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
//        } catch (Exception e) {
//            xAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                    System.currentTimeMillis() + 1000,
//                    AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
//        }
    }

    public static void cancelUpdateBroadcast(Context context) {
        XLog.xLog("send to cancel update weather");
        AlarmManager xAlarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(BROADCAST_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, FLAG_CANCEL_CURRENT);
        xAlarmManager.cancel(pendingIntent);
    }
}
