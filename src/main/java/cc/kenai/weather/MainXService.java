package cc.kenai.weather;

import android.content.Context;
import android.content.Intent;

import cc.kenai.function.base.XService;
import cc.kenai.weather.utils.WeatherStatebar;
import cc.kenai.weather.utils.WeatherUtilsBykenai;

public class MainXService extends XService {

    public MainXService(Context context) {
        super(context);
        // TODO 自动生成的构造函数存根
    }

    @Override
    public void xCreate() {
        WeatherUtilsBykenai.reload(context);
        WeatherStatebar.init(context);
        UpdateReceiver.cancelUpdateBroadcast(context);
        UpdateReceiver.sendUpdateBroadcast(context);
    }

    @Override
    public void xDestroy() {
    }

    @Override
    public void xstart(Intent arg0) {
    }
}
