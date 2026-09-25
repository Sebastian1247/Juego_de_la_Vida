import java.util.List;

/**
 * Tablero del juego de la vida: guarda el estado de las celdas, avanza
 * las generaciones y lleva el historial de movimientos.
 */
public class Tablero {

    private final int numeroFilas;
    private final int numeroColumnas;
    private final LectorPatronInicial lectorPatronInicial;
    private final HistorialMovimientos historialMovimientos;
    private final String encabezado;
    private EstadoOrganismo [][] organismos;
    private int generacionActual;
    private boolean hayOrganismosVivos;
    private boolean huboCambios;


    public Tablero (int numeroFilas, int numeroColumnas, LectorPatronInicial lectorPatronInicial){
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;
        this.lectorPatronInicial = lectorPatronInicial;
        historialMovimientos = new HistorialMovimientos();
        encabezado = crearEncabezado();
        organismos = new EstadoOrganismo[numeroFilas][numeroColumnas];
        inicializarOrganismos();
        generacionActual = 1;
        hayOrganismosVivos = false;
        huboCambios = false;
    }

    private void inicializarOrganismos(){
        // Inicializar el tablero con organismos muertos
        for (int i = 0; i < numeroFilas; i++) {
            for (int k = 0; k < numeroColumnas; k++) {
                organismos[i][k] = EstadoOrganismo.MUERTO;
            }
        }
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
        boolean esCadenaValida = true;
        List<Integer> coordenadas = lectorPatronInicial.procesarDatosTablero(datosIniciales, numeroFilas, numeroColumnas);
        if (coordenadas == null){
            esCadenaValida = false;
            System.out.println(lectorPatronInicial.getMensajeError());
        }
        else{
            crearTableroInicial(coordenadas);

            boolean juegoFinalizado = false;
            do {
                System.out.println("Generacion numero "+generacionActual+": ");
                System.out.println(this);
                juegoFinalizado = generacionActual == generaciones 
                    || (generacionActual > 1 && debeDetenerse());
                if(!juegoFinalizado){
                    System.out.println("Presione enter para continuar...");
                    Keyboard.readLine();
                    nextGeneracion();
                }
            } while (!juegoFinalizado);
            historialMovimientos.imprimirMovimientos();
        }
        return esCadenaValida;
    }

    // Revisa las 2 condiciones de paro: que ya no haya organismos vivos, o
    // que el tablero se haya estabilizado entre esta generacion y la anterior
    private boolean debeDetenerse(){
        boolean hayQueDetenerse = false;
        if (!hayOrganismosVivos){
            System.out.println("Ya no hay organismos vivos, el programa termina...");
            hayQueDetenerse = true;
        }
        if (!huboCambios){
            System.out.println("Las ultimas 2 generaciones se estabilizaron, terminando programa...");
            hayQueDetenerse = true;
        }
        return hayQueDetenerse;
    }

    // Genera el tablero inicial en base a la lista de enteros de coordenadas
    private void crearTableroInicial(List<Integer> coordenadas){
        for(int i = 0; i < coordenadas.size(); i += 2){
            int fila = coordenadas.get(i);
            int columna = coordenadas.get(i + 1);
            organismos[fila][columna] = EstadoOrganismo.VIVO;
        }
    }


    // Cuenta cuantos de los 8 vecinos de la celda (fila, columna) estan
    // vivos. Los vecinos fuera del tablero no cuentan, el tablero no
    // da la vuelta en los bordes
    private int contarVecinosVivos(int fila, int columna){
        int vecinos = 0;
            //Ciclo que recorre los vecinos alrededor de la celda que queremos
            for (int i = fila - 1; i <= fila + 1; i++) {
                for (int k = columna - 1; k <= columna + 1; k++) {
                    try {
                        if (organismos[i][k] == EstadoOrganismo.VIVO) {
                            vecinos++;
                        }
                    } catch (ArrayIndexOutOfBoundsException e){}
                }
            }
        if(organismos[fila][columna] == EstadoOrganismo.VIVO){
            vecinos--;
        }
        return vecinos;
    }

    private void nextGeneracion(){
        // Se guarda la generacion nueva en una matriz aparte para que el
        // calculo de cada celda siga usando el estado anterior del
        // tablero, y no valores que ya se hayan actualizado en esta
        // misma generacion
        int vecinosVivos;
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

    // Arma la fila con los numeros de columna. Se llama una sola vez desde
    // el constructor porque el numero de columnas no cambia entre generaciones
    private String crearEncabezado(){
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        for(int i = 0; i < numeroColumnas; i++){
            sb.append(String.format("%2d ", i));
        }
        sb.append("\n");
        return sb.toString();
    }

    // Regresa el tablero como texto, con el encabezado de columnas arriba
    // y el numero de fila a la izquierda para poder ubicar cualquier celda
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(encabezado);
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