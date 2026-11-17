package Vista;

import BaseDatos.BaseDatos;
import BaseDatos.MunicipioDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Formulario independiente para registrar un municipio. */
public class FormularioMunicipio extends JFrame {

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDepartamento = new JTextField();
    private final JTextField txtPais = new JTextField("COLOMBIA");

    public FormularioMunicipio() {
        setTitle("Registrar municipio");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel campos = new JPanel(new GridLayout(0, 1, 5, 5));
        campos.add(new JLabel("NOMBRE DEL MUNICIPIO"));
        campos.add(txtNombre);
        campos.add(new JLabel("DEPARTAMENTO"));
        campos.add(txtDepartamento);
        campos.add(new JLabel("PAÍS"));
        campos.add(txtPais);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnCerrar = new JButton("Cerrar");
        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnGuardar);
        botones.add(btnLimpiar);
        botones.add(btnCerrar);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        raiz.add(campos, BorderLayout.CENTER);
        raiz.add(botones, BorderLayout.SOUTH);

        setContentPane(raiz);
        setSize(380, 260);
        setLocationRelativeTo(null);
    }

    private void limpiar() {
        txtNombre.setText("");
        txtDepartamento.setText("");
        txtPais.setText("COLOMBIA");
        txtNombre.requestFocus();
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String depto = txtDepartamento.getText().trim();
        String pais = txtPais.getText().trim();

        if (nombre.isEmpty() || depto.isEmpty() || pais.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, departamento y país son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nombre.length() > 50 || depto.length() > 50 || pais.length() > 50) {
            JOptionPane.showMessageDialog(this, "Cada campo admite máximo 50 caracteres.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conexion = new BaseDatos().getConexion()) {
            if (conexion == null) return; // BaseDatos ya mostró el error de conexión
            MunicipioDAO dao = new MunicipioDAO(conexion);

            List<MunicipioDAO.Municipio> parecidos = dao.buscarParecidos(nombre);
            for (MunicipioDAO.Municipio m : parecidos) {
                if (m.nombre().equalsIgnoreCase(nombre) && m.departamento().equalsIgnoreCase(depto)) {
                    JOptionPane.showMessageDialog(this, "Ese municipio ya existe: " + m, "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            int id = SelectorMunicipio.resolver(this, dao, nombre, depto, pais);
            boolean eraExistente = parecidos.stream().anyMatch(m -> m.id() == id);
            JOptionPane.showMessageDialog(this, eraExistente
                    ? "Se seleccionó un municipio que ya existía (id " + id + "); no se creó uno nuevo."
                    : "Municipio registrado (id " + id + ").");
            limpiar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el municipio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormularioMunicipio().setVisible(true));
    }
}
