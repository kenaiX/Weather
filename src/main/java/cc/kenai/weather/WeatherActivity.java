package cc.kenai.weather;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

import com.kenai.function.message.XLog;
import com.kenai.function.message.XToast;
import com.kenai.function.setting.XSetting;
import com.kenai.function.state.XState;

import cc.kenai.function.base.BacePreferenceActivity;
import cc.kenai.weather.utils.MyArea;
import cc.kenai.weather.utils.MyCity;
import cc.kenai.weather.utils.MyProvince;
import cc.kenai.weather.utils.WeatherStatebar;
import cc.kenai.weather.utils.WeatherUtilsBykenai;

public class WeatherActivity extends BacePreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public WeatherActivity() {
        super(TYPE_RELEASE_AFTER_PAUSE, true);
        // TODO 自动生成的构造函数存根
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成的方法存根

    }

    private ListPreference choosePre;
    private ListPreference chooseCity;
    private ListPreference chooseArea;
    private Preference test;
    private Handler handler = new Handler();

    void setEnable(final Preference p, final boolean b) {
        handler.post(new Runnable() {

            public void run() {
                p.setEnabled(b);
            }
        });

    }

    void setValue(final ListPreference p, final String[] s1, final String[] s2) {
        handler.post(new Runnable() {

            public void run() {
                p.setEntries(s1);
                p.setEntryValues(s2);
            }
        });

    }

    void showdialog(final String message) {
        handler.post(new Runnable() {

            public void run() {
                XLog.xLog(message);
            }
        });

    }

    private boolean isSpecial = false;

    private final void getProvince() {
        new Thread(new Runnable() {
            public void run() {
                setEnable(choosePre, false);
                setEnable(chooseCity, false);
                setEnable(chooseArea, false);

                try {
                    MyProvince[] result;
                    result = WeatherUtilsBykenai.getAllProvince();
                    int n = result.length;
                    String[] name = new String[n];
                    String[] id = new String[n];

                    for (int i = 0; i < n; i++) {
                        name[i] = result[i].name;
                        id[i] = result[i].id;
                    }
                    setValue(choosePre, name, id);

                    setEnable(choosePre, true);
                } catch (Exception e) {
                    // 发生超时,返回值区别于null与正常信息
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void xCreate(Bundle arg0) {
        if (XSetting.xget_string(this, "weather_area").length() < 1) {
            XSetting.xset_string_int(this, "weather_area", "010100");
        }
        if (XSetting.xget_string(this, "weather_area_aqi").length() < 1) {
            XSetting.xset_string_int(this, "weather_area_aqi", "beijing");
        }
        addPreferencesFromResource(R.xml.mainsettings_weather);
        choosePre = (ListPreference) findPreference("weather_provinces");
        chooseCity = (ListPreference) findPreference("weather_city");
        chooseArea = (ListPreference) findPreference("weather_area");
        EditTextPreference et = (EditTextPreference) findPreference("weather_area_aqi");
        et.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, final Object newValue) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WeatherUtilsBykenai.WeatherAQI.getWeather_AQI("" + newValue, WeatherActivity.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "支持此城市", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                                    builder.setTitle("AQI获取错误");
                                    builder.setMessage("网络验证错误或暂不支持此城市：" + newValue + "\n请输入城市名拼音\n例如：天津 请输入 tianjin，海淀 请输入 beijing");
                                    builder.create().show();

                                }
                            });
                        }
                    }
                }).start();
                return true;
            }
        });
        test = (Preference) findPreference("weather_test");


        getProvince();

        choosePre
                .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference,
                                                      final Object newValue) {
                        new Thread(new Runnable() {
                            public void run() {
                                setEnable(chooseCity, false);
                                setEnable(chooseArea, false);

                                String city = (String) newValue;
                                try {
                                    MyCity[] result;
                                    result = WeatherUtilsBykenai.getCity(city);
                                    int n = result.length;
                                    String[] name = new String[n];
                                    String[] id = new String[n];

                                    isSpecial = name.length == 1;
                                    for (int i = 0; i < n; i++) {
                                        name[i] = result[i].name;
                                        id[i] = result[i].id;
                                    }
                                    setValue(chooseCity, name, id);

                                    setEnable(chooseCity, true);
                                } catch (Exception e) {
                                    // 发生超时,返回值区别于null与正常信息
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        return true;
                    }
                });

        chooseCity
                .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(Preference preference,
                                                      final Object newValue) {
                        new Thread(new Runnable() {

                            public void run() {
                                setEnable(chooseArea, false);

                                String city = (String) newValue;
                                try {
                                    MyArea[] result;
                                    result = WeatherUtilsBykenai.getArea(city);
                                    int n = result.length;
                                    String[] name = new String[n];
                                    String[] id = new String[n];
                                    if (!isSpecial) {
                                        XLog.xLog("not special area");
                                        for (int i = 0; i < n; i++) {
                                            id[i] = result[i].id;
                                            name[i] = result[i].name;

                                        }
                                    } else {
                                        XLog.xLog("special area");
                                        for (int i = 0; i < n; i++) {
                                            id[i] = result[i].id
                                                    .substring(0, 2)
                                                    + result[i].id.substring(4,
                                                    6) + "00";
                                            name[i] = result[i].name;
                                        }
                                    }

                                    setValue(chooseArea, name, id);

                                    setEnable(chooseArea, true);
                                } catch (Exception e) {
                                    // 发生超时,返回值区别于null与正常信息
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        return true;
                    }
                });

        test.setOnPreferenceClickListener(new OnPreferenceClickListener() {


            public boolean onPreferenceClick(Preference preference) {
                getBaseContext().sendBroadcast(new Intent(WeatherStatebar.BROADCAST_STATEBAR_SHOWSTATE));
                XToast.xToast(cc.kenai.weather.WeatherActivity.this, "顶栏若未刷新，则网络故障");


                AlertDialog.Builder builder = new AlertDialog.Builder(cc.kenai.weather.WeatherActivity.this);
                builder.setTitle("说明：");
                builder.setMessage("若顶栏无法显示：\n系统设置-辅助功能-应用-已下载\n" +
                        "里面把不显示顶栏的应用的 『显示通知』打开");
                builder.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }
                );

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                return false;
            }
        });

        XSetting.getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void xCreatePrepare() {
        if (XState.get_issdk14())
            setTheme(16974123);
    }

    @Override
    public void xDestroy() {
        XSetting.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void xPause() {
        // TODO 自动生成的方法存根

    }

    @Override
    public void xResume() {
        // TODO 自动生成的方法存根

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.startsWith("weather_area")) {
            WeatherUtilsBykenai.reload(getBaseContext());
            getBaseContext().sendBroadcast(new Intent(WeatherStatebar.BROADCAST_STATEBAR_SHOWSTATE));
        } else if (key.equals("weather_mode")) {
            if (XSetting.xget_boolean(this, "weather_mode")) {
                new MainXService(this).xCreate();
            } else {
                WeatherStatebar.cancel_notification(this);
            }
        }
    }

}
