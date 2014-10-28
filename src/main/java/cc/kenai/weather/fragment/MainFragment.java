package cc.kenai.weather.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.preference.PreferenceFragment;

import com.kenai.function.setting.XSetting;

import cc.kenai.common.ad.KenaiTuiguang;
import cc.kenai.common.program.Question;
import cc.kenai.common.stores.StoreUtil;
import cc.kenai.weather.R;

/**
 * Created by kenai on 14/10/27.
 */
public class MainFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.settings);
        load_button();
        XSetting.getSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        XSetting.getSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals("weather_lock_mode")) {
//            if (XSetting.xget_boolean(this, "weather_lock_mode")) {
//                startService(new Intent(this,
//                        WeatherLockService.class));
//            }
//        } else if (key.equals("weather_mode")) {
//            if (XSetting.xget_boolean(this, "weather_mode")) {
//                startService(new Intent(this,
//                        WeatherLockService.class));
//            }
//        }
    }


    private final void load_button() {

        PreferenceScreen weather = (PreferenceScreen) findPreference("weather");
        weather.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {

                return true;
            }
        });
        PreferenceScreen esc = (PreferenceScreen) findPreference("Esc");
        esc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                System.exit(0);

                return true;
            }
        });
        PreferenceScreen call = (PreferenceScreen) findPreference("call");
        call.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Question.NotificationAndDialog(getActivity());

                return false;
            }
        });

        load_button_tuijian();
    }

    private final void load_button_tuijian() {

        StoreUtil.bindClick(getActivity(), findPreference("tuijian_dream"), "dba0d8630ada406dbab446434568bd32");

        StoreUtil.bindClick(getActivity(), findPreference("tuijian_melody"), "a1156e07ad7e4f1bba05014c88b3b98c");

        StoreUtil.bindClick(getActivity(), findPreference("zhichi"), "ffbf487cea6d4064a14c7a364e8481a2");

        findPreference("tuiguang").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                KenaiTuiguang.show(getActivity());
                return true;
            }
        });


    }
}
