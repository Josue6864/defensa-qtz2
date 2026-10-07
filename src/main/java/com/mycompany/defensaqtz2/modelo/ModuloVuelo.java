package com.mycompany.defensaqtz2.modelo;

public class ModuloVuelo extends Modulo {

    private final TipoInstrumento tipoInstrumento;
    private final long datosPorCiclo;
    private final long consumoEnergia;
    private long ciclosAcumulados;

    public ModuloVuelo(String id, String nombre, int salud,
            boolean activo, long costoConstruccion,
            TipoInstrumento tipoInstrumento, long datosPorCiclo,
            long consumoEnergia, long ciclosAcumulados) {
        super(id, nombre, salud, activo, costoConstruccion);

        if (tipoInstrumento == null) {
            throw new IllegalArgumentException("El instrumento es obligatorio.");
        }
        if (datosPorCiclo <= 0 || consumoEnergia <= 0) {
            throw new IllegalArgumentException(
                    "Los datos por ciclo y el consumo deben ser positivos.");
        }
        if (ciclosAcumulados < 0) {
            throw new IllegalArgumentException(
                    "Los ciclos acumulados no pueden ser negativos.");
        }
        if (ciclosAcumulados > Long.MAX_VALUE / datosPorCiclo) {
            throw new IllegalArgumentException(
                    "El total recolectado inicial excede el limite de long.");
        }

        this.tipoInstrumento = tipoInstrumento;
        this.datosPorCiclo = datosPorCiclo;
        this.consumoEnergia = consumoEnergia;
        this.ciclosAcumulados = ciclosAcumulados;
    }

    @Override
    public ResultadoOperacion procesarCiclo(RecursosMision recursos) {
        if (recursos == null) {
            throw new IllegalArgumentException("Los recursos son obligatorios.");
        }
        if (!isActivo()) {
            return new ResultadoOperacion(getId(), getNombre(), false,
                    "INACTIVO", 0, 0, 0, 0);
        }
        if (!recursos.hayEnergia(consumoEnergia)) {
            return new ResultadoOperacion(getId(), getNombre(), false,
                    "ENERGIA_INSUFICIENTE", 0, 0, 0, 0);
        }

        long nuevosCiclos = Math.addExact(ciclosAcumulados, 1);
        Math.multiplyExact(datosPorCiclo, nuevosCiclos);

        /* aca llamamos al metodo intentarRecolectar de la clase Recursos mision para recibir los parametros
    y hacer las modificaciones en ciclos
         */
        if (!recursos.intentarRecolectar(consumoEnergia, datosPorCiclo)) {
            return new ResultadoOperacion(getId(), getNombre(), false,
                    "ENERGIA_INSUFICIENTE", 0, 0, 0, 0);
        }

        ResultadoOperacion resultado = new ResultadoOperacion(
                getId(), getNombre(), true, "OPERACION_REALIZADA",
                0, consumoEnergia, datosPorCiclo, 0);

        ciclosAcumulados = nuevosCiclos;
        return resultado;
    }

    public long getTotalRecolectado() {
        return Math.multiplyExact(datosPorCiclo, ciclosAcumulados);
    }

    public TipoInstrumento getTipoInstrumento() {
        return tipoInstrumento;
    }

    public long getDatosPorCiclo() {
        return datosPorCiclo;
    }

    public long getConsumoEnergia() {
        return consumoEnergia;
    }

    public long getCiclosAcumulados() {
        return ciclosAcumulados;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Tipo: Vuelo"
                + " | Instrumento: " + tipoInstrumento
                + " | MB por ciclo: " + datosPorCiclo
                + " | Consumo de energia: " + consumoEnergia
                + " | Ciclos acumulados: " + ciclosAcumulados
                + " | Total recolectado: " + getTotalRecolectado() + " MB";
    }
}
