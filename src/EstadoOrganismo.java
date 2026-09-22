// Estado de una celda del tablero, con el simbolo que se usa para
// imprimirla
public enum EstadoOrganismo {
    VIVO('*'),
    MUERTO('.');

    private final char simbolo;

    EstadoOrganismo(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }
}
