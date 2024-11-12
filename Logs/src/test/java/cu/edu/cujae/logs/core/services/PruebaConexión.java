package cu.edu.cujae.logs.core.services;

import com.google.gson.Gson;
import cu.edu.cujae.logs.core.dto.record.CoordenadasIp;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PruebaConexión {

    @Test
    public void test() throws IOException, InterruptedException {
        String ip = "152.207.224.73";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8087/api/v1/geoIp/?ip="+ip)).build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        CoordenadasIp cor = new Gson().fromJson(response.body(), CoordenadasIp.class);
        System.out.println(cor);
    }

    @Test
    public void testService() throws IOException, InterruptedException {
        String ip = "152.207.224.73";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8087/api/v1/geoIp/?ip="+ip)).build();
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        CoordenadasIp cor = new Gson().fromJson(response.body(), CoordenadasIp.class);
        System.out.println(cor);
    }
}