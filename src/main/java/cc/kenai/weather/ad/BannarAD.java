package cc.kenai.weather.ad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kenai.function.message.XToast;
import com.kenai.function.setting.XSetting;

import net.ecom.android.EcomManager;
import net.ecom.android.ecom.EcManager;

import cc.kenai.weather.R;
import cc.kenai.weather.utils.WeatherInfo;
import cc.kenai.weather.utils.WeatherLock;

public class BannarAD {

    public static void bindAD(final Context context, final WeatherInfo weatherInfo, View v) {
        LinearLayout adLayout = (LinearLayout) v.findViewById(R.id.adLayout);
        if (!XSetting.xget_boolean(context, "youmi") && XSetting.xget_boolean(context, "weather_lock_mode")) {
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && networkInfo.isAvailable()) {
                {
                    Button button = new Button(context);
                    button.setText("天天特价！");
                    adLayout.addView(button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                weatherInfo.dismiss();
                                EcomManager.getInstance(context).init("4e49671c5838de55", "879cf41079394bb7", false);
                                EcManager.showEcomView(context);
                            } catch (Exception e) {
                                XToast.xToast(context, "出现错误");
                            }
                        }
                    });
                }
                return;

            } else {
                {
                    Button button = new Button(context);
                    button.setText("天天特价！");
                    adLayout.addView(button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            weatherInfo.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme);
                            builder.setMessage("即将打开的页面会消耗少量流量。");
                            builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        EcomManager.getInstance(context).init("4e49671c5838de55", "879cf41079394bb7", false);
                                        EcManager.showEcomView(context);
                                    } catch (Exception e) {
                                        XToast.xToast(context, "出现错误");
                                    }
                                }
                            });
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            AlertDialog dialog = builder.create();

                            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ERROR));
                            dialog.show();


                        }
                    });
                }
                return;
            }
        }

        adLayout.setVisibility(View.GONE);
        return;


//        //获取要嵌入迷你广告条的布局
//        RelativeLayout adLayout = (RelativeLayout) getMianView().findViewById(R.id.AdLayout);
//        //demo 1 迷你Banner : 宽满屏，高32dp
//        DiyBanner banner = new DiyBanner(context, DiyAdSize.SIZE_MATCH_SCREENx32);//传入高度为32dp的AdSize来定义迷你Banner
//        //demo 2 迷你Banner : 宽320dp，高32dp
////        DiyBanner banner = new DiyBanner(this, DiyAdSize.SIZE_320x32);//传入高度为32dp的AdSize来定义迷你Banner
//        //将积分Banner加入到布局中
//        adLayout.addView(banner);


//        TextView tx = (TextView) getMianView().findViewById(R.id.ad);
    }


    public static void bindAD_lock(final Context context, final WeatherLock weatherLock, View v) {
        ImageView img = (ImageView) v.findViewById(R.id.weather_lock_ad);
        if (!XSetting.xget_boolean(context, "youmi")) {
            img.setImageResource(R.drawable.gouwuche);
            final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && networkInfo.isAvailable()) {
                {

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                weatherLock.dismiss();
                                EcomManager.getInstance(context).init("4e49671c5838de55", "879cf41079394bb7", false);
                                EcManager.showEcomView(context);
                            } catch (Exception e) {
                                XToast.xToast(context, "出现错误");
                            }
                        }
                    });
                }
                return;

            } else {
                {
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            weatherLock.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme);
                            builder.setMessage("即将打开的页面会消耗少量流量。");
                            builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        EcomManager.getInstance(context).init("4e49671c5838de55", "879cf41079394bb7", false);
                                        EcManager.showEcomView(context);
                                    } catch (Exception e) {
                                        XToast.xToast(context, "出现错误");
                                    }
                                }
                            });
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            AlertDialog dialog = builder.create();

                            dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ERROR));
                            dialog.show();


                        }
                    });
                }
                return;
            }
        }
    }
}


//                {
//
//                    AdManager.getInstance(context).init("4e49671c5838de55", "879cf41079394bb7", false);
//                    //实例化广告条
//                    OffersBanner banner = new OffersBanner(context, OffersAdSize.SIZE_MATCH_SCREENx60);//传入高度为60dp的OffersAdSize来定义积分Banner
//
////                    AdView adView = new AdView(context, AdSize.FIT_SCREEN);
//                    //获取要嵌入广告条的布局
//                    //将广告条加入到布局中
//                    adLayout.addView(banner);
//                }

//                {
//                    AdView adv = new AdView(context, AdSize.BANNER, "1101260878", "9079537215912806547");
//                    adLayout.addView(adv);
///* 广告请求数据,可以设置广告轮播时间,默认为30s */
//                    AdRequest adr = new AdRequest();
///* 这个接口的作用是设置广告的测试模式,该模式下点击不扣费 * 未发布前请设置testad为true,
//* 上线的版本请确保设置为false或者去掉这行调用
//*/
//                    adr.setTestAd(true);
///* 设置广告刷新时间,为30~120之间的数字,单位为s,0标识不自动刷新*/
//                    adr.setRefresh(31);
///* 设置空广告和首次收到广告数据回调
//* 调用fetchAd方法后会发起广告请求,广告轮播时不会产生回调
//*/
//
///* 发起广告请求,收到广告数据后会展示数据 */
//                    adv.fetchAd(adr);
//                }