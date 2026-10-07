package com.mycompany.defensaqtz2.controlador;

import com.mycompany.defensaqtz2.modelo.Mision;
import com.mycompany.defensaqtz2.modelo.Modulo;
import com.mycompany.defensaqtz2.modelo.ResultadoOperacion;
import com.mycompany.defensaqtz2.vista.VistaConsola;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Controlador {

    private final Mision mision;
    private final VistaConsola vista;

    public Controlador(Mision mision, VistaConsola vista) {
        if (mision == null || vista == null) {
            throw new IllegalArgumentException(
                    "La mision y la vista son obligatorias.");
        }

        this.mision = mision;
        this.vista = vista;
    }

    public void iniciar() {
        vista.mostrarMensaje("Bienvenido a Defensa de Quetzal-2.");

        try {
            while (true) {
                int opcion = vista.leerOpcionMenu();

                switch (opcion) {
                    case 1:
                        listarModulos();
                        break;

                    case 2:
                        buscarModulo();
                        break;

                    case 3:
                        mostrarCatalogo();
                        break;

                    case 4:
                        avanzarCiclo();
                        break;

                    case 5:
                        mostrarComunicaciones();
                        break;

                    case 0:
                        vista.mostrarMensaje(
                                "Simulacion finalizada. Hasta pronto.");
                        return;

                    default:
                        vista.mostrarMensaje(
                                "Opcion no valida. Intenta nuevamente.");
                }
            }
        } catch (NoSuchElementException e) {
            // Scanner puede lanzar esta excepcion si se cierra la entrada.
            vista.mostrarMensaje(
                    "Se cerro la entrada. Simulacion finalizada.");
        }
    }

    private void listarModulos() {
        vista.mostrarMensaje("Modulos en orden de participacion:");
        vista.mostrarModulos(mision.listarModulos());
    }

    private void buscarModulo() {
        int criterio = vista.leerCriterioBusqueda();

        if (criterio == 1) {
            String id = vista.leerTexto("Ingresa el ID del modulo:");
            Optional<Modulo> encontrado = mision.buscarPorId(id);

            if (encontrado.isPresent()) {
                vista.mostrarModulo(encontrado.get());
            } else {
                vista.mostrarMensaje(
                        "No se encontro un modulo con ese ID.");
            }

        } else if (criterio == 2) {
            String nombre = vista.leerTexto(
                    "Ingresa el nombre del modulo:");

            List<Modulo> encontrados = mision.buscarPorNombre(nombre);

            if (encontrados.isEmpty()) {
                vista.mostrarMensaje(
                        "No se encontraron modulos con ese nombre.");
            } else {
                vista.mostrarModulos(encontrados);
            }

        } else {
            vista.mostrarMensaje("Criterio de busqueda no valido.");
        }
    }

    private void mostrarCatalogo() {
        vista.mostrarMensaje(
                "Catalogo por costo de construccion, de menor a mayor:");

        vista.mostrarModulos(mision.obtenerCatalogoPorCosto());
    }

    private void avanzarCiclo() {
        List<ResultadoOperacion> resultados;

        try {
            resultados = mision.avanzarCiclo();

        } catch (ArithmeticException e) {
            vista.mostrarMensaje(
                    "Se alcanzo el limite del contador de ciclos. "
                    + "No se inicio un ciclo nuevo.");
            return;
        }

        vista.mostrarCiclo(
                resultados,
                mision.getRecursos(),
                mision.getCiclosTranscurridos()
        );
    }

    private void mostrarComunicaciones() {
        vista.mostrarResumen(mision.obtenerResumenComunicaciones());
    }
}