package citasmedicas.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Medico {

    private final int idMedico;
    private final String nombreCompleto;
    private final String especialidad;
    private final String consultorio;
    private final String diasConsulta;
    private final String horario;
    private final LocalDate proximaFecha;
    private final LocalTime proximaHora;

    public Medico(int idMedico, String nombreCompleto, String especialidad, String consultorio,
            String diasConsulta, String horario, LocalDate proximaFecha, LocalTime proximaHora) {
        this.idMedico = idMedico;
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
        this.consultorio = consultorio;
        this.diasConsulta = diasConsulta;
        this.horario = horario;
        this.proximaFecha = proximaFecha;
        this.proximaHora = proximaHora;
    }

    public boolean coincideId(int id) {
        return idMedico == id;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public String getDiasConsulta() {
        return diasConsulta;
    }

    public String getHorario() {
        return horario;
    }

    public LocalDate getProximaFecha() {
        return proximaFecha;
    }

    public LocalTime getProximaHora() {
        return proximaHora;
    }

    @Override
    public String toString() {
        return nombreCompleto + " - " + especialidad;
    }
}
