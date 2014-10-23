package cc.kenai.weather.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class MyWeather {
    @Override
    public String toString() {
        return this.weather.toString();
    }

    public MyWeather(JSONObject weather) {
        this.weather = weather;
    }

    public JSONObject weather;

    public final String getCityInfo_cityname() throws JSONException {
        return weather.getString("city");
    }

    public final String getCityInfo_date() throws JSONException {
        return weather.getString("date_y");
    }

//    public final String getCityInfo_week() throws JSONException {
//        return weather.getString("week");
//    }

    public final String getCityInfo_fchh() throws JSONException {
        return weather.getString("fchh");
    }

    public final String[] getWeather_temp() throws JSONException {
        String[] s = new String[6];
        for (int i = 0; i < 6; i++) {
            s[i] = weather.getString("temp" + (i + 1));
        }
        return s;
    }

    public final String[] getWeather_string6() throws JSONException {
        String[] s = new String[6];
        for (int i = 0; i < 6; i++) {
            s[i] = weather.getString("weather" + (i + 1));
        }
        return s;
    }

//    public final String[] getWeather_string12() throws JSONException {
//        String[] s = new String[12];
//        for (int i = 0; i < 12; i++) {
//            s[i] = weather.getString("img_title" + (i + 1));
//        }
//        return s;
//    }

    public final String[] getWeather_wind() throws JSONException {
        String[] s = new String[6];
        for (int i = 0; i < 6; i++) {
            s[i] = weather.getString("wind" + (i + 1));
        }
        return s;
    }

//    /**
//     * 衣着
//     */
//    public final String getLifeInfo_feel_24() throws JSONException {
//        return weather.getString("index");
//    }


//    /**
//     * 衣着
//     */
//    public final String getLifeInfo_feel_detail_48() throws JSONException {
//        return weather.getString("index48_d");
//    }

    /**
     * 紫外线
     */
    public final String getLifeInfo_uv_24() throws JSONException {
        return weather.getString("index_uv");
    }

//    /**
//     * 紫外线
//     */
//    public final String getLifeInfo_uv_48() throws JSONException {
//        return weather.getString("index48_uv");
//    }

    /**
     * 洗车
     */
    public final String getLifeInfo_xc() throws JSONException {
        return weather.getString("index_xc");
    }

    /**
     * 旅游
     */
    public final String getLifeInfo_tr() throws JSONException {
        return weather.getString("index_tr");
    }

//    /**
//     * 舒适指数
//     */
//    public final String getLifeInfo_co() throws JSONException {
//        return weather.getString("index_co");
//    }

    /**
     * 晨练
     */
    public final String getLifeInfo_cl() throws JSONException {
        return weather.getString("index_cl");
    }

    /**
     * 晾晒
     */
    public final String getLifeInfo_ls() throws JSONException {
        return weather.getString("index_ls");
    }

//    /**
//     * 过敏
//     */
//    public final String getLifeInfo_ag() throws JSONException {
//        return weather.getString("index_ag");
//    }


    /**
     * 概况
     */
    public final String getInfo() throws JSONException {
        return weather.getString("info");
    }

    /**
     * 衣着
     */
    public final String getLifeInfo_feel_detail_24() throws JSONException {
        return weather.getString("index_d");
    }

    /**
     * sports
     */
    public final String getLifeInfo_sports() throws JSONException {
        return weather.getString("index_sp");
    }

    /**
     * title
     */
    public final String getLifeTitle() throws JSONException {
        return weather.getString("life_title");
    }
}
