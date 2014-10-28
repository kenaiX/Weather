package cc.kenai.weather;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.ServiceCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class MainService extends Service {
    WeatherServiceCompat serviceCompat = new WeatherServiceCompat();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceCompat.create(this);

//        for (int i = 0; i < 2; i++) {
//            add(new ProgressBar(this));
//        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return ServiceCompat.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceCompat.destroy(this);
    }

    public void add(View v) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        layoutParams.format = PixelFormat.RGBA_8888;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels;
        layoutParams.height = dm.heightPixels;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        wm.addView(v, layoutParams);
    }

}
