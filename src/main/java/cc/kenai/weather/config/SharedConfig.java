package cc.kenai.weather.config;

/**
 * Created by kenai on 13-11-12.
 */
public interface SharedConfig {
    String Filter = "weather_http";
    String Time_update = "updatetime";

    String CacheMD5 = "http_cache_md5";
    String Area_Cache = "weather_area_last";
    String Weather_cache = "weather_cache";
    String Time_day = "weather_time_day";
    String Time_hour = "weather_time_hour";
    String Time_fchh = "weather_time_fchh";

    String CacheMD5_NOW = "http_cache_md5_now";
    String Area_Cache_NOW = "weather_area_last_now";
    String Weather_cache_NOW = "weather_cache_now";
    String Time_day_NOW = "weather_time_day_now";
    String Time_hour_NOW = "weather_time_hour_now";

    String CacheMD5_AQI = "http_cache_md5_aqi";
    String Area_Cache_AQI = "weather_area_last_aqi";
    String Weather_cache_AQI = "weather_cache_aqi";
    String Time_day_AQI = "weather_time_day_aqi";
    String Time_hour_AQI = "weather_time_hour_aqi";
}
