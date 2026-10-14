package citasmedicas.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModeloCitas extends Sujeto {

    private final List<Paciente> pacientes = new ArrayList<>();
    private final List<Medico> medicos = new ArrayList<>();
    private Paciente pacienteActual;
    private Medico medicoSeleccionado;
    private Cita citaActual;
    private Estado estado;
    private String mensaje;

    public ModeloCitas() {
        cargarDatosIniciales();
        estado = Estado.SOLICITANDO_NSS;
        mensaje = "";
    }

    private void cargarDatosIniciales() {
        pacientes.add(new Paciente("12345678901", "María Fernanda López Ruiz", "Clínica 1, Cd. Obregón"));
        pacientes.add(new Paciente("98765432109", "Juan Carlos Pérez Gómez", "Clínica 2, Cd. Obregón"));

        medicos.add(new Medico(1, "Dr. Carlos Ramírez Soto", "Medicina General", "101",
                "Lunes, miércoles y viernes", "9:00 a 13:00 h",
                LocalDate.of(2026, 10, 19), LocalTime.of(9, 30)));
        medicos.add(new Medico(2, "Dra. Laura Méndez Ibarra", "Pediatría", "204",
                "Martes y jueves", "10:00 a 15:00 h",
                LocalDate.of(2026, 10, 20), LocalTime.of(10, 30)));
        medicos.add(new Medico(3, "Dr. Jorge Navarro Peña", "Cardiología", "305",
                "Lunes a jueves", "14:00 a 18:00 h",
                LocalDate.of(2026, 10, 19), LocalTime.of(14, 30)));
    }

    public void buscarPaciente(String nss) {
        String buscado = (nss == null) ? "" : nss.trim();
        pacienteActual = null;
        medicoSeleccionado = null;
        citaActual = null;
        for (Paciente p : pacientes) {
            if (p.coincideNss(buscado)) {
                pacienteActual = p;
                break;
            }
        }
        if (pacienteActual != null) {
            estado = Estado.LISTA_MEDICOS;
            mensaje = "";
        } else {
            estado = Estado.NSS_INVALIDO;
            mensaje = "El Número de Seguridad Social no existe o tiene un formato inválido. "
                    + "Verifíquelo e intente de nuevo.";
        }
        notificarObservadores();
    }

    public void seleccionarMedico(int idMedico) {
        for (Medico m : medicos) {
            if (m.coincideId(idMedico)) {
                medicoSeleccionado = m;
                citaActual = null;
                estado = Estado.MEDICO_SELECCIONADO;
                mensaje = "";
                notificarObservadores();
                return;
            }
        }
    }

    public void registrarCita() {
        if (medicoSeleccionado == null) {
            estado = Estado.SIN_MEDICO;
            mensaje = "Debe seleccionar un médico antes de apartar la cita.";
        } else {
            citaActual = new Cita(pacienteActual, medicoSeleccionado);
            estado = Estado.CITA_REGISTRADA;
            mensaje = "Cita registrada correctamente";
        }
        notificarObservadores();
    }

    public Estado getEstado() {
        return estado;
    }

    public Paciente getPacienteActual() {
        return pacienteActual;
    }

    public List<Medico> getMedicos() {
        return Collections.unmodifiableList(medicos);
    }

    public Medico getMedicoSeleccionado() {
        return medicoSeleccionado;
    }

    public Cita getCitaActual() {
        return citaActual;
    }

    public String getMensaje() {
        return mensaje;
    }
}
