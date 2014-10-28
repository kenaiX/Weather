package cc.kenai.weather.pojos;

import com.google.gson.Gson;

public class WeatherPojo {

    public long updatetime;
    public String citycode;

    public En en = new En();
    public Fc fc = new Fc();
    public Now now = new Now();

    public static WeatherPojo fromJson(String s) {
        Gson gson = new Gson();
        WeatherPojo weatherPojo = gson.fromJson(s, WeatherPojo.class);
        return weatherPojo;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public WeatherPojo newClone() {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), WeatherPojo.class);
    }

    public class En {
        public String aqi, ss, ql, x;

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    public class Fc {
        public String x;
        public Fn f1 = new Fn(), f2 = new Fn(), f3 = new Fn(), f4 = new Fn(), f5 = new Fn(), f6 = new Fn(), f7 = new Fn();

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }

        public class Fn {
            public String dt, h, l, w_d, fx_d, fl_d, w_n, fx_n, fl_n;

            @Override
            public String toString() {
                Gson gson = new Gson();
                return gson.toJson(this);
            }
        }
    }


    /**
     * 使用ct标记是否为空
     */
    public class Now {
        public String ct, ut, wd, fl, sd, fx, x;

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    public class Zs {
        public String x;

        @Override
        public String toString() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

}
