import com.google.gson.Gson;                     // Librería para convertir JSON ↔ objetos Java

import javax.net.ssl.*;                           // Clases para manejar SSL/TLS
import java.net.URI;                              // Para construir URLs
import java.net.http.HttpClient;                  // Cliente HTTP incluido en Java 11+
import java.net.http.HttpRequest;                 // Representa una petición HTTP
import java.net.http.HttpResponse;                // Representa la respuesta HTTP
import java.security.SecureRandom;                // Para inicializar el contexto SSL
import java.security.cert.X509Certificate;        // Representa certificados X.509

public class ConsultaPelicula {

    /**
     * Consulta la API de Star Wars (SWAPI) para obtener
     * la información de una película según su número.
     *
     * @param numeroDePelicula Número de la película en SWAPI (1–7, etc.)
     * @return Un objeto Pelicula mapeado desde el JSON de la respuesta
     * @throws RuntimeException Si hay algún fallo en la conexión o el parseo
     */
    public Pelicula buscaPelicula(int numeroDePelicula) {
        // 1) Construimos la URL completa con la barra final para evitar redirecciones.
        String url = "https://swapi.dev/api/films/" + numeroDePelicula + "/";
        // 2) Convertimos el String en un objeto URI para usarlo en la petición.
        URI direccion = URI.create(url);

        // 3) Creamos un TrustManager “inseguro” que no valida certificados.
        //    Esto permite evitar errores de PKIX en entornos de desarrollo.
        TrustManager[] trustAll = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        // No hace nada: confía en cualquier certificado de cliente
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        // No hace nada: confía en cualquier certificado de servidor
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        // Retorna un array vacío, ya que acepta todos los emisores
                        return new X509Certificate[0];
                    }
                }
        };

        try {
            // 4) Inicializamos un contexto SSL/TLS usando nuestro TrustManager “inseguro”
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(
                    null,          // KeyManagers (nulos porque no necesitamos certificados cliente)
                    trustAll,      // Nuestro TrustManager que confía en todo
                    new SecureRandom() // Fuente de entropía para TLS
            );

            // 5) Construimos el HttpClient configurado para usar nuestro SSLContext y seguir redirecciones
            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            // 6) Preparamos la petición HTTP GET a la URI
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(direccion)
                    .GET()    // Método GET
                    .build();

            // 7) Enviamos la petición de forma sincrónica y obtenemos la respuesta como String
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // 8) Validamos el código de estado HTTP: debe ser 200 OK
            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Error HTTP al consultar película: código "
                                + response.statusCode()
                );
            }

            // 9) Convertimos el cuerpo JSON en una instancia de Pelicula
            //    Gson ignorará cualquier campo adicional que nuestro record no declare.
            return new Gson().fromJson(response.body(), Pelicula.class);

        } catch (Exception e) {
            // 10) Capturamos cualquier excepción (I/O, SSL, JSON mal formado…) y la envolvemos
            throw new RuntimeException(
                    "No encontré esa película. Detalle: " + e.getMessage()
            );
        }
    }
}
