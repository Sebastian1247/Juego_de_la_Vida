/**
 * Punto de entrada del programa. Pide por consola el tamano del tablero,
 * el numero de generaciones a simular y el patron inicial de organismos
 * vivos, y le pasa esos datos al Tablero para que corra el juego.
 */
public class App {

    public static void main(String[] args) throws Exception {
        int filas, columnas, generaciones;

        // El numero de filas y columnas se limita entre 2 y 20 segun lo
        // pedido en el enunciado
        System.out.print("Ingrese el numero de filas del tablero: ");
        filas = Keyboard.readInt();
        while (filas < 2 || filas > 20) {
            System.out.print("Error, ingresa un numero correcto de filas: ");
            filas = Keyboard.readInt();
        }

        System.out.print("Ingrese el numero de columnas del tablero: ");
        columnas = Keyboard.readInt();
        while (columnas < 2 || columnas > 20) {
            System.out.print("Error, ingresa un numero correcto de columnas: ");
            columnas = Keyboard.readInt();
        }

        // El numero de generaciones se limita entre 1 y 50 segun lo
        // pedido en el enunciado
        System.out.print("Ingrese el numero de generaciones del juego: ");
        generaciones = Keyboard.readInt();
        while (generaciones < 1 || generaciones > 50){
            System.out.print("Error, ingresa un numero correcto de generaciones: ");
            generaciones = Keyboard.readInt();
        }
        System.out.println();
        
        Tablero tablero = new Tablero(filas, columnas);

        System.out.println("Ingrese el texto para generar el tablero y sus organismos vivos: ");
        System.out.println("Formato: cantidadOrganismos,fila1,columna1,fila2,columna2,... Ejemplo: 3,1,2,2,2,3,2");
        String tableroString = Keyboard.readString();
        System.out.println();
        while(!tablero.iniciar(tableroString, generaciones)){
            System.out.println("Cadena invalida, ingresa otra cadena: ");
            tableroString = Keyboard.readString();
        }
        System.out.println();
        System.out.println("El programa ha terminado, gracias por jugar!");
    }
}
