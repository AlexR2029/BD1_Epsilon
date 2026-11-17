package Vista;

import BaseDatos.BaseDatos;
import BaseDatos.MunicipioDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Formulario independiente para registrar una empresa. */
public class FormularioEmpresa extends JFrame {

    private final JTextField txtNombre = new JTextField();
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"PUBLICA", "PRIVADA"});
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtDireccion = new JTextField();
    private final JComboBox<MunicipioDAO.Municipio> cbMunicipio = new JComboBox<>();

    public FormularioEmpresa() {
        setTitle("Registrar empresa");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel campos = new JPanel(new GridLayout(0, 1, 5, 5));
        campos.add(new JLabel("NOMBRE DE LA EMPRESA"));
        campos.add(txtNombre);
        campos.add(new JLabel("TIPO"));
        campos.add(cbTipo);
        campos.add(new JLabel("EMAIL"));
        campos.add(txtEmail);
        campos.add(new JLabel("TELÉFONO"));
        campos.add(txtTelefono);
        campos.add(new JLabel("DIRECCIÓN"));
        campos.add(txtDireccion);
        campos.add(new JLabel("MUNICIPIO"));

        JPanel municipio = new JPanel(new BorderLayout(5, 0));
        municipio.add(cbMunicipio, BorderLayout.CENTER);
        JButton btnNuevoMunicipio = new JButton("Nuevo municipio");
        btnNuevoMunicipio.addActionListener(e -> abrirNuevoMunicipio());
        municipio.add(btnNuevoMunicipio, BorderLayout.EAST);
        campos.add(municipio);

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
        raiz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        raiz.add(campos, BorderLayout.CENTER);
        raiz.add(botones, BorderLayout.SOUTH);

        setContentPane(raiz);
        setSize(420, 400);
        setLocationRelativeTo(null);
        cargarMunicipios();
    }

    private void cargarMunicipios() {
        cbMunicipio.removeAllItems();
        try (Connection conexion = new BaseDatos().getConexion()) {
            if (conexion == null) return; // BaseDatos ya mostró el error de conexión
            for (MunicipioDAO.Municipio m : new MunicipioDAO(conexion).listar()) {
                cbMunicipio.addItem(m); // se muestra "Cali - Valle del Cauca"
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los municipios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirNuevoMunicipio() {
        FormularioMunicipio f = new FormularioMunicipio();
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) { cargarMunicipios(); }
        });
        f.setVisible(true);
    }

    private void limpiar() {
        txtNombre.setText("");
        cbTipo.setSelectedIndex(0);
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        if (cbMunicipio.getItemCount() > 0) cbMunicipio.setSelectedIndex(0);
        txtNombre.requestFocus();
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        MunicipioDAO.Municipio municipio = (MunicipioDAO.Municipio) cbMunicipio.getSelectedItem();

        if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || municipio == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios y debe haber un municipio registrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nombre.length() > 20 || email.length() > 50 || telefono.length() > 12 || direccion.length() > 30) {
            JOptionPane.showMessageDialog(this, "Máximos: nombre 20, email 50, teléfono 12 y dirección 30 caracteres.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "El email no es válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO EMPRESAS (nombre, tipo_empresa, pais, departamento, municipio, "
                + "email_empresa, telefono, direccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = new BaseDatos().getConexion()) {
            if (conexion == null) return; // BaseDatos ya mostró el error de conexión
            try (PreparedStatement pst = conexion.prepareStatement(sql)) {
                pst.setString(1, nombre);
                pst.setString(2, cbTipo.getSelectedItem().toString());
                pst.setString(3, municipio.pais());
                pst.setString(4, municipio.departamento());
                pst.setString(5, municipio.nombre());
                pst.setString(6, email);
                pst.setString(7, telefono);
                pst.setString(8, direccion);
                pst.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Empresa registrada.");
            limpiar();
        } catch (SQLException e) {
            String msg = e.getErrorCode() == 1062
                    ? "Esa empresa ya está registrada."
                    : "Error al guardar la empresa: " + e.getMessage();
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la empresa: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormularioEmpresa().setVisible(true));
    }
}
