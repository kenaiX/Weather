package cc.kenai.umeng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UmengHelper {

    //自动更新
    public static void update_auto(final Activity activity, final String storeId) {
        final SharedPreferences shared = activity.getSharedPreferences("umeng-update", 0);
        long lasttime = shared.getLong("lasttime", 0);
        final long thisTime = System.currentTimeMillis();
        if (Math.abs(thisTime - lasttime) > 24 * 60 * 60 * 1000) {

            UmengUpdateAgent.setUpdateAutoPopup(false);
            UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                @Override
                public void onUpdateReturned(int updateStatus, final UpdateResponse updateInfo) {
                    switch (updateStatus) {
                        case UpdateStatus.Yes: // has update
                            shared.edit().putLong("lasttime", thisTime).commit();
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage("新版本高能反应！");
                            builder.setPositiveButton("查看更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        activity.startActivity(new Intent(
                                                "android.intent.action.VIEW",
                                                Uri.parse("mstore:http://app.meizu.com/phone/apps/" + storeId)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    } catch (Exception e) {
                                        UmengUpdateAgent.showUpdateDialog(activity, updateInfo);
                                    }
                                }
                            });
//                        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                UmengUpdateAgent.ignoreUpdate(activity, updateInfo);
//                            }
//                        });
                            builder.setNegativeButton("下载更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UmengUpdateAgent.showUpdateDialog(activity, updateInfo);
                                }
                            });
                            builder.create().show();
                            break;
                        case UpdateStatus.No: // has no update
                            shared.edit().putLong("lasttime", thisTime).commit();
                            break;
                        case UpdateStatus.NoneWifi: // none wifi
                            break;
                        case UpdateStatus.Timeout: // time out
                            shared.edit().putLong("lasttime", thisTime).commit();
                            break;
                    }
                }
            });
            UmengUpdateAgent.update(activity);
        }
    }


    //统计接口

    //获取上传策略
    public static void updateOnlineConfig(Activity context) {
        MobclickAgent.updateOnlineConfig(context);
    }

    public static void onActivityPause(Activity context) {
        MobclickAgent.onPause(context);
    }

    public static void onActivityResume(Activity context) {
        MobclickAgent.onResume(context);
    }


    public static void onKillProcess(Context context) {
        MobclickAgent.onKillProcess(context);
    }

    public static void onEvent(Context context, String s) {
        MobclickAgent.onEvent(context, s);
    }

    public static void onEventBegin(Context context, String s) {
        MobclickAgent.onEventBegin(context, s);
    }

    public static void onEventEnd(Context context, String s) {
        MobclickAgent.onEventEnd(context, s);
    }

}
