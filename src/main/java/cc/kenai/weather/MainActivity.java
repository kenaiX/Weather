package cc.kenai.weather;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.kenai.function.setting.XSetting;
import com.kenai.function.state.XState;

import cc.kenai.common.ad.KenaiTuiguang;
import cc.kenai.common.ad.LoadDialog;
import cc.kenai.common.program.Question;
import cc.kenai.common.store.StoreUtil;
import cc.kenai.function.base.BacePreferenceActivity;
import cc.kenai.weather.ad.AdActivity;

public class MainActivity extends BacePreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public MainActivity() {
        super(TYPE_RELEASE_AFTER_PAUSE, false);
    }

    @Override
    public void xCreatePrepare() {
    }

    @Override
    public void xCreate(Bundle bundle) {
        addPreferencesFromResource(R.xml.settings);
        XSetting.getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        load_button();

        if (XSetting.xget_boolean(this, "weather_mode")) {
            startService(new Intent(this, MainService.class));
        }

        if (XState.get_isfirst(this)) {
            LoadDialog.showDialog(this);
        }
    }

    @Override
    public void xDestroy() {
        XSetting.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void xPause() {

    }

    @Override
    public void xResume() {

    }

    private final void load_button() {
        PreferenceScreen aboutAd = (PreferenceScreen) findPreference("aboutAd");
        aboutAd.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {

                startActivity(new Intent(getBaseContext(), AdActivity.class));
                return true;
            }
        });

        PreferenceScreen weather = (PreferenceScreen) findPreference("weather");
        weather.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {

                startActivity(new Intent(getBaseContext(), WeatherActivity.class));
                return true;
            }
        });
        PreferenceScreen esc = (PreferenceScreen) findPreference("Esc");
        esc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                stopService(new Intent(getBaseContext(),
                        MainService.class));

                finish();
//                System.exit(0);

                return true;
            }
        });
        PreferenceScreen call = (PreferenceScreen) findPreference("call");
        call.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Question.NotificationAndDialog(MainActivity.this);

                return false;
            }
        });

        load_button_tuijian();
    }

    private final void load_button_tuijian() {

        StoreUtil.bindClick(this, findPreference("tuijian_dream"), "dba0d8630ada406dbab446434568bd32");

        StoreUtil.bindClick(this, findPreference("tuijian_melody"), "a1156e07ad7e4f1bba05014c88b3b98c");

        StoreUtil.bindClick(this, findPreference("zhichi"), "ffbf487cea6d4064a14c7a364e8481a2");

        findPreference("tuiguang").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                KenaiTuiguang.show(getBaseContext());
                return true;
            }
        });


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("weather_lock_mode")) {
            if (XSetting.xget_boolean(this, "weather_lock_mode")) {
                startService(new Intent(this,
                        MainService.class));
            }
        } else if (key.equals("weather_mode")) {
            if (XSetting.xget_boolean(this, "weather_mode")) {
                startService(new Intent(this,
                        MainService.class));
            }
        }
    }
}
