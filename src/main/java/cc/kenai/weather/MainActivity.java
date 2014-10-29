package cc.kenai.weather;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.kenai.function.state.XState;

import cc.kenai.common.ad.LoadDialog;
import cc.kenai.meizu.MZActivity;
import cc.kenai.umeng.UmengHelper;
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

        startService(new Intent(this, MainService.class));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        UmengHelper.updateOnlineConfig(this);
        UmengHelper.update_auto(this, "ffbf487cea6d4064a14c7a364e8481a2");
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengHelper.onActivityPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengHelper.onActivityResume(this);
    }


}
