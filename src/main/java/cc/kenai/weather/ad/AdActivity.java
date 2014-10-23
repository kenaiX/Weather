package cc.kenai.weather.ad;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import cc.kenai.function.base.BacePreferenceActivity;
import cc.kenai.weather.R;

/**
 * Created by kenai on 14-3-20.
 */
public class AdActivity extends BacePreferenceActivity {
    public AdActivity() {
        super(TYPE_USUAL, false);
    }

    @Override
    public void xCreatePrepare() {

    }

    @Override
    public void xCreate(Bundle savedInstanceState) {
        addPreferencesFromResource(R.xml.weather_settings_ad);
        PreferenceScreen shuoming = (PreferenceScreen) findPreference("shuoming");
        shuoming.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdActivity.this);
                builder.setTitle("广告说明");
                builder.setMessage("天气详情以及锁屏天气右上角页面会显示广告入口，不点击不会消耗流量，非wifi下点击会有二次确认。\n\n通过下方的去广告下载获得积分后即永久去除此广告。（建议在wifi下操作！）\n\n谢谢！");
                builder.create().show();
                return true;
            }
        });
        PreferenceScreen adremove = (PreferenceScreen) findPreference("adremove");
        adremove.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                OfferAD.prepare(AdActivity.this);
                OfferAD.show(AdActivity.this);
                return true;
            }
        });
    }

    @Override
    public void xDestroy() {
        OfferAD.quit(this);
    }

    @Override
    public void xPause() {

    }

    @Override
    public void xResume() {

    }
}
