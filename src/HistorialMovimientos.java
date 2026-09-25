import java.util.ArrayList;
import java.util.List;

// Guarda y muestra la lista de todos los movimientos (cambios de estado
// de una celda) que ocurren a lo largo del juego
public class HistorialMovimientos {

    private List<Movimiento> movimientos;

    public HistorialMovimientos(){
        movimientos = new ArrayList<>();
    }

    public void registrar(Movimiento movimiento) {
        movimientos.add(movimiento);
    }

    public void imprimirMovimientos() {
        System.out.println("Listado de movimientos realizados:");
        int generacionAnterior = -1;
        //Esto podria corregirse aunque no fue indicado
        for (Movimiento m : movimientos) {
            if (m.getNumeroGeneracion() != generacionAnterior) {
                System.out.println();
                System.out.println("=== Generacion " + m.getNumeroGeneracion() + " ===");
                System.out.printf("%-6s %-6s %-8s %-10s %-8s %-10s%n",
                        "Gen", "Fila", "Columna", "Estado Ant.", "Vecinos", "Resultado");
                System.out.println("-------------------------------------------------------");
                generacionAnterior = m.getNumeroGeneracion();
            }
            System.out.println(m);
        }
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

}