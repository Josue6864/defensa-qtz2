package com.mycompany.defensaqtz2;

import com.mycompany.defensaqtz2.controlador.Controlador;
import com.mycompany.defensaqtz2.modelo.Mision;
import com.mycompany.defensaqtz2.modelo.Modulo;
import com.mycompany.defensaqtz2.modelo.ModuloEnergia;
import com.mycompany.defensaqtz2.modelo.ModuloTierra;
import com.mycompany.defensaqtz2.modelo.ModuloVuelo;
import com.mycompany.defensaqtz2.modelo.RecursosMision;
import com.mycompany.defensaqtz2.modelo.TipoInstrumento;
import com.mycompany.defensaqtz2.vista.VistaConsola;
import java.util.ArrayList;
import java.util.List;

/**
  Punto de entrada del programa. Crea la mision inicial, conecta la
  vista con el controlador e inicia el menu.
 */
public class DefensaQTZ2 {

    public static void main(String[] args) {
        try {
            Mision mision = crearMisionInicial();
            VistaConsola vista = new VistaConsola();
            Controlador controlador = new Controlador(mision, vista);
            controlador.iniciar();

        } catch (IllegalArgumentException e) {
            // Una carga invalida se rechaza antes de mostrar el menu (CA13).
            System.out.println("No se pudo iniciar la mision: "
                    + e.getMessage());
        }
    }

    /**
      Construye los diez modulos de la carga inicial, en el orden en que
      participan en cada ciclo, y los recursos con los que arranca la mision.
      Los datos son solo configuracion; las validaciones las hacen los
      constructores y Mision.
     */
    private static Mision crearMisionInicial() {
        List<Modulo> modulos = new ArrayList<>();

        // Parametros: id, nombre, salud, activo, costo, + datos propios
        modulos.add(new ModuloEnergia("E01", "Panel Alfa", 100, true, 1000,
                20, 3));
        modulos.add(new ModuloVuelo("V01", "Camara Norte", 100, true, 2500,
                TipoInstrumento.CAMARA, 15, 8, 3));
        modulos.add(new ModuloTierra("T01", "Antena Campus", 100, true, 1800,
                "Campus Central", 10, 5, 30));
        modulos.add(new ModuloEnergia("E02", "Panel Beta", 100, true, 1200,
                12, 3));
        modulos.add(new ModuloVuelo("V02", "Sensor Termico", 100, true, 1600,
                TipoInstrumento.SENSOR, 8, 4, 3));
        modulos.add(new ModuloTierra("T02", "Antena Laboratorio", 100, true, 2100,
                "Laboratorio", 10, 6, 30));
        modulos.add(new ModuloVuelo("V03", "Camara Sur", 100, true, 2400,
                TipoInstrumento.CAMARA, 10, 6, 3));
        modulos.add(new ModuloEnergia("E03", "Panel Reserva", 50, false, 1400,
                15, 0));
        modulos.add(new ModuloVuelo("V04", "Sensor Respaldo", 45, false, 1500,
                TipoInstrumento.SENSOR, 6, 3, 0));
        modulos.add(new ModuloTierra("T03", "Antena Auxiliar", 100, true, 1700,
                "Campus Norte", 8, 4, 24));

        
        RecursosMision recursos = new RecursosMision(7, 15, 84);

        return new Mision(modulos, recursos, 3);
    }
}