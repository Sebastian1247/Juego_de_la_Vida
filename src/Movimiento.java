// Representa el cambio de estado de una celda en una generacion: en que
// fila y columna, cual era su estado antes, cuantos vecinos vivos tenia
// y en que estado quedo
public class Movimiento {
    private final int numeroGeneracion;
    private final int fila;
    private final int columna;
    private final EstadoOrganismo estadoAnterior;
    private final int vecinosVivos;
    private final EstadoOrganismo resultado;

    public Movimiento(int numeroGeneracion, int fila, int columna, EstadoOrganismo estadoAnterior,
                      int vecinosVivos, EstadoOrganismo resultado) {
        this.numeroGeneracion = numeroGeneracion;
        this.fila = fila;
        this.columna = columna;
        this.estadoAnterior = estadoAnterior;
        this.vecinosVivos = vecinosVivos;
        this.resultado = resultado;
    }

    public int getNumeroGeneracion() {
        return numeroGeneracion;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public EstadoOrganismo getEstadoAnterior() {
        return estadoAnterior;
    }

    public int getVecinosVivos() {
        return vecinosVivos;
    }

    public EstadoOrganismo getResultado() {
        return resultado;
    }

    @Override
    public String toString(){
        return String.format("%-6d %-6d %-8d %-10s %-8d %-10s",
                numeroGeneracion, fila, columna, estadoAnterior, vecinosVivos, resultado);
    }
}