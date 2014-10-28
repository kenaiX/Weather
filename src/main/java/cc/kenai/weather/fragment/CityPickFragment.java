package cc.kenai.weather.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kenai.function.setting.XSetting;

import cc.kenai.citypicker.CityPicker;
import cc.kenai.weather.R;

public class CityPickFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_citypicker, container, false);
        final CityPicker cityPicker = (CityPicker) view.findViewById(R.id.citypicker);
        view.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XSetting.xset_string_int(getActivity(), "weather_area_name", cityPicker.getcity_name());
                XSetting.xset_string_int(getActivity(), "weather_area", cityPicker.getCity_code().substring(3, 9));
                Toast.makeText(getActivity(), cityPicker.getCity_code().substring(3, 9), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        view.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setTitle("选择城市");
    }
}
