import java.io.IOException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        // Creamos un Scanner para leer el input del usuario desde la consola
        Scanner lectura = new Scanner(System.in);
        // Instanciamos nuestro servicio de consulta de películas
        ConsultaPelicula consulta = new ConsultaPelicula();

        System.out.println("Escriba el numero de la pelicula de Star Wars que quiere consultar:");
        try {
            // Leemos la línea ingresada y la convertimos a Integer
            var numeroDePelicula = Integer.valueOf(lectura.nextLine());

            // Llamamos al método que consulta la API y devuelve un objeto Pelicula
            Pelicula pelicula = consulta.buscaPelicula(numeroDePelicula);

            // Imprimimos en consola el toString() generado por el record Pelicula
            System.out.println(pelicula);

            // Preparamos el generador de archivos JSON
            GeneradorDeArchivo generador = new GeneradorDeArchivo();
            // Guardamos la película en un archivo .json
            generador.guardarJson(pelicula);

        } catch (NumberFormatException e) {
            // Si el usuario no ingresó un número válido
            System.out.println("Número no encontrado: " + e.getMessage());
        } catch (RuntimeException | IOException e) {
            // Capturamos errores en la consulta HTTP o en la escritura del archivo
            System.out.println(e.getMessage());
            System.out.println("Finalizando la aplicación.");
        } finally {
            // Cerramos el Scanner para liberar la entrada estándar
            lectura.close();
        }
    }
}
