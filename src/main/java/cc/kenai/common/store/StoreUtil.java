package cc.kenai.common.store;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;

/**
 * Created by kenai on 13-12-2.
 */
public class StoreUtil {
    public static void bindClick(final Context context, final Preference p, final String id) {
        p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                try {
                    context.startActivity(new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("mstore:http://app.meizu.com/phone/apps/" + id)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (Exception e) {

                }
                return true;
            }
        });
    }
}
