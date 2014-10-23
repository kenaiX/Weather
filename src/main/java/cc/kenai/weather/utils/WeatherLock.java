package cc.kenai.weather.utils;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenai.function.setting.XSetting;
import com.kenai.function.time.XTime;

import cc.kenai.weather.R;
import cc.kenai.weather.UpdateWeatherService;
import cc.kenai.weather.ad.BannarAD;

/**
 * Created by kenai on 14-3-16.
 */
public class WeatherLock {
    View v;
    Context context;
    int screenWidth; // 屏幕宽度（像素）
    int screenHeight; // 屏幕高度（像素）
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (Intent.ACTION_USER_PRESENT.equals(intent
                        .getAction())) {
                    dismiss();
                } else if (intent.getAction().equals(
                        Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                    if (((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE))
                            .inKeyguardRestrictedInputMode()) {
                        show();
                    }
                } else if (UpdateWeatherService.ACTION_WEATHER_HAS_UPDATED.equals(intent
                        .getAction())) {
                    update();
                } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                    if (v != null && !v.isShown()) {
                        v.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    };

    public WeatherLock(Context context) {
        this.context = context;
    }

    public void xCreate() {
        IntentFilter it = new IntentFilter();
        it.addAction(Intent.ACTION_USER_PRESENT);
        it.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        it.addAction(UpdateWeatherService.ACTION_WEATHER_HAS_UPDATED);
        it.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(receiver, it);
    }

    public void xDestroy() {
        context.unregisterReceiver(receiver);
        dismiss();
    }


    protected void show() {
        if (v == null) {
            switch (XSetting.xget_int(context, "weather_lock_pifu")) {
                case 0:
                default:
                    v = View.inflate(context, R.layout.weather_lock, null);
                    break;
                case 1:
                    v = View.inflate(context, R.layout.weather_lock_blue, null);
                    break;
            }

            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    XLog.xLog("yyyyy:"+event.getY());
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        v.setVisibility(View.INVISIBLE);
                    }
//                    if (event.getY() < screenHeight * 2 / 10 || event.getRawY() > screenHeight * 8 / 10) {
//                        XLog.xLog("yyyyy:"+event.getY());
//                    v.setVisibility(View.INVISIBLE);
//                    }
                    return true;
                }
            });
            update();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.addView(v, getLayoutParams(context));
        }
    }

    public void dismiss() {
        if (v != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(v);
            v = null;
        }
    }

    public WindowManager.LayoutParams getLayoutParams(Context context) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES;
        wmParams.gravity = Gravity.CENTER_HORIZONTAL
                | Gravity.TOP;

        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels; // 屏幕宽度（像素）
        screenHeight = metric.heightPixels; // 屏幕高度（像素）
        wmParams.width = screenWidth * 8 / 10;
        wmParams.height = Math.max(460,screenHeight * 4 / 10);
        wmParams.y = screenHeight * 4 / 10;
        return wmParams;
    }

    void update() {
        if (v == null) {
            return;
        }
        TextView cityname = (TextView) v.findViewById(R.id.weather_lock_text_cityname);
        TextView weather0 = (TextView) v.findViewById(R.id.weather_lock_text_weather0);
        ImageView img0 = (ImageView) v.findViewById(R.id.weather_lock_img0);
        TextView temp_now = (TextView) v.findViewById(R.id.weather_lock_text_temp_now);
        TextView temp_info = (TextView) v.findViewById(R.id.weather_lock_text_temp_info);
        TextView wind = (TextView) v.findViewById(R.id.weather_lock_text_wind);
        TextView aqi = (TextView) v.findViewById(R.id.weather_lock_text_aqi);
        TextView aqi_text = (TextView) v.findViewById(R.id.weather_lock_text_aqi_text);

        TextView weather1 = (TextView) v.findViewById(R.id.weather_lock_text_weather1);
        TextView weather2 = (TextView) v.findViewById(R.id.weather_lock_text_weather2);
        TextView weather3 = (TextView) v.findViewById(R.id.weather_lock_text_weather3);

        ImageView img1 = (ImageView) v.findViewById(R.id.weather_lock_img1);
        ImageView img2 = (ImageView) v.findViewById(R.id.weather_lock_img2);
        ImageView img3 = (ImageView) v.findViewById(R.id.weather_lock_img3);

        TextView temp1 = (TextView) v.findViewById(R.id.weather_lock_text_temp1);
        TextView temp2 = (TextView) v.findViewById(R.id.weather_lock_text_temp2);
        TextView temp3 = (TextView) v.findViewById(R.id.weather_lock_text_temp3);

        try {
            MyWeather m = WeatherUtilsBykenai.WeatherTotal.getWeather_huancun(context);
            cityname.setText(m.getCityInfo_cityname());
            weather0.setText(m.getWeather_string6()[0].split("，")[0]);
            img0.setImageResource(creatImg(true, m.getWeather_string6()[0].split("，")[0]));
            temp_info.setText(m.getWeather_temp()[0]);

            weather1.setText(m.getWeather_string6()[1].split("，")[0]);
            img1.setImageResource(creatImg(false, m.getWeather_string6()[1].split("，")[0]));
            temp1.setText(m.getWeather_temp()[1]);
            weather2.setText(m.getWeather_string6()[2].split("，")[0]);
            img2.setImageResource(creatImg(false, m.getWeather_string6()[2].split("，")[0]));
            temp2.setText(m.getWeather_temp()[2]);
            weather3.setText(m.getWeather_string6()[3].split("，")[0]);
            img3.setImageResource(creatImg(false, m.getWeather_string6()[3].split("，")[0]));
            temp3.setText(m.getWeather_temp()[3]);
        } catch (Exception e) {

        }

        try {
            MyNowWeather weather_now = WeatherUtilsBykenai.WeatherNOW.getWeather_huancun_NOW(context);
            temp_now.setText(weather_now.getTemp() + "℃");
            wind.setText(weather_now.getWind());
        } catch (Exception e) {

        }
        try {
            MyAQIWeather weather_aqi = WeatherUtilsBykenai.WeatherAQI.getWeather_huancun_AQI(context);
            aqi.setText("AQI:" + weather_aqi.getAQI());
            aqi_text.setText(createAqi(Integer.parseInt(weather_aqi.getAQI())));
        } catch (Exception e) {
            aqi.setText("AQI:0");
            aqi_text.setText("未知");
        }


        BannarAD.bindAD_lock(context, this, v);
    }

    int creatImg(boolean isToday, String mainWeather) {
        int smallIco;
        if (mainWeather.contains("雹")) {
            smallIco = R.drawable.weather_lock_hail;
        } else if (mainWeather.contains("雪")) {
            if (mainWeather.contains("大雪")) {
                smallIco = R.drawable.weather_lock_snow_l;
            } else if (mainWeather.contains("雨")) {
                smallIco = R.drawable.weather_lock_rain_snow;
            } else {
                smallIco = R.drawable.weather_lock_snow_s;
            }
        } else if (mainWeather.contains("霜")) {
            smallIco = R.drawable.weather_lock_snow_storm;
        } else if (mainWeather.contains("雨")) {
            if (mainWeather.contains("晴")) {
                smallIco = R.drawable.weather_lock_sun_rain;
            } else if (mainWeather.contains("雷")) {
                smallIco = R.drawable.weather_lock_rain_t;
            } else if (mainWeather.contains("大雨")) {
                smallIco = R.drawable.weather_lock_rain_l;
            } else if (mainWeather.contains("中雨")) {
                smallIco = R.drawable.weather_lock_rain_m;
            } else if (mainWeather.contains("小雨")) {
                smallIco = R.drawable.weather_lock_rain_s;
            } else {
                smallIco = R.drawable.weather_lock_rain_s;
            }
        } else if (mainWeather.contains("云") || mainWeather.contains("阴")) {

            if (mainWeather.contains("晴")) {
                if (isToday && XTime.gettime_partly(4) > 17) {
                    smallIco = R.drawable.weather_lock_sun_cloudy_night;
                } else {
                    smallIco = R.drawable.weather_lock_sun_cloudy;
                }
            } else {
                smallIco = R.drawable.weather_lock_cloudy;
            }
        } else if (mainWeather.contains("风")) {
            if (isToday && XTime.gettime_partly(4) > 17) {
                smallIco = R.drawable.weather_lock_fog_night;
            } else {
                smallIco = R.drawable.weather_lock_fog;
            }
        } else if (mainWeather.contains("晴")) {
            if (isToday && XTime.gettime_partly(4) > 17) {
                smallIco = R.drawable.weather_lock_sun_night;
            } else {
                smallIco = R.drawable.weather_lock_sun;
            }
        } else {
            smallIco = R.drawable.weather_lock_other;
        }
        return smallIco;
    }

    String createAqi(int aqi) {
        String contentText = "";
        if (aqi != 0) {
            if (aqi < 50) {
                contentText += "空气优";
            } else if (aqi < 100) {
                contentText += "空气良";
            } else if (aqi < 150) {
                contentText += "轻度污染";
            } else if (aqi < 200) {
                contentText += "中度污染";
            } else if (aqi < 300) {
                contentText += "重度污染";
            } else {
                contentText += "严重污染";
            }
        }
        return contentText;
    }
}
