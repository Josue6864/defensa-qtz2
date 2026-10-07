package com.mycompany.defensaqtz2.vista;

import com.mycompany.defensaqtz2.modelo.Modulo;
import com.mycompany.defensaqtz2.modelo.ModuloTierra;
import com.mycompany.defensaqtz2.modelo.RecursosMision;
import com.mycompany.defensaqtz2.modelo.ResultadoOperacion;
import com.mycompany.defensaqtz2.modelo.ResumenComunicaciones;
import java.util.List;
import java.util.Scanner;

public class VistaConsola {

    private final Scanner scanner;

    public VistaConsola() {
        scanner = new Scanner(System.in);
    }

    public int leerOpcionMenu() {
        System.out.println("\n===== DEFENSA DE QUETZAL-2 =====");
        System.out.println("1. Listar módulos");
        System.out.println("2. Buscar módulo");
        System.out.println("3. Mostrar catálogo por costo");
        System.out.println("4. Avanzar un ciclo");
        System.out.println("5. Mostrar resumen de comunicaciones");
        System.out.println("0. Salir");

        return leerEnteroEnRango("Selecciona una opción:", 0, 5);
    }

    public int leerCriterioBusqueda() {
        System.out.println("\nBuscar módulo:");
        System.out.println("1. Por ID");
        System.out.println("2. Por nombre");

        return leerEnteroEnRango("Selecciona el criterio:", 1, 2);
    }

    public String leerTexto(String solicitud) {
        while (true) {
            System.out.print(solicitud + " ");
            String texto = scanner.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            mostrarMensaje(
                    "La entrada no puede estar vacía. Intenta nuevamente.");
        }
    }

    private int leerEnteroEnRango(String solicitud, int minimo, int maximo) {
        while (true) {
            String entrada = leerTexto(solicitud);

            try {
                int valor = Integer.parseInt(entrada);

                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }

                mostrarMensaje(
                        "Ingresa una opción entre " + minimo
                        + " y " + maximo + ".");

            } catch (NumberFormatException e) {
                mostrarMensaje("Ingresa un número entero válido.");
            }
        }
    }

    public void mostrarModulo(Modulo modulo) {
        System.out.println(modulo.toString());
    }

    public void mostrarModulos(List<Modulo> modulos) {
        if (modulos.isEmpty()) {
            mostrarMensaje("No hay módulos para mostrar.");
            return;
        }

        for (Modulo modulo : modulos) {
            mostrarModulo(modulo);
        }
    }

    public void mostrarCiclo(List<ResultadoOperacion> resultados,
                             RecursosMision recursos,
                             long numeroCiclo) {
        System.out.println(
                "\n===== RESULTADOS DEL CICLO " + numeroCiclo + " =====");

        for (ResultadoOperacion resultado : resultados) {
            System.out.println(
                    "[" + resultado.getIdModulo() + "] "
                    + resultado.getNombreModulo());

            System.out.println(
                    "  " + describirMotivo(resultado.getMotivo()));

            if (resultado.isOpero()) {
                System.out.println(
                        "  Energía generada: " + resultado.getEnergiaGenerada()
                        + " | Energía consumida: "
                        + resultado.getEnergiaConsumida());

                System.out.println(
                        "  Datos recolectados: " + resultado.getDatosRecolectados()
                        + " MB | Datos descargados: "
                        + resultado.getDatosDescargados() + " MB");
            }
        }

        System.out.println("\nEstado al finalizar el ciclo:");

        System.out.println(
                "Energía disponible: " + recursos.getEnergiaDisponible());

        System.out.println(
                "Datos pendientes en órbita: "
                + recursos.getDatosEnOrbita() + " MB");

        System.out.println(
                "Total histórico descargado: "
                + recursos.getTotalDescargado() + " MB");
    }

    private String describirMotivo(String motivo) {
        switch (motivo) {
            case "OPERACION_REALIZADA":
                return "Operación realizada.";

            case "INACTIVO":
                return "No operó porque está inactivo.";

            case "ENERGIA_INSUFICIENTE":
                return "No operó por energía insuficiente.";

            case "SIN_DATOS":
                return "No operó porque no hay datos en órbita.";

            case "DESBORDAMIENTO":
                return "No operó porque se alcanzaría el límite numérico permitido.";

            default:
                return "Resultado sin descripción disponible.";
        }
    }

    public void mostrarResumen(ResumenComunicaciones resumen) {
        System.out.println("\n===== RESUMEN DE COMUNICACIONES =====");

        System.out.println(
                "Antenas totales: " + resumen.getTotalModulosTierra());

        System.out.println(
                "Antenas activas: " + resumen.getModulosTierraActivos());

        System.out.println(
                "Capacidad activa máxima: "
                + resumen.getCapacidadActivaTotal() + " MB por ciclo");

        System.out.println(
                "Total histórico descargado: "
                + resumen.getTotalHistoricoDescargado() + " MB");

        if (resumen.getMayoresDescargas().isEmpty()) {
            mostrarMensaje(
                    "No hay antenas para identificar una descarga máxima.");
            return;
        }

        System.out.println("Antenas con mayor descarga histórica:");

        for (ModuloTierra antena : resumen.getMayoresDescargas()) {
            System.out.println(
                    "ID: " + antena.getId()
                    + " | Nombre: " + antena.getNombre()
                    + " | Estación: " + antena.getEstacion()
                    + " | Histórico descargado: "
                    + antena.getTotalDescargado() + " MB");
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}