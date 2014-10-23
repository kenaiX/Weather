package cc.kenai.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kenai.function.message.XLog;
import com.kenai.function.setting.XSetting;
import com.kenai.function.time.XTime;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import cc.kenai.weather.config.SharedConfig;

public class WeatherUtilsBykenai {
    private final static String Type = "UTF-8";

    public final static MyProvince[] getAllProvince()
            throws UnsupportedEncodingException {
        cc.kenai.weather.utils.HttpHelper hp = new cc.kenai.weather.utils.HttpHelper();
        String value_total;
        value_total = new String(hp.getHtml(
                "http://www.weather.com.cn/data/list3/city.xml").getBytes(Type));
        String[] provinceAndId = value_total.split(",");
        int n = provinceAndId.length;
        MyProvince[] mMyProvince = new MyProvince[n];
        for (int i = 0; i < n; i++) {
            mMyProvince[i] = new MyProvince(provinceAndId[i].substring(3),
                    provinceAndId[i].substring(0, 2));
            XLog.xLog("省份：" + mMyProvince[i].name + " id:" + mMyProvince[i].id);
        }
        return mMyProvince;
    }

    public final static MyCity[] getCity(String id)
            throws UnsupportedEncodingException {
        cc.kenai.weather.utils.HttpHelper hp = new cc.kenai.weather.utils.HttpHelper();
        String value_total;
        value_total = new String(hp.getHtml(
                "http://www.weather.com.cn/data/list3/city" + id + ".xml")
                .getBytes(Type));
        String[] provinceAndId = value_total.split(",");
        int n = provinceAndId.length;
        MyCity[] mMyProvince = new MyCity[n];
        for (int i = 0; i < n; i++) {
            mMyProvince[i] = new MyCity(provinceAndId[i].substring(5),
                    provinceAndId[i].substring(0, 4));

            XLog.xLog("城市：" + mMyProvince[i].name + " id:" + mMyProvince[i].id);
        }
        return mMyProvince;
    }

    public final static MyArea[] getArea(String id)
            throws UnsupportedEncodingException {
        cc.kenai.weather.utils.HttpHelper hp = new cc.kenai.weather.utils.HttpHelper();
        String value_total;
        value_total = new String(hp.getHtml(
                "http://www.weather.com.cn/data/list3/city" + id + ".xml")
                .getBytes(Type));
        String[] provinceAndId = value_total.split(",");
        int n = provinceAndId.length;
        MyArea[] mMyProvince = new MyArea[n];
        for (int i = 0; i < n; i++) {
            mMyProvince[i] = new MyArea(provinceAndId[i].substring(7),
                    provinceAndId[i].substring(0, 6));
            XLog.xLog("区域：" + mMyProvince[i].name + " id:" + mMyProvince[i].id);
        }
        return mMyProvince;
    }

    public static class INTERNET_ERROR extends Exception {

    }

    public final static void reload(Context context) {
        context.getSharedPreferences(SharedConfig.Filter, 0).edit().clear().commit();
    }


    public static class WeatherTotal {
        public final static MyWeather getWeather_huancun(Context context) throws JSONException {
            return new MyWeather(WeatherTotal.getWeather_json(context));
        }

        private static JSONObject getWeather_detail(String id, Context context)
                throws cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR {
            JSONObject json;
            XLog.xLog("即将获取的城市：" + id);
            String value_total = null;
            String api = "http://weather.kenai.cc/rest/1/weather/" + id;
            String cache = context.getSharedPreferences(SharedConfig.Filter, 0).getString(SharedConfig.CacheMD5, null);
            if (cache != null && cache.length() > 1) {
                XLog.xLog("cache", cache);
                api += "?cache=" + cache;
                XLog.xLog("cache:" + cache);
            }
            HttpClient hc = new DefaultHttpClient();
            hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);//连接时间20s
            hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
            HttpGet get = new HttpGet(api);
            try {
                HttpResponse response = hc.execute(get);
                int state = response.getStatusLine().getStatusCode();
                XLog.xLog("state:" + state);
                if (state == 200 || state == 201) {
                    value_total = EntityUtils.toString(response.getEntity(), "utf-8");
                    XLog.xLog("http re:" + value_total);
                    try {
                        EncryptUtil eu = new EncryptUtil();
                        String real;
                        try {
                            real = eu.md5Digest(value_total);
                            context.getSharedPreferences(SharedConfig.Filter, 0).edit().putString(SharedConfig.CacheMD5, real).commit();
                        } catch (UnsupportedEncodingException e) {

                        }
                    } catch (NoSuchAlgorithmException e) {

                    }
                } else if (state == 202 || state == 203) {
                    return getWeather_huancun(context).weather;
                }
            } catch (Exception e) {
                throw new cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR();
            } finally {
                hc.getConnectionManager().shutdown();
            }

