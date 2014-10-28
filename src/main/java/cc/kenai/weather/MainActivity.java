package cc.kenai.weather;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.kenai.function.setting.XSetting;
import com.kenai.function.state.XState;

import cc.kenai.common.ad.LoadDialog;
import cc.kenai.meizu.MZActivity;
import cc.kenai.weather.fragment.MainFragment;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        MZActivity.flyme4PrenferenceActivity(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainfragment, new MainFragment()).commit();
        if (XState.get_isfirst(this)) {
            LoadDialog.showDialog(this);
        }

        XSetting.xset_string_int(this, "weather_area", "010100");

    }


}
