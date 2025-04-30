import com.google.gson.Gson;

import javax.net.ssl.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class ConsultaPelicula {

    public Pelicula buscaPelicula(int numeroDePelicula) {
        String url = "https://swapi.dev/api/films/" + numeroDePelicula + "/";
        URI direccion = URI.create(url);

        // Creamos un TrustManager que no valida nada
        TrustManager[] trustAll = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
        };

        try {
            // Iniciamos un SSLContext “inseguro” que confía en todo
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAll, new SecureRandom());

            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(direccion)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Error HTTP al consultar película: código "
                                + response.statusCode()
                );
            }

            return new Gson().fromJson(response.body(), Pelicula.class);

        } catch (Exception e) {
            throw new RuntimeException(
                    "No encontré esa película. Detalle: " + e.getMessage()
            );
        }
    }
}
