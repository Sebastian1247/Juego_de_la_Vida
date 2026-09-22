# Juego de la Vida (Conway)

Implementación en Java del Juego de la Vida de Conway usando arreglos, para el curso de Tecnologías de Programación (Maestría en Ciencias de la Computación, 1er semestre).

## Descripción

El programa pide el tamaño del tablero, el número de generaciones a simular y un patrón inicial de organismos vivos, y luego va mostrando la evolución del tablero generación por generación, aplicando las reglas clásicas de Conway:

- Una celda viva con 2 o 3 vecinos vivos sobrevive.
- Una celda viva con menos de 2 o más de 3 vecinos vivos muere (soledad o sobrepoblación).
- Una celda muerta con exactamente 3 vecinos vivos nace.

El tablero no es circular: las celdas de los bordes no tienen vecinos "al otro lado".

La simulación se detiene antes de llegar al número de generaciones pedido si ya no quedan organismos vivos, o si el tablero se estabiliza (dos generaciones seguidas iguales). Al terminar, se imprime el historial completo de movimientos (cambios de estado) de cada celda.

## Cómo compilar y ejecutar

Desde la raíz del proyecto:

```bash
javac -d bin src/*.java
java -cp bin App
```

## Uso

El programa pide, en orden:

1. **Número de filas** del tablero (entre 2 y 20).
2. **Número de columnas** del tablero (entre 2 y 20).
3. **Número de generaciones** a simular (entre 1 y 50).
4. **Patrón inicial**, como un string con el siguiente formato:

   ```
   cantidadOrganismos,fila1,columna1,fila2,columna2,...
   ```

   Las filas y columnas empiezan en 0. Por ejemplo, para un tablero de 5x5:

   ```
   3,1,2,2,2,3,2
   ```

   Coloca 3 organismos vivos en las posiciones (1,2), (2,2) y (3,2) — un patrón "blinker" que oscila entre vertical y horizontal en cada generación.

Durante la simulación, se presiona Enter para avanzar a la siguiente generación.

## Estructura del proyecto

| Clase | Responsabilidad |
|---|---|
| `App` | Punto de entrada; pide los datos por consola y arranca el juego. |
| `Tablero` | Guarda el estado de las celdas, calcula cada nueva generación y controla cuándo se detiene la simulación. |
| `LectorPatronInicial` | Valida el string del patrón inicial y lo convierte en coordenadas utilizables. |
| `EstadoOrganismo` | Enum con los dos estados posibles de una celda (`VIVO` / `MUERTO`) y su símbolo. |
| `Movimiento` | Representa el cambio de estado de una celda en una generación específica. |
| `HistorialMovimientos` | Guarda y muestra la lista de todos los movimientos ocurridos durante el juego. |
| `Keyboard` | Utilería para leer datos desde la consola. |

## Autores

- Sebastian Verdugo Bermudez — [Sebastian1247](https://github.com/Sebastian1247)
- Roberto Carlos Saucedo Rodriguez — [RobertoCSR2003](https://github.com/RobertoCSR2003) / [RobertoCR2003](https://github.com/RobertoCR2003)

Tecnologías de Programación, Maestría en Ciencias de la Computación (1er semestre)
