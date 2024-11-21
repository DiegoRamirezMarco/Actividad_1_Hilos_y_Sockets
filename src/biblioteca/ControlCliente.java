package biblioteca;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//ControlCliente se encarga de gestionar la logica de las opciones que elige el cliente
//implementa runnable y la interfaz IBiblioteca con sus funciones
public  class ControlCliente implements Runnable, IBiblioteca {
    private Socket cliente;
    private final List<Libro> listaLibros;

    public ControlCliente(Socket cliente, List<Libro> listaLibros) {
        this.cliente = cliente;
        this.listaLibros = listaLibros;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream())) {
        //Lee los datos proporcionados por teclado desde cliente y los gestiona mediante las funciones de cada opcion
        //despues devuelve los datos pertinentes para que el cliente pueda visualizar los resultados.
            boolean continuar = true;
            while (continuar) {
                String opcion = (String)in.readObject();

                switch (opcion) {
                    case "0":
                        //Opcion de oculta para probar
                        out.writeObject(buscarTodos());
                        break;
                    case "1":
                        // Buscar por ISBN
                        String isbn = (String) in.readObject();
                        Libro libroISBN = buscarPorISBN(isbn);
                        if(libroISBN != null){
                            out.writeObject(libroISBN);
                        }else {
                            out.writeObject("No se encontro el libro con el ISBN "+isbn);
                        }
                        break;
                    case "2":
                        //Buscar por Titulo
                        String titulo = (String)in.readObject();
                        Libro libroTitulo = buscarPorTitulo(titulo);
                        if (libroTitulo != null){
                            out.writeObject(libroTitulo);
                        }else {
                            out.writeObject("No se encontro el libro con el titulo "+ titulo);
                        }
                        break;
                    case "3":
                        // Buscar libros por Autor
                        String autor = (String)in.readObject();
                        List<Libro> listaAutor = buscarPorAutor(autor);
                        if (!listaAutor.isEmpty()){
                            out.writeObject(listaAutor);
                        }else {
                            out.writeObject("No se encontro el libro con el autor "+ autor);
                        }
                        break;
                    case "4":
                        // Crear el libro
                        Libro nuevoLibro = (Libro)in.readObject();
                        String mensajeLibroAñadido = añadirLibro(nuevoLibro);
                        out.writeObject(mensajeLibroAñadido);
                        break;
                    case "5":
                        //Salir
                        continuar = false;
                        out.writeObject("Servidor desconectado");
                        break;
                    default:
                        //Opcion no valida
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error con el cliente: " + e.getMessage());
        }
    }

    @Override
    public Libro buscarPorISBN(String isbn) {
        for(Libro lib : listaLibros){
            if (isbn.equals(lib.getIsbn())){
                return lib;
            }
        }
        return null;
    }

@Override
    public Libro buscarPorTitulo(String titulo) {
        for(Libro lib : listaLibros){
            if (titulo.equals(lib.getTitulo())){
                return lib;
            }
        }
        return null;
    }

    @Override
    public List<Libro> buscarPorAutor(String autor) {
        List<Libro> listaLibrosPorAutor = new ArrayList<>();
        for (Libro lib : listaLibros){
            if (autor.equals(lib.getAutor())){
                listaLibrosPorAutor.add(lib);
            }
        }
        return listaLibrosPorAutor;
    }

    @Override
    public List<Libro> buscarTodos() {
        return listaLibros;
    }

    @Override
    public String añadirLibro(Libro libro) {
        List<Libro> listaLibrosComprobacion = new ArrayList<>();
        for (Libro lib : listaLibros){
            if (lib.getIsbn().equals(libro.getIsbn())){
                listaLibrosComprobacion.add(lib);
            }
        }
        if (listaLibrosComprobacion.isEmpty()){
            listaLibros.add(libro);
            return libro.getTitulo()+" se ha añadido a la lista.";
        }else{
            return "No se pudo añadir el libro, el ISBN "+libro.getIsbn() + " ya existe.";
        }
    }
}
