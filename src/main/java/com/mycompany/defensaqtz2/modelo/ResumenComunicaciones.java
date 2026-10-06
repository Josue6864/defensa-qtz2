package com.mycompany.defensaqtz2.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResumenComunicaciones {

    private final int totalModulosTierra;
    private final int modulosTierraActivos;
    private final long capacidadActivaTotal;
    private final long totalHistoricoDescargado;
    private final List<ModuloTierra> mayoresDescargas;

    public ResumenComunicaciones(int totalModulosTierra,
                                 int modulosTierraActivos,
                                 long capacidadActivaTotal,
                                 long totalHistoricoDescargado,
                                 List<ModuloTierra> mayoresDescargas) {
        if (totalModulosTierra < 0 || modulosTierraActivos < 0
                || modulosTierraActivos > totalModulosTierra) {
            throw new IllegalArgumentException(
                    "Las cantidades de antenas no son validas.");
        }

        if (capacidadActivaTotal < 0 || totalHistoricoDescargado < 0) {
            throw new IllegalArgumentException(
                    "La capacidad y el historico no pueden ser negativos.");
        }

        if ((modulosTierraActivos == 0 && capacidadActivaTotal != 0)
                || (modulosTierraActivos > 0
                && capacidadActivaTotal < modulosTierraActivos)) {
            throw new IllegalArgumentException(
                    "La capacidad no coincide con las antenas activas.");
        }

        if (mayoresDescargas == null) {
            throw new IllegalArgumentException(
                    "La lista de mayores descargas es obligatoria.");
        }

        List<ModuloTierra> copia = new ArrayList<>(mayoresDescargas);

        if (copia.contains(null) || copia.size() > totalModulosTierra) {
            throw new IllegalArgumentException(
                    "La lista de mayores descargas no es valida.");
        }

        if ((totalModulosTierra == 0 && totalHistoricoDescargado != 0)
                || (totalModulosTierra > 0 && copia.isEmpty())) {
            throw new IllegalArgumentException(
                    "El resumen no coincide con la cantidad de antenas.");
        }

        this.totalModulosTierra = totalModulosTierra;
        this.modulosTierraActivos = modulosTierraActivos;
        this.capacidadActivaTotal = capacidadActivaTotal;
        this.totalHistoricoDescargado = totalHistoricoDescargado;
        this.mayoresDescargas = Collections.unmodifiableList(copia);
    }

    public int getTotalModulosTierra() {
        return totalModulosTierra;
    }

    public int getModulosTierraActivos() {
        return modulosTierraActivos;
    }

    public long getCapacidadActivaTotal() {
        return capacidadActivaTotal;
    }

    public long getTotalHistoricoDescargado() {
        return totalHistoricoDescargado;
    }

    public List<ModuloTierra> getMayoresDescargas() {
        return mayoresDescargas;
    }
}