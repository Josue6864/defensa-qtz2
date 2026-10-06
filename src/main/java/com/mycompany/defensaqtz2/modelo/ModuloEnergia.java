package com.mycompany.defensaqtz2.modelo;

public class ModuloEnergia extends Modulo {

    private final long energiaPorCiclo;
    private long ciclosAcumulados;

    public ModuloEnergia(String id, String nombre, int salud,
            boolean activo, long costoConstruccion,
            long energiaPorCiclo, long ciclosAcumulados) {
        super(id, nombre, salud, activo, costoConstruccion);

        if (energiaPorCiclo <= 0) {
            throw new IllegalArgumentException(
                    "La energia por ciclo debe ser mayor que cero.");
        }
        if (ciclosAcumulados < 0) {
            throw new IllegalArgumentException(
                    "Los ciclos acumulados no pueden ser negativos.");
        }
        if (ciclosAcumulados > Long.MAX_VALUE / energiaPorCiclo) {
            throw new IllegalArgumentException(
                    "El total generado inicial excede el limite de long.");
        }

        this.energiaPorCiclo = energiaPorCiclo;
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

        // Aca comprobamos el contador y el historico antes de cambiar recursos.
        long nuevosCiclos = Math.addExact(ciclosAcumulados, 1);
        /* El resultado se descarta: solo queremos que lance excepcion si el
         historico generado ya no cabe en un long*/
        Math.multiplyExact(energiaPorCiclo, nuevosCiclos);

        ResultadoOperacion resultado = new ResultadoOperacion(
                getId(), getNombre(), true, "OPERACION_REALIZADA",
                energiaPorCiclo, 0, 0, 0);

        recursos.generarEnergia(energiaPorCiclo);
        ciclosAcumulados = nuevosCiclos;
        return resultado;
    }

    public long getTotalGenerado() {
        return Math.multiplyExact(energiaPorCiclo, ciclosAcumulados);
    }

    public long getEnergiaPorCiclo() {
        return energiaPorCiclo;
    }

    public long getCiclosAcumulados() {
        return ciclosAcumulados;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Tipo: Energia"
                + " | Energia por ciclo: " + energiaPorCiclo
                + " | Ciclos acumulados: " + ciclosAcumulados
                + " | Total generado: " + getTotalGenerado();
    }
}
