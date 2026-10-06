package com.mycompany.defensaqtz2.modelo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class Mision {

    private final List<Modulo> modulos;
    private final RecursosMision recursos;
    private long ciclosTranscurridos;

    public Mision(List<Modulo> modulos, RecursosMision recursos,
                  long ciclosTranscurridos) {
        if (modulos == null || recursos == null) {
            throw new IllegalArgumentException(
                    "Los modulos y los recursos son obligatorios.");
        }

        if (ciclosTranscurridos < 0) {
            throw new IllegalArgumentException(
                    "Los ciclos transcurridos no pueden ser negativos.");
        }

        this.modulos = Collections.unmodifiableList(new ArrayList<>(modulos));
        this.recursos = recursos;
        this.ciclosTranscurridos = ciclosTranscurridos;

        validarCargaInicial();
    }

    private void validarCargaInicial() {
        if (modulos.size() < 10) {
            throw new IllegalArgumentException(
                    "La mision necesita al menos 10 modulos.");
        }

        Set<String> ids = new HashSet<>();

        boolean hayEnergia = false;
        boolean hayVuelo = false;
        boolean hayTierra = false;

        BigInteger recolectado = BigInteger.ZERO;
        BigInteger descargado = BigInteger.ZERO;
        BigInteger capacidadActiva = BigInteger.ZERO;

        for (Modulo modulo : modulos) {
            if (modulo == null) {
                throw new IllegalArgumentException(
                        "La lista contiene un modulo nulo.");
            }

            if (!ids.add(modulo.getId())) {
                throw new IllegalArgumentException(
                        "ID duplicado: " + modulo.getId());
            }

            if (modulo instanceof ModuloEnergia) {
                hayEnergia = true;
                ModuloEnergia energia = (ModuloEnergia) modulo;

                if (energia.getCiclosAcumulados() > ciclosTranscurridos) {
                    throw new IllegalArgumentException(
                            "Ciclos incompatibles: " + modulo.getId());
                }

            } else if (modulo instanceof ModuloVuelo) {
                hayVuelo = true;
                ModuloVuelo vuelo = (ModuloVuelo) modulo;

                if (vuelo.getCiclosAcumulados() > ciclosTranscurridos) {
                    throw new IllegalArgumentException(
                            "Ciclos incompatibles: " + modulo.getId());
                }

                recolectado = recolectado.add(
                        BigInteger.valueOf(vuelo.getTotalRecolectado()));

            } else if (modulo instanceof ModuloTierra) {
                hayTierra = true;
                ModuloTierra tierra = (ModuloTierra) modulo;

                BigInteger limiteDescarga =
                        BigInteger.valueOf(tierra.getCapacidadDescarga())
                                .multiply(BigInteger.valueOf(ciclosTranscurridos));

                if (BigInteger.valueOf(tierra.getTotalDescargado())
                        .compareTo(limiteDescarga) > 0) {
                    throw new IllegalArgumentException(
                            "Historico imposible para la antena: "
                                    + modulo.getId());
                }

                descargado = descargado.add(
                        BigInteger.valueOf(tierra.getTotalDescargado()));

                if (tierra.isActivo()) {
                    capacidadActiva = capacidadActiva.add(
                            BigInteger.valueOf(tierra.getCapacidadDescarga()));
                }
            }
        }

        if (!hayEnergia || !hayVuelo || !hayTierra) {
            throw new IllegalArgumentException(
                    "La carga debe incluir los tres tipos de modulo.");
        }

        if (!descargado.equals(
                BigInteger.valueOf(recursos.getTotalDescargado()))) {
            throw new IllegalArgumentException(
                    "El historico global no coincide con las antenas.");
        }

        BigInteger datosContabilizados =
                BigInteger.valueOf(recursos.getDatosEnOrbita())
                        .add(BigInteger.valueOf(recursos.getTotalDescargado()));

        if (!recolectado.equals(datosContabilizados)) {
            throw new IllegalArgumentException(
                    "Lo recolectado debe ser igual a lo pendiente mas lo descargado.");
        }

        if (capacidadActiva.compareTo(
                BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            throw new IllegalArgumentException(
                    "La capacidad activa total excede el limite de long.");
        }
    }

    public List<Modulo> listarModulos() {
        return Collections.unmodifiableList(new ArrayList<>(modulos));
    }

    public Optional<Modulo> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }

        String buscado = id.trim().toUpperCase(Locale.ROOT);

        for (Modulo modulo : modulos) {
            if (modulo.getId().equals(buscado)) {
                return Optional.of(modulo);
            }
        }

        return Optional.empty();
    }

    public List<Modulo> buscarPorNombre(String nombre) {
        List<Modulo> coincidencias = new ArrayList<>();

        if (nombre != null && !nombre.trim().isEmpty()) {
            String buscado = nombre.trim();

            for (Modulo modulo : modulos) {
                if (modulo.getNombre().equalsIgnoreCase(buscado)) {
                    coincidencias.add(modulo);
                }
            }
        }

        return Collections.unmodifiableList(coincidencias);
    }

    public List<Modulo> obtenerCatalogoPorCosto() {
        List<Modulo> catalogo = new ArrayList<>(modulos);

        catalogo.sort(
                Comparator.comparingLong(Modulo::getCostoConstruccion)
                        .thenComparing(Modulo::getId)
        );

        return Collections.unmodifiableList(catalogo);
    }

    public List<ResultadoOperacion> avanzarCiclo() {
        // Validar la cantidad de ciclos ocurridos antes de modificar los datos delicados.
        long siguienteCiclo = Math.addExact(ciclosTranscurridos, 1);

        List<ResultadoOperacion> resultados = new ArrayList<>();

        for (Modulo modulo : modulos) {
            try {
                //Se utiliza procesar Ciclo de modulo de esta manera aplicando el polimorfismo en esta clase
                resultados.add(modulo.procesarCiclo(recursos));

            } catch (ArithmeticException e) {
                // Aqui se agrega a resultados el problema en este caso desbordamiento osea continua con el ciclo
                resultados.add(new ResultadoOperacion(
                        modulo.getId(),
                        modulo.getNombre(),
                        false,
                        "DESBORDAMIENTO",
                        0, 0, 0, 0
                ));
            }
        }

        ciclosTranscurridos = siguienteCiclo;

        return Collections.unmodifiableList(resultados);
    }

    public ResumenComunicaciones obtenerResumenComunicaciones() {
        int totalTierra = 0;
        int activos = 0;
        long capacidadActiva = 0;
        long historico = 0;
        long mayorDescarga = -1;

        List<ModuloTierra> mayores = new ArrayList<>();

        for (Modulo modulo : modulos) {
            if (modulo instanceof ModuloTierra) {
                ModuloTierra tierra = (ModuloTierra) modulo;
                totalTierra++;

                if (tierra.isActivo()) {
                    activos++;
                    capacidadActiva = Math.addExact(
                            capacidadActiva,
                            tierra.getCapacidadDescarga()
                    );
                }

                historico = Math.addExact(
                        historico,
                        tierra.getTotalDescargado()
                );

                if (tierra.getTotalDescargado() > mayorDescarga) {
                    mayorDescarga = tierra.getTotalDescargado();
                    mayores.clear();
                    mayores.add(tierra);

                } else if (tierra.getTotalDescargado() == mayorDescarga) {
                    mayores.add(tierra);
                }
            }
        }

        return new ResumenComunicaciones(
                totalTierra,
                activos,
                capacidadActiva,
                historico,
                mayores
        );
    }

    public RecursosMision getRecursos() {
        return recursos;
    }

    public long getCiclosTranscurridos() {
        return ciclosTranscurridos;
    }
}