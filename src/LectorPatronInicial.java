import java.util.List;
import java.util.ArrayList;

/**
 * Se encarga de leer y validar el string con el patron inicial del
 * tablero (numero de organismos y sus coordenadas), y de convertirlo
 * en una lista de enteros que Tablero pueda usar.
 */
public class LectorPatronInicial {

    private String mensajeError;

    // metodo que devuelva las coordenadas ya parseadas, cuando validar() dio true recibiendo numero de filas y columnas del tablero
    public List<Integer> procesarDatosTablero(String datosIniciales, int numeroFilas, int numeroColumnas){
        List<Integer> datosProcesados = new ArrayList<>();
        if (datosIniciales == null || datosIniciales.isEmpty()) {
            mensajeError = "El texto recibido es nulo o vacio";
        } 
        else{
            // Con -1 como segundo parametro, split conserva los vacios del final (ej. "1,2,3,")
            String[] datosTablero = datosIniciales.split(",", -1);
            if(validar(datosTablero, numeroFilas, numeroColumnas)){
                for (int i = 1;i < datosTablero.length; i++){
                    datosProcesados.add(Integer.parseInt(datosTablero[i]));
                }
            }
        }
        return datosProcesados;
    }

    //Valida el string completo (usa split, recorre pares, etc.)
    private boolean validar(String[] datosTablero, int numeroFilas, int numeroColumnas) {
        boolean esValido = true;
        //Validar que la primera posicion sea un entero
        if (!esEnteroNoNegativo(datosTablero[0])) {
            mensajeError = "El texto recibido no tiene numero como primer parametro";
            esValido = false;
        }
        else{
            int numeroOrganismos = Integer.parseInt(datosTablero[0]);
            int numeroElementos = datosTablero.length;
            //Validar que la cadena tenga al menos 3 elementos (numero de organismos, fila y columna)
            if (numeroElementos < 3){
                mensajeError = "El texto recibido tiene menos de 3 elementos";
                esValido = false;
            }
            //Validar que el numero de organismos no sea 0
            else if (numeroOrganismos == 0){
                mensajeError = "El numero de organismos es 0, no se puede iniciar el juego";
                esValido = false;
            }
            //Validar que las coordenadas vengan en pares (fila y columna)
            else if ((numeroElementos - 1) % 2 != 0){
                mensajeError = "El texto recibido tiene un numero impar de coordenadas";
                esValido = false;
            }
            //Validar que el numero de organismos declarado coincida con los pares recibidos
            else if (numeroOrganismos != (numeroElementos - 1) / 2){
                mensajeError = "El numero de organismos no coincide con la cantidad de coordenadas recibidas";
                esValido = false;
            }
            //Validar que los organismos no superen el 50% de la capacidad del tablero
            else if ((numeroElementos - 1) / 2 > (numeroFilas * numeroColumnas) / 2){
                mensajeError = "El texto recibido tiene más coordenadas que el 50% de la capacidad del tablero";
                esValido = false;
            }
            else{
                // Se usa para detectar si dos organismos quedan en la misma posicion
                boolean[][] posicionesUsadas = new boolean[numeroFilas][numeroColumnas];

                // Se detiene en el primer error para que mensajeError
                // describa ese error y no uno posterior
                for (int i = 1; esValido && i < numeroElementos; i += 2) {
                    String par1 = datosTablero[i];
                    String par2 = datosTablero[i+1];
                    //Validar que los datos ingresados sean numeros no negativos
                    if (!esEnteroNoNegativo(par1) || !esEnteroNoNegativo(par2)){
                        mensajeError = "El texto recibido no cuenta con unicamente numeros positivos";
                        esValido = false;
                    } else{
                        int fila = Integer.parseInt(datosTablero[i]);
                        int columna = Integer.parseInt(datosTablero[i + 1]);
                        //Validar que los datos ingresados esten dentro del rango del tablero
                        if (fila < 0 || fila >= numeroFilas || columna < 0 || columna >= numeroColumnas) {
                            mensajeError = "Existen posiciones fuera de rango en el texto en la posicion de las coordenadas: "+i;
                            esValido = false;
                        }
                        //Validar que la posicion no se haya usado ya por otro organismo
                        else if(posicionesUsadas[fila][columna]){
                            mensajeError = "La coordenada (" + fila + "," + columna + ") está repetida";
                            esValido = false;
                        }
                        else{
                            posicionesUsadas[fila][columna] = true;
                        }
                    }
                }
            }
        }
        return esValido;
    }

    // Valida que el string que se le pase como parametro sea un numero entero
    // no negativo (acepta el 0). Se limita a 9 digitos para que siempre quepa
    // en un int y Integer.parseInt no lance NumberFormatException
    private boolean esEnteroNoNegativo(String s) {
        boolean esValido = true;
        if (s == null || s.isEmpty() || s.length() > 9) {
            esValido = false;
        } else {
            for (int i = 0; esValido && i < s.length(); i++){
                if(!Character.isDigit(s.charAt(i))){
                    esValido = false;
                }
            }
        }
        return esValido;
    }

    public String getMensajeError() {
        return mensajeError;
    }


}