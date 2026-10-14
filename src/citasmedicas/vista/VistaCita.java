package citasmedicas.vista;

import citasmedicas.controlador.ControladorCita;
import citasmedicas.modelo.Cita;
import citasmedicas.modelo.Medico;
import citasmedicas.modelo.ModeloCitas;
import citasmedicas.modelo.Observador;
import citasmedicas.modelo.Paciente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class VistaCita extends JFrame implements Observador {

    private final ModeloCitas modelo;
    private final ControladorCita controlador;
    private JList<Medico> listaMedicos;
    private JPanel panelPaciente;
    private JPanel panelDerecha;
    private JButton botonApartar;
    private boolean actualizando = false;

    public VistaCita(ModeloCitas modelo, ControladorCita controlador) {
        this.modelo = modelo;
        this.controlador = controlador;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setTitle("Sistema de Citas Médicas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920, 580);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(16, 16));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel encabezado = new JLabel("  Sistema de Citas Médicas - Registrar cita");
        encabezado.setOpaque(true);
        encabezado.setBackground(new Color(0x1E3A8A));
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(encabezado.getFont().deriveFont(Font.BOLD, 18f));
        encabezado.setPreferredSize(new Dimension(920, 50));
        getContentPane().add(encabezado, BorderLayout.NORTH);

        JPanel zonaIzquierda = new JPanel();
        zonaIzquierda.setLayout(new BoxLayout(zonaIzquierda, BoxLayout.Y_AXIS));
        zonaIzquierda.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        zonaIzquierda.setPreferredSize(new Dimension(360, 400));

        panelPaciente = new JPanel(new BorderLayout());
        panelPaciente.setBorder(BorderFactory.createTitledBorder("Datos del paciente"));
        panelPaciente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        listaMedicos = new JList<>(new DefaultListModel<Medico>());
        listaMedicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMedicos.setFixedCellHeight(34);
        listaMedicos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !actualizando && listaMedicos.getSelectedValue() != null) {
                alSeleccionarMedico();
            }
        });
        JScrollPane scrollLista = new JScrollPane(listaMedicos);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Médicos disponibles"));

        zonaIzquierda.add(panelPaciente);
        zonaIzquierda.add(Box.createVerticalStrut(12));
        zonaIzquierda.add(scrollLista);
        getContentPane().add(zonaIzquierda, BorderLayout.WEST);

        panelDerecha = new JPanel();
        panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));
        panelDerecha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 16),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x2563EB), 2),
                        BorderFactory.createEmptyBorder(16, 16, 16, 16))));
        getContentPane().add(panelDerecha, BorderLayout.CENTER);

        botonApartar = new JButton("Apartar cita");
        botonApartar.setAlignmentX(LEFT_ALIGNMENT);
        botonApartar.addActionListener(e -> alApartarCita());
    }

    public void mostrar() {
        setVisible(true);
    }

    @Override
    public void actualizar() {
        switch (modelo.getEstado()) {
            case LISTA_MEDICOS:
                mostrarListaMedicos();
                break;
            case MEDICO_SELECCIONADO:
                mostrarDatosMedico();
                break;
            case SIN_MEDICO:
                mostrarAviso();
                break;
            case CITA_REGISTRADA:
                mostrarDatosCita();
                break;
            default:
                break;
        }
    }

    public void alSeleccionarMedico() {
        Medico elegido = listaMedicos.getSelectedValue();
        if (elegido != null) {
            controlador.seleccionarMedico(elegido.getIdMedico());
        }
    }

    public void alApartarCita() {
        controlador.apartarCita();
    }

    private void mostrarListaMedicos() {
        Paciente p = modelo.getPacienteActual();
        panelPaciente.removeAll();
        panelPaciente.add(new JLabel("<html><table>"
                + "<tr><td><b>NSS</b></td><td>" + p.getNss() + "</td></tr>"
                + "<tr><td><b>Nombre</b></td><td>" + p.getNombre() + "</td></tr>"
                + "<tr><td><b>Clínica</b></td><td>" + p.getClinica() + "</td></tr>"
                + "</table></html>"), BorderLayout.CENTER);

        actualizando = true;
        DefaultListModel<Medico> lista = new DefaultListModel<>();
        for (Medico m : modelo.getMedicos()) {
            lista.addElement(m);
        }
        listaMedicos.setModel(lista);
        listaMedicos.clearSelection();
        actualizando = false;

        panelDerecha.removeAll();
        panelDerecha.add(titulo("Datos del médico", new Color(0x1E3A8A)));
        panelDerecha.add(izquierda(new JLabel("Seleccione un médico de la lista para ver sus datos.")));
        panelDerecha.add(Box.createVerticalStrut(12));
        panelDerecha.add(botonApartar);
        refrescar();
        mostrar();
    }

    private void mostrarDatosMedico() {
        Medico m = modelo.getMedicoSeleccionado();
        actualizando = true;
        listaMedicos.setSelectedValue(m, true);
        actualizando = false;

        panelDerecha.removeAll();
        panelDerecha.add(titulo("Datos del médico", new Color(0x1E3A8A)));
        panelDerecha.add(izquierda(new JLabel("<html><table cellpadding='3'>"
                + "<tr><td><b>Nombre</b></td><td>" + m.getNombreCompleto() + "</td></tr>"
                + "<tr><td><b>Especialidad</b></td><td>" + m.getEspecialidad() + "</td></tr>"
                + "<tr><td><b>Consultorio</b></td><td>" + m.getConsultorio() + "</td></tr>"
                + "<tr><td><b>Días de consulta</b></td><td>" + m.getDiasConsulta() + "</td></tr>"
                + "<tr><td><b>Horario</b></td><td>" + m.getHorario() + "</td></tr>"
                + "</table></html>")));
        panelDerecha.add(Box.createVerticalStrut(12));
        panelDerecha.add(botonApartar);
        refrescar();
    }

    private void mostrarDatosCita() {
        Cita c = modelo.getCitaActual();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX"));
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("H:mm 'h'");
        String fecha = c.getFecha().format(formatoFecha);
        fecha = fecha.substring(0, 1).toUpperCase() + fecha.substring(1);

        panelDerecha.removeAll();
        panelDerecha.add(titulo("Datos de la cita", new Color(0x15803D)));
        panelDerecha.add(izquierda(new JLabel("<html><table cellpadding='3'>"
                + "<tr><td><b>Paciente</b></td><td>" + c.getPaciente().getNombre()
                + " (NSS " + c.getPaciente().getNss() + ")</td></tr>"
                + "<tr><td><b>Médico</b></td><td>" + c.getMedico().getNombreCompleto() + "</td></tr>"
                + "<tr><td><b>Consultorio</b></td><td>" + c.getMedico().getConsultorio() + "</td></tr>"
                + "<tr><td><b>Fecha</b></td><td>" + fecha + "</td></tr>"
                + "<tr><td><b>Hora</b></td><td>" + c.getHora().format(formatoHora) + "</td></tr>"
                + "</table></html>")));
        panelDerecha.add(Box.createVerticalStrut(12));
        JLabel exito = new JLabel(" " + modelo.getMensaje() + " ");
        exito.setOpaque(true);
        exito.setBackground(new Color(0xDCFCE7));
        exito.setForeground(new Color(0x15803D));
        exito.setBorder(BorderFactory.createLineBorder(new Color(0x16A34A)));
        panelDerecha.add(izquierda(exito));
        refrescar();
    }

    private void mostrarAviso() {
        panelDerecha.removeAll();
        panelDerecha.add(titulo("Datos del médico", new Color(0x1E3A8A)));
        JLabel aviso = new JLabel(" " + modelo.getMensaje() + " ");
        aviso.setOpaque(true);
        aviso.setBackground(new Color(0xFEE2E2));
        aviso.setForeground(new Color(0xB91C1C));
        aviso.setBorder(BorderFactory.createLineBorder(new Color(0xDC2626)));
        panelDerecha.add(izquierda(aviso));
        panelDerecha.add(Box.createVerticalStrut(12));
        panelDerecha.add(botonApartar);
        refrescar();
    }

    private JLabel titulo(String texto, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 16f));
        l.setForeground(color);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JLabel izquierda(JLabel l) {
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private void refrescar() {
        panelDerecha.revalidate();
        panelDerecha.repaint();
        panelPaciente.revalidate();
        panelPaciente.repaint();
    }
}
