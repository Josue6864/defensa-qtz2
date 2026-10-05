package com.mycompany.defensaqtz2.modelo;

public class ResultadoOperacion {

    private final String idModulo;
    private final String nombreModulo;
    private final boolean opero;
    private final String motivo;
    private final long energiaGenerada;
    private final long energiaConsumida;
    private final long datosRecolectados;
    private final long datosDescargados;

    public ResultadoOperacion(String idModulo, String nombreModulo,
                              boolean opero, String motivo,
                              long energiaGenerada, long energiaConsumida,
                              long datosRecolectados, long datosDescargados) {
        if (idModulo == null || idModulo.trim().isEmpty()
                || nombreModulo == null || nombreModulo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El ID y el nombre del modulo son obligatorios.");
        }

        boolean motivoExitoso = "OPERACION_REALIZADA".equals(motivo);
        boolean motivoImpedimento = "INACTIVO".equals(motivo)
                || "ENERGIA_INSUFICIENTE".equals(motivo)
                || "SIN_DATOS".equals(motivo);

        if ((!motivoExitoso && !motivoImpedimento) || opero != motivoExitoso) {
            throw new IllegalArgumentException(
                    "El motivo debe ser valido y coincidir con opero.");
        }

        if (energiaGenerada < 0 || energiaConsumida < 0
                || datosRecolectados < 0 || datosDescargados < 0) {
            throw new IllegalArgumentException(
                    "Las cantidades del resultado no pueden ser negativas.");
        }

        if (!opero && (energiaGenerada != 0 || energiaConsumida != 0
                || datosRecolectados != 0 || datosDescargados != 0)) {
            throw new IllegalArgumentException(
                    "Si el modulo no opero, todas las cantidades deben ser cero.");
        }

        this.idModulo = idModulo.trim();
        this.nombreModulo = nombreModulo.trim();
        this.opero = opero;
        this.motivo = motivo;
        this.energiaGenerada = energiaGenerada;
        this.energiaConsumida = energiaConsumida;
        this.datosRecolectados = datosRecolectados;
        this.datosDescargados = datosDescargados;
    }

    public String getIdModulo() {
        return idModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public boolean isOpero() {
        return opero;
    }

    public String getMotivo() {
        return motivo;
    }

    public long getEnergiaGenerada() {
        return energiaGenerada;
    }

    public long getEnergiaConsumida() {
        return energiaConsumida;
    }

    public long getDatosRecolectados() {
        return datosRecolectados;
    }

    public long getDatosDescargados() {
        return datosDescargados;
    }
}