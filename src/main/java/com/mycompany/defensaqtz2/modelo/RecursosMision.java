package com.mycompany.defensaqtz2.modelo;

public class RecursosMision {

    private long energiaDisponible;
    private long datosEnOrbita;
    private long totalDescargado;

    //Aqui recibimos los datos long de cada variable para validar que no sean negativos
    public RecursosMision(long energiaDisponible, long datosEnOrbita,
                         long totalDescargado) {
        if (energiaDisponible < 0 || datosEnOrbita < 0 || totalDescargado < 0) {
            throw new IllegalArgumentException(
                    "Los saldos iniciales no pueden ser negativos.");
        }
        this.energiaDisponible = energiaDisponible;
        this.datosEnOrbita = datosEnOrbita;
        this.totalDescargado = totalDescargado;
    }

    public boolean hayEnergia(long cantidad) {
        validarPositivo(cantidad, "La cantidad de energia");
        return energiaDisponible >= cantidad;
    }

    public void generarEnergia(long cantidad) {
        validarPositivo(cantidad, "La energia generada");
        // addExact nos sirve para ver si el numero se desborda o no cabe
        energiaDisponible = Math.addExact(energiaDisponible, cantidad);
    }

    public boolean intentarRecolectar(long consumo, long datos) {
        validarPositivo(consumo, "El consumo");
        validarPositivo(datos, "Los datos por recolectar");

        if (!hayEnergia(consumo)) {
            return false;
        }

        /* Calcular todo antes de modificar los saldos para ver que las cantidades
        esten bien
        */
        long nuevaEnergia = energiaDisponible - consumo;
        long nuevosDatos = Math.addExact(datosEnOrbita, datos);

        energiaDisponible = nuevaEnergia;
        datosEnOrbita = nuevosDatos;
        return true;
    }

    public long intentarDescargar(long consumo, long capacidad) {
        validarPositivo(consumo, "El consumo");
        validarPositivo(capacidad, "La capacidad de descarga");

        if (datosEnOrbita == 0 || !hayEnergia(consumo)) {
            return 0;
        }

        long descargados = Math.min(capacidad, datosEnOrbita);
        long nuevaEnergia = energiaDisponible - consumo;
        long nuevosDatos = datosEnOrbita - descargados;
        long nuevoTotal = Math.addExact(totalDescargado, descargados);

        // Una descarga parcial consume la energia completa.
        energiaDisponible = nuevaEnergia;
        datosEnOrbita = nuevosDatos;
        totalDescargado = nuevoTotal;
        return descargados;
    }

    private static void validarPositivo(long valor, String descripcion) {
        if (valor <= 0) {
            throw new IllegalArgumentException(
                    descripcion + " debe ser mayor que cero.");
        }
    }

    public long getEnergiaDisponible() {
        return energiaDisponible;
    }

    public long getDatosEnOrbita() {
        return datosEnOrbita;
    }

    public long getTotalDescargado() {
        return totalDescargado;
    }
}
