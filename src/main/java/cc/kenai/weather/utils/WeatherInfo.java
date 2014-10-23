package cc.kenai.weather.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kenai.function.message.XToast;

import org.json.JSONException;

import cc.kenai.weather.R;
import cc.kenai.weather.UpdateReceiver;
import cc.kenai.weather.ad.BannarAD;

public class WeatherInfo {
    Context context;
    View v;

    public WeatherInfo(Context context) {
        this.context = context;
    }

    public void show() {
        if (v == null) {
            v = View.inflate(context, R.layout.weather_main, null);
            Button bt1 = (Button) v.findViewById(R.id.bt1_weather);
            bt1.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    dismiss();
                    WeatherStatebar.cancel_statebar(context);
                }
            });

            Button bt2 = (Button) v.findViewById(R.id.bt2_weather);
            bt2.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    dismiss();
                }
            });


            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.addView(v, getLayoutParams(context));


            try {
                MyWeather m = WeatherUtilsBykenai.WeatherTotal.getWeather_huancun(context);

                TextView tx = (TextView) v.findViewById(R.id.tx_weather);
                tx.setText(createText(m));
                BannarAD.bindAD(context,this, v);

            } catch (Exception e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {

                public void run() {
                    try {
                        WeatherUtilsBykenai
                                .WeatherTotal.getWeather(context);
                        handler.post(run_success);
                    } catch (JSONException e) {
                    }
                }
            }).start();
        }
    }

    public void dismiss() {
        if (v != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(v);
            v = null;
        }
    }


    public static WindowManager.LayoutParams getLayoutParams(Context context) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES;
//        wmParams.gravity = Gravity.CENTER_HORIZONTAL
//                | Gravity.CENTER_VERTICAL;
        wmParams.gravity = Gravity.CENTER;

        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        wmParams.width = width;
        wmParams.height = height;
        return wmParams;
    }


    private final Handler handler = new Handler();
    private final Runnable run_fail = new Runnable() {

        @Override
        public void run() {
            XToast.xToast(context, "网络故障，获取最新天气失败");
        }
    };
    private final Runnable run_success = new Runnable() {
        @Override
        public void run() {
            try {
                MyWeather m = WeatherUtilsBykenai.WeatherTotal.getWeather_huancun(context);

                TextView tx = (TextView) v.findViewById(R.id.tx_weather);
                tx.setText(createText(m));

                XToast.xToast(context, "已更新至最新天气！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    static Spanned createText(MyWeather m) {

        StringBuilder weatherinfo = new StringBuilder();
        try {
            weatherinfo.append("<big>" + m.getCityInfo_cityname() + "</big>" + " " + m.getCityInfo_date() + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            weatherinfo.append(m.getWeather_temp()[0] + " " + m.getWeather_string6()[0] + " " + m.getWeather_wind()[0] + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (m.getInfo().length() > 0)
                weatherinfo.append("<p><font color=\"#32B4E6\">概况：</font>" + m.getInfo() + "</p>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = m.getLifeTitle();
            if (!s.contains("明")) {
                creatLife(m, weatherinfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            weatherinfo.append("<br>" + "明天：" + m.getWeather_temp()[1] + " " + m.getWeather_string6()[1] + " " + m.getWeather_wind()[1] + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String s = m.getLifeTitle();
            if (s.contains("明")) {
                creatLife(m, weatherinfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            weatherinfo.append("<br>" + "后天：" + m.getWeather_temp()[2] + " " + m.getWeather_string6()[2] + " " + m.getWeather_wind()[2] + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            weatherinfo.append("三天：" + m.getWeather_temp()[3] + " " + m.getWeather_string6()[3] + " " + m.getWeather_wind()[3] + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            weatherinfo.append("四天：" + m.getWeather_temp()[4] + " " + m.getWeather_string6()[4] + " " + m.getWeather_wind()[4] + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            weatherinfo.append("五天：" + m.getWeather_temp()[5] + " " + m.getWeather_string6()[5] + " " + m.getWeather_wind()[5] + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Html.fromHtml(weatherinfo.toString());


    }

    static final void creatLife(MyWeather m, StringBuilder weatherinfo) {
        try {
            weatherinfo.append(m.getLifeTitle() + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            weatherinfo.append("<b><font color=\"#32B4E6\">衣着建议：</b>" + m.getLifeInfo_feel_detail_24().replace(" ", "<br>") + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            weatherinfo.append("<b><font color=\"#32B4E6\">紫外线：</b>" + m.getLifeInfo_uv_24().replace(" ", "<br>") + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            weatherinfo.append("<b><font color=\"#32B4E6\">晨练：</b>" + m.getLifeInfo_cl().replace(" ", "<br>") + "<br>" + "<b><font color=\"#32B4E6\">运动：</b>" + m.getLifeInfo_sports().replace(" ", "<br>") + "<br>" + "<b><font color=\"#32B4E6\">晾晒：</b>" + m.getLifeInfo_ls().replace(" ", "<br>") + "<br>" + "<b><font color=\"#32B4E6\">洗车：</b>" + m.getLifeInfo_xc().replace(" ", "<br>") + "<br>" + "<b><font color=\"#32B4E6\">出游：</b>" + m.getLifeInfo_tr().replace(" ", "<br>") + "<br>");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class WeatherInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WeatherStatebar.BROADCAST_STATEBAR_SHOWINFO)) {
                new WeatherInfo(context).show();
                UpdateReceiver.sendUpdateBroadcast(context);
            }
        }
    }
}