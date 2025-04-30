import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Se encarga de serializar un objeto Pelicula a JSON
 * y escribirlo en un archivo con el título de la película.
 */
public class GeneradorDeArchivo {

    /**
     * Crea un JSON "bonito" (pretty printing) y lo guarda en disco.
     * @param pelicula objeto con datos a serializar
     * @throws IOException si hay error al crear o escribir el archivo
     */
    public void guardarJson(Pelicula pelicula) throws IOException {
        // Configuramos Gson para que imprima el JSON de forma legible
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Abrimos un FileWriter: nombre del archivo = título de la película + ".json"
        FileWriter escritura = new FileWriter(pelicula.title() + ".json");

        // Escribimos el JSON convertido
        escritura.write(gson.toJson(pelicula));

        // Cerramos el recurso para asegurar flush y liberar memoria
        escritura.close();
    }
}
