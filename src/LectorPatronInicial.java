import java.util.List;
import java.util.ArrayList;

/**
 * Se encarga de leer y validar el string con el patron inicial del
 * tablero (numero de organismos y sus coordenadas), y de convertirlo
 * en una lista de enteros que Tablero pueda usar.
 */
public class LectorPatronInicial {

    private String datosIniciales;
    private String [] datosTablero;
    private String mensajeError;


    public LectorPatronInicial(String datosIniciales) {
        this.datosIniciales = datosIniciales;
        this.datosTablero = datosIniciales.split(",");
        mensajeError = "";
    }

    // metodo que devuelva las coordenadas ya parseadas, cuando esValido() dio true recibiendo numero de filas y columnas del tablero
    public List<Integer> procesarDatosTablero(int numeroFilas, int numeroColumnas){
        if(validar(numeroFilas, numeroColumnas)){
            List<Integer> datosProcesados = new ArrayList<>();

            for (int i = 1;i < datosTablero.length; i++){
                datosProcesados.add(Integer.parseInt(datosTablero[i]));
            }
            return datosProcesados;
        }
        return null;
    }

    //Valida el string completo (usa split, recorre pares, etc.)
    private boolean validar(int numeroFilas, int numeroColumnas) {
        //Validar que la cadena no sea nula o vacia
        if (datosIniciales == null || datosIniciales.isEmpty()){
            mensajeError = "El string recibido era nulo o vacio";
            return false;
        }

        //Validar que la primera posicion sea un entero
        if (!esEnteroPositivo(datosTablero[0])) {
            mensajeError = "El string recibido tenia un string como primer parametro";
            return false;
        }

        int numeroOrganismos = Integer.parseInt(datosTablero[0]);
        int numeroElementos = datosTablero.length;
        //Validar que el numero de organismos no sea 0
        if (numeroOrganismos == 0){
            mensajeError = "El numero de organismos es 0, no se puede iniciar el juego";
            return false;
        }
        //Validar que las coordenadas vengan en pares (fila y columna)
        if ((numeroElementos - 1) % 2 != 0){
            mensajeError = "El string recibido tiene un numero impar de pares de coordenadas";
            return false;
        }
        //Validar que el numero de organismos declarado coincida con los pares recibidos
        if (numeroOrganismos != (numeroElementos - 1) / 2){
            mensajeError = "El numero de organismos no coincide con la cantidad de pares de coordenadas recibidas";
            return false;
        }
        //Validar que la cadena tenga al menos 3 elementos (numero de organismos, fila y columna)
        if (numeroElementos < 3){
            mensajeError = "El string recibido tiene menos de 3 elementos";
            return false;
        }
        //Validar que los organismos no superen el 50% de la capacidad del tablero
        if ((numeroElementos - 1) / 2 > (numeroFilas * numeroColumnas) / 2){
            mensajeError = "El string recibido tiene mas pares de coordenadas que el 50% de la capacidad del tablero";
            return false;
        }
        // Se usa para detectar si dos organismos quedan en la misma posicion
        boolean[][] posicionesUsadas = new boolean[numeroFilas][numeroColumnas];
        for (int i = 1; i < numeroElementos; i += 2) {
            String par1 = datosTablero[i];
            String par2 = datosTablero[i+1];
            //Validar que los datos ingresados sean numeros positivos
            if (!esEnteroPositivo(par1) || !esEnteroPositivo(par2)){
                mensajeError = "El string recibido tiene strings como parametros en lugar de solo enteros";
                return false;
            }
            int fila = Integer.parseInt(datosTablero[i]);
            int columna = Integer.parseInt(datosTablero[i + 1]);
            //Validar que los datos ingresados esten dentro del rango del tablero
            if (fila < 0 || fila >= numeroFilas || columna < 0 || columna >= numeroColumnas) {
                mensajeError = "Existen posiciones fuera de rango en el string en la posicion de los pares: "+i;
                return false;
            }
            //Validar que la posicion no se haya usado ya por otro organismo
            if(posicionesUsadas[fila][columna]){
                mensajeError = "La posición (" + fila + "," + columna + ") está repetida";
                return false;
            }
            posicionesUsadas[fila][columna] = true;
        }
        return true;
    }

    // Valida que el string que se le pase como parametro sea un numero entero positivo
    private boolean esEnteroPositivo(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public String getMensajeError() {
        return mensajeError;
    }


}