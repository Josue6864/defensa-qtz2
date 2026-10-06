package com.mycompany.defensaqtz2.modelo;

import java.util.Locale;

public abstract class Modulo {

    private final String id;
    private String nombre;
    private int salud;
    private boolean activo;
    private final long costoConstruccion;

    protected Modulo(String id, String nombre, int salud,
                     boolean activo, long costoConstruccion) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (salud < 0 || salud > 100) {
            throw new IllegalArgumentException("La salud debe estar entre 0 y 100.");
        }
        if (activo && salud == 0) {
            throw new IllegalArgumentException(
                    "Un modulo con salud cero no puede estar activo.");
        }
        if (costoConstruccion < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }

        this.id = id.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
        this.salud = salud;
        this.activo = activo;
        this.costoConstruccion = costoConstruccion;
    }

    // Cada subclase implementara esta operacion usando los mismos parametros de recursos.
    public abstract ResultadoOperacion procesarCiclo(RecursosMision recursos);

    @Override
    public String toString() {
        return "ID: " + id
                + " | Nombre: " + nombre
                + " | Salud: " + salud
                + " | Activo: " + (activo ? "Si" : "No")
                + " | Costo de construccion: " + costoConstruccion;
    }

    @Override
    public final boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Modulo)) {
            return false;
        }
        Modulo otroModulo = (Modulo) otro;
        return id.equals(otroModulo.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSalud() {
        return salud;
    }

    public boolean isActivo() {
        return activo;
    }

    public long getCostoConstruccion() {
        return costoConstruccion;
    }
}
