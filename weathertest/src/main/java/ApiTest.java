import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiTest {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        client.setConnectTimeout(3, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url("http://localhost:8080/rest?city=010400")
                .build();

        Response response = client.newCall(request).execute();

        System.out.print(response.body().string());

        request = new Request.Builder()
                .url("http://localhost:8080/rest?city=010602")
                .build();

        response = client.newCall(request).execute();

        System.out.print(response.body().string());
    }
}
