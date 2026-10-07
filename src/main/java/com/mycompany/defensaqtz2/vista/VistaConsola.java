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
        System.out.println("1. Listar modulos");
        System.out.println("2. Buscar modulo");
        System.out.println("3. Mostrar catalogo por costo");
        System.out.println("4. Avanzar un ciclo");
        System.out.println("5. Mostrar resumen de comunicaciones");
        System.out.println("0. Salir");

        return leerEnteroEnRango("Selecciona una opcion:", 0, 5);
    }

    public int leerCriterioBusqueda() {
        System.out.println("\nBuscar modulo:");
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
                    "La entrada no puede estar vacia. Intenta nuevamente.");
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
                        "Ingresa una opcion entre " + minimo
                        + " y " + maximo + ".");

            } catch (NumberFormatException e) {
                mostrarMensaje("Ingresa un numero entero valido.");
            }
        }
    }

    public void mostrarModulo(Modulo modulo) {
        System.out.println(modulo.toString());
    }

    public void mostrarModulos(List<Modulo> modulos) {
        if (modulos.isEmpty()) {
            mostrarMensaje("No hay modulos para mostrar.");
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
                        "  Energia generada: " + resultado.getEnergiaGenerada()
                        + " | Energia consumida: "
                        + resultado.getEnergiaConsumida());

                System.out.println(
                        "  Datos recolectados: " + resultado.getDatosRecolectados()
                        + " MB | Datos descargados: "
                        + resultado.getDatosDescargados() + " MB");
            }
        }

        System.out.println("\nEstado al finalizar el ciclo:");

        System.out.println(
                "Energia disponible: " + recursos.getEnergiaDisponible());

        System.out.println(
                "Datos pendientes en orbita: "
                + recursos.getDatosEnOrbita() + " MB");

        System.out.println(
                "Total historico descargado: "
                + recursos.getTotalDescargado() + " MB");
    }

    private String describirMotivo(String motivo) {
        switch (motivo) {
            case "OPERACION_REALIZADA":
                return "Operacion realizada.";

            case "INACTIVO":
                return "No opero porque esta inactivo.";

            case "ENERGIA_INSUFICIENTE":
                return "No opero por energia insuficiente.";

            case "SIN_DATOS":
                return "No opero porque no hay datos en orbita.";

            case "DESBORDAMIENTO":
                return "No opero porque se alcanzaria el limite numerico permitido.";

            default:
                return "Resultado sin descripcion disponible.";
        }
    }

    public void mostrarResumen(ResumenComunicaciones resumen) {
        System.out.println("\n===== RESUMEN DE COMUNICACIONES =====");

        System.out.println(
                "Antenas totales: " + resumen.getTotalModulosTierra());

        System.out.println(
                "Antenas activas: " + resumen.getModulosTierraActivos());

        System.out.println(
                "Capacidad activa maxima: "
                + resumen.getCapacidadActivaTotal() + " MB por ciclo");

        System.out.println(
                "Total historico descargado: "
                + resumen.getTotalHistoricoDescargado() + " MB");

        if (resumen.getMayoresDescargas().isEmpty()) {
            mostrarMensaje(
                    "No hay antenas para identificar una descarga maxima.");
            return;
        }

        System.out.println("Antenas con mayor descarga historica:");

        for (ModuloTierra antena : resumen.getMayoresDescargas()) {
            System.out.println(
                    "ID: " + antena.getId()
                    + " | Nombre: " + antena.getNombre()
                    + " | Estacion: " + antena.getEstacion()
                    + " | Historico descargado: "
                    + antena.getTotalDescargado() + " MB");
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}