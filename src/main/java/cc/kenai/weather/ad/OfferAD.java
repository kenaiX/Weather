package cc.kenai.weather.ad;

import android.content.Context;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;

//import com.qq.e.appwall.GdtAppwall;


/**
 * Created by kenai on 14-3-19.
 */
public class OfferAD {
    public final static void prepare(Context context) {
        AdManager.getInstance(context).init("4e49671c5838de55", "879cf41079394bb7", false);
        OffersManager.getInstance(context).onAppLaunch();
//        GdtAppwall.init(context, "1101260878", "9007479621874878611",false);

    }

    public final static void show(Context context) {
        OffersManager.getInstance(context).showOffersWall();
//        GdtAppwall.showAppwall();
    }

    public final static void quit(Context context) {
        OffersManager.getInstance(context).onAppExit();
    }
}
