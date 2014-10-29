package cc.kenai.weather.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import cc.kenai.weather.R;
import cc.kenai.weather.event.StateBarControl;
import cc.kenai.weather.event.UpdateStateIfNeed;
import cc.kenai.weather.pojos.WeatherPojo;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

public class WeatherInfo {
    Context context;
    final View v;

    public WeatherInfo(Context context) {
        this.context = context;
        v = View.inflate(context, R.layout.weather_info, null);
    }

    public void show() {
        Button bt1 = (Button) v.findViewById(R.id.bt1_weather);
        bt1.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                dismiss();
                EventBus.getDefault().post(new StateBarControl.Dismiss());
            }
        });

        Button bt2 = (Button) v.findViewById(R.id.bt2_weather);
        bt2.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                dismiss();
            }
        });


        if (!v.isShown()) {
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).addView(v, getLayoutParams(context));
        }

        EventBus.getDefault().registerSticky(this);
    }

    public void dismiss() {
        EventBus.getDefault().unregister(this);
        if (v.isShown()) {
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(v);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @DebugLog
    public void onEventMainThread(UpdateStateIfNeed event) {
        if (event != null) {
            TextView viewById = (TextView) v.findViewById(R.id.tx_weather);
            viewById.setText(createText(event.weatherPojo));
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


    //todo 某些时候可能为空
    static Spanned createText(WeatherPojo pojo) {

        StringBuilder weatherinfo = new StringBuilder();

        WeatherFnString f1String = new WeatherFnString(pojo.fc.f1);

        weatherinfo.append("<big>" + pojo.now.ct + "</big>" + " " + pojo.fc.f1.dt + "<br>");

        weatherinfo.append(f1String.getWeatherMay() + " " + f1String.getTemMay() + " " + f1String.getWindMay() + " " + f1String.getWindSpeedMay() + "<br>");


        if (pojo.en.x != null) {
            weatherinfo.append("<p><font color=\"#32B4E6\">空气：</font>" + pojo.en.ql + "  aqi:" + pojo.en.aqi + "<br>" + pojo.en.ss + "</p>");
        }

        {
            WeatherFnString fiString = new WeatherFnString(pojo.fc.f2);
            weatherinfo.append("<br>" + "明天：" + fiString.getWeatherMay() + " " + fiString.getTemMay() + " " + fiString.getWindMay() + " " + fiString.getWindSpeedMay() + "<br>");
        }

        {
            WeatherFnString fiString = new WeatherFnString(pojo.fc.f3);
            weatherinfo.append("<br>" + "后天：" + fiString.getWeatherMay() + " " + fiString.getTemMay() + " " + fiString.getWindMay() + " " + fiString.getWindSpeedMay() + "<br>");
        }
        {
            WeatherFnString fiString = new WeatherFnString(pojo.fc.f4);
            weatherinfo.append("<br>" + "三天：" + fiString.getWeatherMay() + " " + fiString.getTemMay() + " " + fiString.getWindMay() + " " + fiString.getWindSpeedMay() + "<br>");
        }
        {
            WeatherFnString fiString = new WeatherFnString(pojo.fc.f5);
            weatherinfo.append("<br>" + "四天：" + fiString.getWeatherMay() + " " + fiString.getTemMay() + " " + fiString.getWindMay() + " " + fiString.getWindSpeedMay() + "<br>");
        }

        {
            WeatherFnString fiString = new WeatherFnString(pojo.fc.f6);
            weatherinfo.append("<br>" + "五天：" + fiString.getWeatherMay() + " " + fiString.getTemMay() + " " + fiString.getWindMay() + " " + fiString.getWindSpeedMay() + "<br>");
        }
        {
            WeatherFnString fiString = new WeatherFnString(pojo.fc.f7);
            weatherinfo.append("<br>" + "六天：" + fiString.getWeatherMay() + " " + fiString.getTemMay() + " " + fiString.getWindMay() + " " + fiString.getWindSpeedMay() + "<br>");
        }
        {
            WeatherFnString fiString = new WeatherFnString(pojo.fc.f1);
            weatherinfo.append("<br>" + "七天：" + fiString.getWeatherMay() + " " + fiString.getTemMay() + " " + fiString.getWindMay() + " " + fiString.getWindSpeedMay() + "<br>");
        }
        return Html.fromHtml(weatherinfo.toString());


    }

//    static final void creatLife(MyWeather m, StringBuilder weatherinfo) {
//        try {
//            weatherinfo.append(m.getLifeTitle() + "<br>");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            weatherinfo.append("<b><font color=\"#32B4E6\">衣着建议：</b>" + m.getLifeInfo_feel_detail_24().replace(" ", "<br>") + "<br>");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            weatherinfo.append("<b><font color=\"#32B4E6\">紫外线：</b>" + m.getLifeInfo_uv_24().replace(" ", "<br>") + "<br>");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            weatherinfo.append("<b><font color=\"#32B4E6\">晨练：</b>" + m.getLifeInfo_cl().replace(" ", "<br>") + "<br>" + "<b><font color=\"#32B4E6\">运动：</b>" + m.getLifeInfo_sports().replace(" ", "<br>") + "<br>" + "<b><font color=\"#32B4E6\">晾晒：</b>" + m.getLifeInfo_ls().replace(" ", "<br>") + "<br>" + "<b><font color=\"#32B4E6\">洗车：</b>" + m.getLifeInfo_xc().replace(" ", "<br>") + "<br>" + "<b><font color=\"#32B4E6\">出游：</b>" + m.getLifeInfo_tr().replace(" ", "<br>") + "<br>");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static class WeatherInfoReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(WeatherStatebarUtil.BROADCAST_STATEBAR_SHOWINFO)) {
//                new WeatherInfo(context).show();
//                UpdateReceiver.sendUpdateBroadcast(context);
//            }
//        }
//    }
}