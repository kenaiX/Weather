import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kenai on 14/10/27.
 */
public class WeatherPojo {
    public En en;
    public Fc fc;
    public Now now;
    public Zs zs;


    @Override
    public String toString() {
        return "WeatherPojo{" + "\n" +
                "    en=" + en + "\n" +
                "    fc=" + fc + "\n" +
                "    now=" + now + "\n" +
                "    zs=" + zs + "\n" +
                '}';
    }

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(3, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url("http://kweather.duapp.com/rest?city=010400")
                .build();

        Response response = client.newCall(request).execute();

        String result = response.body().string().replaceAll("\"\\{", "{").replaceAll("\\}\"", "}");


        Gson gson = new Gson();

        WeatherPojo weatherPojo = gson.fromJson(result, WeatherPojo.class);

        System.out.print(weatherPojo);
    }


    public class En {
        public String aqi, ss, ql, x;

        @Override
        public String toString() {
            return "En{" +
                    "aqi='" + aqi + '\'' +
                    ", ss='" + ss + '\'' +
                    ", ql='" + ql + '\'' +
                    ", x='" + x + '\'' +
                    '}';
        }
    }

    public class Fc {
        public String x;
        public Fn f1, f2, f3, f4, f5, f6, f7;

        @Override
        public String toString() {
            return "Fc{" +
                    "x='" + x + '\'' +
                    ", f1=" + f1 +
                    ", f2=" + f2 +
                    ", f3=" + f3 +
                    ", f4=" + f4 +
                    ", f5=" + f5 +
                    ", f6=" + f6 +
                    ", f7=" + f7 +
                    '}';
        }

        public class Fn {
            public String dt, h, l, w_d, fx_d, fl_d, w_n, fx_n, fl_n;

            @Override
            public String toString() {
                return "Fcn{" +
                        "dt='" + dt + '\'' +
                        ", h='" + h + '\'' +
                        ", l='" + l + '\'' +
                        ", w_d='" + w_d + '\'' +
                        ", fx_d='" + fx_d + '\'' +
                        ", fl_d='" + fl_d + '\'' +
                        ", w_n='" + w_n + '\'' +
                        ", fx_n='" + fx_n + '\'' +
                        ", fl_n='" + fl_n + '\'' +
                        '}';
            }
        }
    }

    public class Now {
        public String ct, ut, wd, fl, sd, fx, x;

        @Override
        public String toString() {
            return "Now{" +
                    "ct='" + ct + '\'' +
                    ", ut='" + ut + '\'' +
                    ", wd='" + wd + '\'' +
                    ", fl='" + fl + '\'' +
                    ", sd='" + sd + '\'' +
                    ", fx='" + fx + '\'' +
                    ", x='" + x + '\'' +
                    '}';
        }
    }

    public class Zs {
        public String x;

        @Override
        public String toString() {
            return "Zs{" +
                    "x='" + x + '\'' +
                    '}';
        }
    }
}



