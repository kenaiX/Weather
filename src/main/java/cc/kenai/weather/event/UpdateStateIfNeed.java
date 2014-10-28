package cc.kenai.weather.event;

import cc.kenai.weather.pojos.WeatherPojo;
import hugo.weaving.DebugLog;

public class UpdateStateIfNeed {
    public WeatherPojo weatherPojo;

    @DebugLog
    public UpdateStateIfNeed(WeatherPojo weatherPojo) {
        this.weatherPojo = weatherPojo;
    }
}