            try {
                json = new JSONObject(value_total);
                json = json.getJSONObject("weatherinfo");
                saveWeather(context.getSharedPreferences(SharedConfig.Filter, 0), json);
            } catch (Exception e) {
                throw new cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR();
            }

            return json;
        }

        private static void saveWeather(SharedPreferences mShared, JSONObject weather) {
            mShared.edit().putString(SharedConfig.Weather_cache, weather.toString()).commit();
        }

        private static JSONObject getWeather_json(Context context) throws JSONException {
            String s = context.getSharedPreferences(SharedConfig.Filter, 0).getString(SharedConfig.Weather_cache, "");
            return new JSONObject(s);
        }

        public final static MyWeather getWeather(Context context)
                throws JSONException {
            JSONObject weather;
            SharedPreferences mShared = context.getSharedPreferences(SharedConfig.Filter, 0);
            String area_last = mShared.getString(SharedConfig.Area_Cache, "");
            String area_now = XSetting.xget_string(context, "weather_area");
            if (area_now.equals(area_last)) {
                int time_day_last = mShared.getInt(SharedConfig.Time_day, -10);
                int time_day_now = XTime.gettime_partly(3);
                if (time_day_last == time_day_now) {
                    int time_hour_last = mShared.getInt(SharedConfig.Time_hour, -10);
                    int time_now = XTime.gettime_partly(4);
                    if (time_now != time_hour_last) {
                        MyWeather mMyWeather = null;
                        try {
                            weather = getWeather_detail(area_now, context);
                            mMyWeather = new MyWeather(weather);
                        } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                            throw new JSONException("");
                        }
                        mShared.edit().putInt(SharedConfig.Time_fchh, Integer.valueOf(mMyWeather.getCityInfo_fchh())).commit();
                        mShared.edit().putInt(SharedConfig.Time_hour, time_now).commit();
                        XLog.xLog("同一天内不同时间点");
                        return mMyWeather;
                    } else {
                        XLog.xLog("同一天内相同时间点");
                        return getWeather_huancun(context);
                    }
                } else {
                    XLog.xLog("不同天");
                    MyWeather mMyWeather = null;
                    try {
                        weather = getWeather_detail(area_now, context);
                        mMyWeather = new MyWeather(weather);
                    } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                        throw new JSONException("");
                    }
                    mShared.edit().putInt(SharedConfig.Time_fchh, Integer.valueOf(mMyWeather.getCityInfo_fchh())).commit();
                    int time_now = XTime.gettime_partly(4);
                    mShared.edit().putInt(SharedConfig.Time_hour, time_now).commit();
                    mShared.edit().putInt(SharedConfig.Time_day, time_day_now).commit();
                    return mMyWeather;
                }

            } else {
                MyWeather mMyWeather = null;
                try {
                    weather = getWeather_detail(area_now, context);
                    mMyWeather = new MyWeather(weather);
                } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                    throw new JSONException("");
                }
                mShared.edit().putInt(SharedConfig.Time_fchh, Integer.valueOf(mMyWeather.getCityInfo_fchh())).commit();
                int time_now = XTime.gettime_partly(4);
                mShared.edit().putInt(SharedConfig.Time_hour, time_now).commit();
                int time_day_now = XTime.gettime_partly(3);
                mShared.edit().putInt(SharedConfig.Time_day, time_day_now).commit();
                mShared.edit().putString(SharedConfig.Area_Cache, area_now).commit();
                return mMyWeather;
            }

        }

    }

    public static class WeatherAQI {
        public final static MyAQIWeather getWeather_huancun_AQI(Context context) throws JSONException {
            return new MyAQIWeather(getWeather_json_AQI(context));
        }

        public synchronized static JSONObject getWeather_AQI(String id, Context context)
                throws cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR {
            XLog.xLog("即将获取的城市：" + id);
            String value_total = null;
            String api = "http://weather.kenai.cc/rest/1/weather/aqi?city=" + id;
            String cache = context.getSharedPreferences(SharedConfig.Filter, 0).getString(SharedConfig.CacheMD5_AQI, null);
            if (cache != null && cache.length() > 1) {
                XLog.xLog("cache", cache);
                api += "&cache=" + cache;
                XLog.xLog("cache:" + cache);
            }
            HttpClient hc = new DefaultHttpClient();
            hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);//连接时间20s
            hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
            HttpGet get = new HttpGet(api);
            try {
                HttpResponse response = hc.execute(get);
                int state = response.getStatusLine().getStatusCode();
                XLog.xLog("state:" + state);
                if (state == 200 || state == 201) {
                    value_total = EntityUtils.toString(response.getEntity(), "utf-8");
                    XLog.xLog("http re:" + value_total);
                    try {
                        EncryptUtil eu = new EncryptUtil();
                        String real;
                        try {
                            real = eu.md5Digest(value_total);
                            context.getSharedPreferences(SharedConfig.Filter, 0).edit().putString(SharedConfig.CacheMD5_AQI, real).commit();
                        } catch (UnsupportedEncodingException e) {

                        }
                    } catch (NoSuchAlgorithmException e) {

                    }
                } else if (state == 202 || state == 203) {
                    return getWeather_huancun_AQI(context).weather;
                }
            } catch (Exception e) {
                throw new cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR();
            } finally {
                hc.getConnectionManager().shutdown();
            }
            JSONObject json;
            try {
                json = new JSONArray(value_total).getJSONObject(0);
                saveWeather_AQI(context.getSharedPreferences(SharedConfig.Filter, 0), json);
            } catch (Exception e) {
                throw new cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR();
            }

            return json;
        }

        public final static MyAQIWeather getWeather_AQI(Context context)
                throws JSONException {
            JSONObject weather;
            SharedPreferences mShared = context.getSharedPreferences(SharedConfig.Filter, 0);
            String area_last = mShared.getString(SharedConfig.Area_Cache_AQI, "");
            String area_now = XSetting.xget_string(context, "weather_area_aqi");
            if (area_now == null || area_now.length() < 2) {
                throw new JSONException("no_area_AQI");
            }
            if (area_now.equals(area_last)) {
                int time_day_last = mShared.getInt(SharedConfig.Time_day_AQI, -10);
                int time_day_now = XTime.gettime_partly(3);
                if (time_day_last == time_day_now) {
                    int time_hour_last = mShared.getInt(SharedConfig.Time_hour_AQI, -10);
                    int time_now = XTime.gettime_partly(4);
                    if (time_now != time_hour_last) {
                        MyAQIWeather mMyWeather = null;
                        try {
                            weather = getWeather_AQI(area_now, context);

                            mMyWeather = new MyAQIWeather(weather);
                        } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                        }
                        if (mMyWeather == null) {
                            throw new JSONException("");
                        }
                        mShared.edit().putInt(SharedConfig.Time_hour_AQI, time_now).commit();
                        XLog.xLog("同一天内不同时间点");
                        return mMyWeather;
                    } else {
                        XLog.xLog("同一天内相同时间点");
                        return getWeather_huancun_AQI(context);
                    }
                } else {
                    XLog.xLog("不同天");
                    MyAQIWeather mMyWeather = null;
                    try {
                        weather = getWeather_AQI(area_now, context);
                        mMyWeather = new MyAQIWeather(weather);
                    } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                    }
                    if (mMyWeather == null) {
                        throw new JSONException("");
                    }
                    int time_now = XTime.gettime_partly(4);
                    mShared.edit().putInt(SharedConfig.Time_hour_AQI, time_now).commit();
                    mShared.edit().putInt(SharedConfig.Time_day_AQI, time_day_now).commit();
                    XLog.xLog("同一天内不同时间点");
                    return mMyWeather;
                }

            } else {
                MyAQIWeather mMyWeather = null;
                try {
                    weather = getWeather_AQI(area_now, context);
                    mMyWeather = new MyAQIWeather(weather);
                } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                }
                if (mMyWeather == null) {
                    throw new JSONException("");
                }
                int time_now = XTime.gettime_partly(4);
                mShared.edit().putInt(SharedConfig.Time_hour_AQI, time_now).commit();
                int time_day_now = XTime.gettime_partly(3);
                mShared.edit().putInt(SharedConfig.Time_day_AQI, time_day_now).commit();
                mShared.edit().putString(SharedConfig.Area_Cache_AQI, area_now).commit();
                return mMyWeather;
            }

        }

        private static void saveWeather_AQI(SharedPreferences mShared, JSONObject weather) {
            mShared.edit().putString(SharedConfig.Weather_cache_AQI, weather.toString()).commit();
        }

        private static JSONObject getWeather_json_AQI(Context context) throws JSONException {
            String s = context.getSharedPreferences(SharedConfig.Filter, 0).getString(SharedConfig.Weather_cache_AQI, "");
            return new JSONObject(s);
        }

    }

    public static class WeatherNOW {
        public final static MyNowWeather getWeather_huancun_NOW(Context context) throws JSONException {
            return new MyNowWeather(getWeather_json_NOW(context));
        }


        public final static MyNowWeather getWeather_NOW(Context context)
                throws JSONException {
            JSONObject weather;
            SharedPreferences mShared = context.getSharedPreferences(SharedConfig.Filter, 0);
            String area_last = mShared.getString(SharedConfig.Area_Cache_NOW, "");
            String area_now = XSetting.xget_string(context, "weather_area");
            if (area_now.equals(area_last)) {
                int time_day_last = mShared.getInt(SharedConfig.Time_day_NOW, -10);
                int time_day_now = XTime.gettime_partly(3);
                if (time_day_last == time_day_now) {
                    int time_hour_last = mShared.getInt(SharedConfig.Time_hour_NOW, -10);
                    int time_now = XTime.gettime_partly(4);
                    if (time_now != time_hour_last) {
                        MyNowWeather mMyWeather = null;
                        try {
                            weather = getWeather_NOW(area_now, context);

                            mMyWeather = new MyNowWeather(weather);
                        } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                        }
                        if (mMyWeather == null) {
                            throw new JSONException("");
                        }
                        mShared.edit().putInt(SharedConfig.Time_hour_NOW, time_now).commit();
                        XLog.xLog("同一天内不同时间点");
                        return mMyWeather;
                    } else {
                        XLog.xLog("同一天内相同时间点");
                        return getWeather_huancun_NOW(context);
                    }
                } else {
                    XLog.xLog("不同天");
                    MyNowWeather mMyWeather = null;
                    try {
                        weather = getWeather_NOW(area_now, context);
                        mMyWeather = new MyNowWeather(weather);
                    } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                    }
                    if (mMyWeather == null) {
                        throw new JSONException("");
                    }
                    int time_now = XTime.gettime_partly(4);
                    mShared.edit().putInt(SharedConfig.Time_hour_NOW, time_now).commit();
                    mShared.edit().putInt(SharedConfig.Time_day_NOW, time_day_now).commit();
                    XLog.xLog("同一天内不同时间点");
                    return mMyWeather;
                }

            } else {
                MyNowWeather mMyWeather = null;
                try {
                    weather = getWeather_NOW(area_now, context);
                    mMyWeather = new MyNowWeather(weather);
                } catch (cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR internet_error) {
                }
                if (mMyWeather == null) {
                    throw new JSONException("");
                }
                int time_now = XTime.gettime_partly(4);
                mShared.edit().putInt(SharedConfig.Time_hour_NOW, time_now).commit();
                int time_day_now = XTime.gettime_partly(3);
                mShared.edit().putInt(SharedConfig.Time_day_NOW, time_day_now).commit();
                mShared.edit().putString(SharedConfig.Area_Cache_NOW, area_now).commit();
                return mMyWeather;
            }

        }

        private static JSONObject getWeather_NOW(String id, Context context)
                throws cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR {
            XLog.xLog("即将获取的城市：" + id);
            String value_total = null;
            String api = "http://weather.kenai.cc/rest/1/weather/now?city=" + id;
            String cache = context.getSharedPreferences(SharedConfig.Filter, 0).getString(SharedConfig.CacheMD5_NOW, null);
            if (cache != null && cache.length() > 1) {
                XLog.xLog("cache", cache);
                api += "&cache=" + cache;
                XLog.xLog("cache:" + cache);
            }
            HttpClient hc = new DefaultHttpClient();
            hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);//连接时间20s
            hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
            HttpGet get = new HttpGet(api);
            try {
                HttpResponse response = hc.execute(get);
                int state = response.getStatusLine().getStatusCode();
                XLog.xLog("state:" + state);
                if (state == 200 || state == 201) {
                    value_total = EntityUtils.toString(response.getEntity(), "utf-8");
                    XLog.xLog("http re:" + value_total);
                    try {
                        EncryptUtil eu = new EncryptUtil();
                        String real;
                        try {
                            real = eu.md5Digest(value_total);
                            context.getSharedPreferences(SharedConfig.Filter, 0).edit().putString(SharedConfig.CacheMD5_NOW, real).commit();
                        } catch (UnsupportedEncodingException e) {

                        }
                    } catch (NoSuchAlgorithmException e) {

                    }

//                value_total=new String(value_total.getBytes(),"utf-8");
//                value_total= URLDecoder.decode(value_total,"utf-8");

                } else if (state == 202 || state == 203) {
                    return getWeather_huancun_NOW(context).weather;
                }
            } catch (Exception e) {
                throw new cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR();
            } finally {
                hc.getConnectionManager().shutdown();
            }
            JSONObject json;
            try {
                json = new JSONObject(value_total);
                json = json.getJSONObject("weatherinfo");
                saveWeather_NOW(context.getSharedPreferences(SharedConfig.Filter, 0), json);
            } catch (Exception e) {
                throw new cc.kenai.weather.utils.WeatherUtilsBykenai.INTERNET_ERROR();
            }

            return json;
        }

        private static void saveWeather_NOW(SharedPreferences mShared, JSONObject weather) {
            mShared.edit().putString(SharedConfig.Weather_cache_NOW, weather.toString()).commit();
        }

        private static JSONObject getWeather_json_NOW(Context context) throws JSONException {
            String s = context.getSharedPreferences(SharedConfig.Filter, 0).getString(SharedConfig.Weather_cache_NOW, "");
            return new JSONObject(s);
        }
    }

}

