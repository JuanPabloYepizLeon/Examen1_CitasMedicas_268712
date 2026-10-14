package citasmedicas.controlador;

import citasmedicas.modelo.ModeloCitas;

public class ControladorCita {

    private final ModeloCitas modelo;

    public ControladorCita(ModeloCitas modelo) {
        this.modelo = modelo;
    }

    public void buscarPaciente(String nss) {
        modelo.buscarPaciente(nss);
    }

    public void seleccionarMedico(int idMedico) {
        modelo.seleccionarMedico(idMedico);
    }

    public void apartarCita() {
        modelo.registrarCita();
    }
}
