package cc.kenai.weather;

public interface MainBroadcastEvent {
    String BROADCAST_STATEBAR_SHOWINFO = "cc.kenai.weather.utils.WeatherStatebar.statebar_showinfo";
    String BROADCAST_STATEBAR_CANCEL = "cc.kenai.weather.utils.WeatherStatebar.statebar_cancel";
    String BROADCAST_UPDATE = "cc.kenai.weather.MainXService.update";
}
