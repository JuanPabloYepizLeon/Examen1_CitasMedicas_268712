package citasmedicas.vista;

import citasmedicas.controlador.ControladorCita;
import citasmedicas.modelo.ModeloCitas;
import citasmedicas.modelo.Observador;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VistaNSS extends JFrame implements Observador {

    private final ModeloCitas modelo;
    private final ControladorCita controlador;
    private JTextField campoNSS;
    private JButton botonContinuar;
    private JLabel etiquetaError;

    public VistaNSS(ModeloCitas modelo, ControladorCita controlador) {
        this.modelo = modelo;
        this.controlador = controlador;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setTitle("Sistema de Citas Médicas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel encabezado = new JLabel("  Sistema de Citas Médicas - Registrar cita");
        encabezado.setOpaque(true);
        encabezado.setBackground(new Color(0x1E3A8A));
        encabezado.setForeground(Color.WHITE);
        encabezado.setFont(encabezado.getFont().deriveFont(Font.BOLD, 18f));
        encabezado.setPreferredSize(new Dimension(640, 50));
        encabezado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        getContentPane().add(encabezado);

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JLabel titulo = new JLabel("Bienvenido");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        JLabel indicacion = new JLabel("Ingrese su Número de Seguridad Social para registrar una cita médica.");
        JLabel etiquetaCampo = new JLabel("Número de Seguridad Social");
        etiquetaCampo.setFont(etiquetaCampo.getFont().deriveFont(Font.BOLD));

        campoNSS = new JTextField();
        campoNSS.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        campoNSS.addActionListener(e -> alContinuar());

        etiquetaError = new JLabel(" ");
        etiquetaError.setForeground(new Color(0xDC2626));

        botonContinuar = new JButton("Continuar");
        botonContinuar.addActionListener(e -> alContinuar());

        titulo.setAlignmentX(LEFT_ALIGNMENT);
        indicacion.setAlignmentX(LEFT_ALIGNMENT);
        etiquetaCampo.setAlignmentX(LEFT_ALIGNMENT);
        campoNSS.setAlignmentX(LEFT_ALIGNMENT);
        etiquetaError.setAlignmentX(LEFT_ALIGNMENT);
        botonContinuar.setAlignmentX(LEFT_ALIGNMENT);

        formulario.add(titulo);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(indicacion);
        formulario.add(Box.createVerticalStrut(20));
        formulario.add(etiquetaCampo);
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(campoNSS);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(etiquetaError);
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(botonContinuar);
        getContentPane().add(formulario);
    }

    public void mostrar() {
        setVisible(true);
    }

    public void ocultar() {
        setVisible(false);
    }

    @Override
    public void actualizar() {
        switch (modelo.getEstado()) {
            case NSS_INVALIDO:
                etiquetaError.setText("<html><body style='width:380px'>" + modelo.getMensaje() + "</body></html>");
                campoNSS.setBorder(BorderFactory.createLineBorder(new Color(0xDC2626), 2));
                break;
            case LISTA_MEDICOS:
                ocultar();
                break;
            default:
                break;
        }
    }

    public void alContinuar() {
        controlador.buscarPaciente(campoNSS.getText());
    }
}
