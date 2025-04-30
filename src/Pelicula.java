/**
 * Registro inmutable que mapea exactamente la estructura JSON
 * que devuelve SWAPI para una película.
 */
public record Pelicula(
        String title,           // Título de la película
        int episode_id,         // Número de episodio (I, II, III...)
        String opening_crawl,   // Texto de apertura (“crawl”)
        String director,        // Director de la película
        String producer,        // Productores de la película
        String release_date     // Fecha de estreno (YYYY-MM-DD)
) {
    // El método toString() es generado automáticamente y
    // muestra todos los campos de forma legible.
}
