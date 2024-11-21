package biblioteca;

import java.io.*;
import java.net.*;
import java.util.Scanner;

//Cliente que interactua con el servidor de la biblioteca.
public class Cliente {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        /*Inicalizo las salidas y el soket para el cliente*/
        try (Socket servidor = new Socket(HOST, PORT);
             ObjectOutputStream salida = new ObjectOutputStream(servidor.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(servidor.getInputStream())) {

            Scanner scanner = new Scanner(System.in);
            boolean continuar = true;

            while (continuar) {
                System.out.println("""
                        ╔══════════════════════════════════╗
                        ║              MENU                ║
                        ╠══════════════════════════════════╣
                        ║  1. Consultar libro por ISBN     ║
                        ║──────────────────────────────────║
                        ║  2. Consultar libro por título   ║
                        ║──────────────────────────────────║
                        ║  3. Consultar libros por autor   ║
                        ║──────────────────────────────────║
                        ║  4. Añadir libro                 ║
                        ║──────────────────────────────────║
                        ║  5. Salir                        ║
                        ║──────────────────────────────────║
                        ╚══════════════════════════════════╝                  
                        """);
                String opcion = scanner.nextLine();

                salida.writeObject(opcion);

                switch (opcion) {
                    case "0":
                        //esta opcion no sale en el menu por que es para comprobar yo si se añadian los libros
                        System.out.println(entrada.readObject());
                        break;
                    case "1":
                        //Opciones para buscar o añadir libros envian los datos que escribimos por pantalla y recibe el
                        //resultado de la logica en ControlCliente
                        System.out.print("Introduce el ISBN: ");
                        String isbn = scanner.nextLine();
                        salida.writeObject(isbn);
                        System.out.println(entrada.readObject());
                        break;
                    case "2":
                        System.out.print("Introduce el título: ");
                        String titulo = scanner.nextLine();
                        salida.writeObject(titulo);
                        System.out.println(entrada.readObject());
                        break;

                    case "3":
                        System.out.print("Introduce el autor: ");
                        String autor = scanner.nextLine();
                        salida.writeObject(autor);
                        System.out.println(entrada.readObject());
                        break;

                    case "4":
                        System.out.print("Introduce el ISBN: ");
                        String nuevoISBN = scanner.nextLine();
                        System.out.print("Introduce el título: ");
                        String nuevoTitulo = scanner.nextLine();
                        System.out.print("Introduce el autor: ");
                        String nuevoAutor = scanner.nextLine();
                        double nuevoPrecio = 0.0;
                        while (true) {
                            System.out.print("Introduce el precio: ");
                            if (scanner.hasNextInt()) {
                                nuevoPrecio = scanner.nextDouble();
                                break;
                            } else {
                                System.out.println("El valor debe ser un numero decimal");
                                scanner.next();                              }
                        }
                        scanner.nextLine();
                        Libro libroCreado = new Libro(nuevoISBN, nuevoTitulo, nuevoAutor, nuevoPrecio);
                        salida.writeObject(libroCreado);
                        //---------------------------------------
                        System.out.println(entrada.readObject());
                        break;

                    case "5":
                        continuar = false;
                        System.out.println(entrada.readObject());
                        break;

                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
