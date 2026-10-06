package com.mycompany.defensaqtz2.modelo;

public class ModuloTierra extends Modulo {

    private String estacion;
    private final long capacidadDescarga;
    private final long energiaPorDescarga;
    private long totalDescargado;

    public ModuloTierra(String id, String nombre, int salud,
            boolean activo, long costoConstruccion,
            String estacion, long capacidadDescarga,
            long energiaPorDescarga, long totalDescargado) {
        super(id, nombre, salud, activo, costoConstruccion);

        if (estacion == null || estacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La estacion es obligatoria.");
        }
        if (capacidadDescarga <= 0 || energiaPorDescarga <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad y el consumo de descarga deben ser positivos.");
        }
        if (totalDescargado < 0) {
            throw new IllegalArgumentException(
                    "El total descargado no puede ser negativo.");
        }

        this.estacion = estacion.trim();
        this.capacidadDescarga = capacidadDescarga;
        this.energiaPorDescarga = energiaPorDescarga;
        this.totalDescargado = totalDescargado;
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
        if (recursos.getDatosEnOrbita() == 0) {
            return new ResultadoOperacion(getId(), getNombre(), false,
                    "SIN_DATOS", 0, 0, 0, 0);
        }
        if (!recursos.hayEnergia(energiaPorDescarga)) {
            return new ResultadoOperacion(getId(), getNombre(), false,
                    "ENERGIA_INSUFICIENTE", 0, 0, 0, 0);
        }

        long descargados = Math.min(capacidadDescarga, recursos.getDatosEnOrbita());
        long nuevoTotal = Math.addExact(totalDescargado, descargados);

        ResultadoOperacion resultado = new ResultadoOperacion(
                getId(), getNombre(), true, "OPERACION_REALIZADA",
                0, energiaPorDescarga, 0, descargados);

        // Aqui ocurre la descarga deverdad, le pide a recursosMision que reste la energia completa 
        long descargadosReales = recursos.intentarDescargar(
                energiaPorDescarga, capacidadDescarga);

        //Validacion para ver que ambos valores coincidan
        if (descargadosReales != descargados) {
            throw new IllegalStateException(
                    "La descarga real no coincide con la esperada.");
        }

        //Se actualiza el historico de esa antena hasta ahorita por que ya se verifico que no tuviera errores con addExact.
        totalDescargado = nuevoTotal;
        return resultado;
    }

    public String getEstacion() {
        return estacion;
    }

    public long getCapacidadDescarga() {
        return capacidadDescarga;
    }

    public long getEnergiaPorDescarga() {
        return energiaPorDescarga;
    }

    public long getTotalDescargado() {
        return totalDescargado;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Tipo: Tierra"
                + " | Estacion: " + estacion
                + " | Capacidad de descarga: " + capacidadDescarga + " MB"
                + " | Energia por descarga: " + energiaPorDescarga
                + " | Total descargado: " + totalDescargado + " MB";
    }
}
