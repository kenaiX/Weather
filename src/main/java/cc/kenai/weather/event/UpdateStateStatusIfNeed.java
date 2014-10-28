package cc.kenai.weather.event;

import cc.kenai.weather.pojos.WeatherPojo;
import hugo.weaving.DebugLog;

public class UpdateStateStatusIfNeed {
    public WeatherPojo weatherPojo;

    @DebugLog
    public UpdateStateStatusIfNeed(WeatherPojo weatherPojo) {
        this.weatherPojo = weatherPojo;
    }
}
