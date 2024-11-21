package biblioteca;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*La parte de servidor que conectara todas las partes de la biblioteca
* el servidor conectara varios clientes
*/
public class Servidor{
    private static final int PORT = 12345;
    private static final List<Libro> listaLibros = new ArrayList<>();

    public static void main(String[] args) {
/*Creamos la lista inicializando la funcion donde estan creados*/
        inicializarLibros();

        //Inicializamos la conexion y creamos un bucle que aceptara clientes indefinidos a los cuales les pasaremos
        //el shoket y la lista.
        try {
            ServerSocket servidor = new ServerSocket(PORT);

            while (true){
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado");
                new Thread(new ControlCliente(cliente, listaLibros)).start();
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor");
        }
    }
    private static void inicializarLibros() {
        listaLibros.add(new Libro("11111", "Steel Ball Run", "Hirohiko Araki", 16.99));
        listaLibros.add(new Libro("22222", "Diamond Is Unbreakable", "Hirohiko Araki", 15.99));
        listaLibros.add(new Libro("33333", "Berserk", "Kentaro Miura", 18.99));
        listaLibros.add(new Libro("44444", "Superlópez", "Juan López Fernández", 20.99));
        listaLibros.add(new Libro("55555", "Skulduggery Pleasant", "Derek Landy", 20.99));
    }

}
