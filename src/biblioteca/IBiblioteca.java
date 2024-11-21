package biblioteca;

import java.util.List;

public interface IBiblioteca {
    Libro buscarPorISBN(String isbn);
    Libro buscarPorTitulo(String titulo);
    List<Libro> buscarPorAutor(String autor);
    List<Libro> buscarTodos();
    String añadirLibro(Libro libro);
}
