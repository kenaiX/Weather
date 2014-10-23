package cc.kenai.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kenai.function.setting.XSetting;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String locIntent = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(locIntent)) {
            if (XSetting
                    .xget_boolean(context, "autostart")) {
                context.startService(new Intent(context,
                        MainService.class));
            }
        }

    }
}
