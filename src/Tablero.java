import java.util.ArrayList;
import java.util.List;

/**
 * Tablero del juego de la vida: guarda el estado de las celdas, avanza
 * las generaciones y lleva el historial de movimientos.
 */
public class Tablero {

    private int numeroFilas;
    private int numeroColumnas;
    private EstadoOrganismo [][] organismos;
    private HistorialMovimientos historialMovimientos;
    private int generacionActual;
    private boolean hayOrganismosVivos;
    private boolean huboCambios;

    public Tablero (int numeroFilas, int numeroColumnas){
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;
        organismos = new EstadoOrganismo[numeroFilas][numeroColumnas];
        // Inicializar el tablero con organismos muertos
        for (int i = 0; i < numeroFilas; i++) {
            for (int k = 0; k < numeroColumnas; k++) {
                organismos[i][k] = EstadoOrganismo.MUERTO;
            }
        }
        historialMovimientos = new HistorialMovimientos();
        generacionActual = 1;
        hayOrganismosVivos = false;
        huboCambios = false;
    }

    /**
     * Recibe el patron inicial en formato "n,fila1,col1,...,filaN,colN" y
     * el numero de generaciones a simular. Valida el patron, coloca los
     * organismos vivos y va simulando generaciones hasta el limite
     * indicado, hasta que no queden organismos vivos, o hasta que el
     * tablero se estabilice. Devuelve true si el patron era valido y el
     * juego corrio, o false si el patron era invalido.
     */
     public boolean iniciar(String datosIniciales, int generaciones){

        LectorPatronInicial lectorPatronInicial = new LectorPatronInicial(datosIniciales);

        List<Integer> coordenadas = lectorPatronInicial.procesarDatosTablero(numeroFilas, numeroColumnas);
        if (coordenadas == null){
            System.out.println(lectorPatronInicial.getMensajeError());
            return false;
        }
        crearTableroInicial(coordenadas);

        System.out.println("Generacion numero 1: ");
        System.out.println(this.toString());
        System.out.println("Presione enter para continuar...");
        Keyboard.readLine();
        for (int i = 1;i < generaciones; i++){
            System.out.println("Generacion numero "+(i+1)+":");
            nextGeneracion();
            System.out.println(this.toString());
            if(debeDetenerse()){
                break;
            }
            System.out.println("Presione enter para continuar...");
            Keyboard.readLine();
        }

        historialMovimientos.imprimirMovimientos();
        return true;
    }

     // Revisa las 2 condiciones de paro: que ya no haya organismos vivos, o
    // que el tablero se haya estabilizado entre esta generacion y la anterior
    private boolean debeDetenerse(){
        if (!hayOrganismosVivos){
            System.out.println("Ya no hay organismos vivos, el programa termina...");
            return true;
        }
        if (!huboCambios){
            System.out.println("Las ultimas 2 generaciones se estabilizaron, terminando programa...");
            return true;
        }
        return false;
    }

    // Genera el tablero inicial en base a la lista de enteros de coordenadas
    private void crearTableroInicial(List<Integer> coordenadas){
        for(int i=0; i<coordenadas.size();i+=2){
            int fila = coordenadas.get(i);
            int columna = coordenadas.get(i+1);
            organismos[fila][columna] = EstadoOrganismo.VIVO;
        }
    }

    // Cuenta cuantos de los 8 vecinos de la celda (fila, columna) estan
    // vivos. Los vecinos fuera del tablero no cuentan, el tablero no
    // da la vuelta en los bordes
    private int contarVecinosVivos(int fila, int columna){
        int vecinos = 0;
        //Ciclo que recorre los vecinos alrededor de la celda que queremos
        for (int i = fila - 1; i <= fila + 1; i++){
            for (int k = columna - 1; k <= columna + 1; k++){
                if (!(i == fila && k == columna) && !(i < 0 || i >= numeroFilas) && !(k < 0 || k >= numeroColumnas)){
                    if (organismos[i][k] == EstadoOrganismo.VIVO){
                        vecinos++;
                    }
                }
            }
        }
        return vecinos;
    }

    private void nextGeneracion(){
        int vecinosVivos;
        // Se guarda la generacion nueva en una matriz aparte para que el
        // calculo de cada celda siga usando el estado anterior del
        // tablero, y no valores que ya se hayan actualizado en esta
        // misma generacion
        EstadoOrganismo[][] matrizTemporal = new EstadoOrganismo[numeroFilas][numeroColumnas];
        generacionActual++;
        huboCambios = false;
        hayOrganismosVivos = false;
        for (int i = 0; i < numeroFilas; i++){
            for (int k = 0; k < numeroColumnas; k++){
                EstadoOrganismo estadoAnterior = organismos[i][k];
                vecinosVivos = contarVecinosVivos(i,k);
                EstadoOrganismo resultado = calcularSiguienteEstado(estadoAnterior, vecinosVivos);
                matrizTemporal[i][k] = resultado;
                if(estadoAnterior != resultado && !huboCambios){
                    huboCambios = true;
                }
                if (resultado == EstadoOrganismo.VIVO && !hayOrganismosVivos){
                    hayOrganismosVivos = true;
                }
                historialMovimientos.registrar(new Movimiento(generacionActual, i, k, estadoAnterior, vecinosVivos, resultado));
            }
        }
        organismos = matrizTemporal;
    }

     // Reglas de Conway: una celda viva sobrevive con 2 o 3 vecinos vivos y
    // muere por soledad (<2) o sobrepoblacion (>3); una celda muerta nace
    // solo si tiene exactamente 3 vecinos vivos
    private EstadoOrganismo calcularSiguienteEstado(EstadoOrganismo estadoActual, int vecinosVivos){
        if (estadoActual == EstadoOrganismo.VIVO){
            if (vecinosVivos < 2 || vecinosVivos > 3){
                return EstadoOrganismo.MUERTO;
            }
            return EstadoOrganismo.VIVO;
        }
        if (vecinosVivos == 3){
            return EstadoOrganismo.VIVO;
        }
        return EstadoOrganismo.MUERTO;
    }

    // Se imprimen los numeros de columna arriba y el de fila a la
    // izquierda para poder ubicar cualquier celda del tablero
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        for (int k = 0; k < numeroColumnas; k++) {
            sb.append(String.format("%2d ", k));
        }
        sb.append("\n");
        for (int i = 0; i < numeroFilas; i++) {
            sb.append(String.format("%2d | ", i));
            for (int k = 0; k < numeroColumnas; k++) {
                sb.append(organismos[i][k].getSimbolo());
                sb.append("  ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    public int getNumeroFilas() {
        return numeroFilas;
    }

    public int getNumeroColumnas() {
        return numeroColumnas;
    }

}