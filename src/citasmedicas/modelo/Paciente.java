package citasmedicas.modelo;

public class Paciente {

    private final String nss;
    private final String nombre;
    private final String clinica;

    public Paciente(String nss, String nombre, String clinica) {
        this.nss = nss;
        this.nombre = nombre;
        this.clinica = clinica;
    }

    public boolean coincideNss(String nss) {
        return this.nss.equals(nss);
    }

    public String getNss() {
        return nss;
    }

    public String getNombre() {
        return nombre;
    }

    public String getClinica() {
        return clinica;
    }
}
