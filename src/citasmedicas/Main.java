package citasmedicas;

import citasmedicas.controlador.ControladorCita;
import citasmedicas.modelo.ModeloCitas;
import citasmedicas.vista.VistaCita;
import citasmedicas.vista.VistaNSS;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModeloCitas modelo = new ModeloCitas();
            ControladorCita controlador = new ControladorCita(modelo);
            VistaNSS vistaNSS = new VistaNSS(modelo, controlador);
            VistaCita vistaCita = new VistaCita(modelo, controlador);

            modelo.agregarObservador(vistaNSS);
            modelo.agregarObservador(vistaCita);

            vistaNSS.mostrar();
        });
    }

}
