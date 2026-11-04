/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista; 
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import BaseDatos.BaseDatos;

public class FormularioIdiomas extends JFrame {

    private long idCandidato;
    private JTextField txtNombreIdioma;
    private JComboBox<String> cbHabla, cbLectura, cbEscritura;
    private JButton btnGuardar, btnAgregarOtro, btnOmitir;

    public FormularioIdiomas(long idCandidato) {
        this.idCandidato = idCandidato;
        
        setTitle("Idiomas (ID: " + idCandidato + ")");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel(" ID Candidato:")); 
        JTextField txtIdMostrar = new JTextField(String.valueOf(idCandidato));
        txtIdMostrar.setEditable(false);
        add(txtIdMostrar);

        add(new JLabel(" Nombre Idioma (No Español):")); 
        txtNombreIdioma = new JTextField(); 
        add(txtNombreIdioma);
        
        String[] niveles = {"R", "B", "MB"};
        
        add(new JLabel(" Nivel Hablado:")); cbHabla = new JComboBox<>(niveles); add(cbHabla);
        add(new JLabel(" Nivel Lectura:")); cbLectura = new JComboBox<>(niveles); add(cbLectura);
        add(new JLabel(" Nivel Escritura:")); cbEscritura = new JComboBox<>(niveles); add(cbEscritura);

        btnOmitir = new JButton("Omitir (No aplica)"); 
        add(btnOmitir);
        
        btnAgregarOtro = new JButton("Guardar y Agregar Otro"); 
        add(btnAgregarOtro);

        add(new JLabel("")); // Espaciador
        btnGuardar = new JButton("Guardar y Continuar"); 
        add(btnGuardar);

        btnAgregarOtro.addActionListener(e -> {
            if(registrarEnBD()) {
                JOptionPane.showMessageDialog(this, "Idioma guardado. Puedes ingresar otro.");
                limpiarCampos();
            }
        });
        
        btnGuardar.addActionListener(e -> {
            if(registrarEnBD()) {
                JOptionPane.showMessageDialog(this, "Último idioma guardado exitosamente.");
                saltarExperiencia();
            }
        });
        
        btnOmitir.addActionListener(e -> saltarExperiencia());
    }

    private boolean registrarEnBD() {
        String idioma = txtNombreIdioma.getText().trim().toUpperCase();
        
        if (idioma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del idioma no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (idioma.equals("ESPAÑOL") || idioma.equals("ESPANOL")) {
            JOptionPane.showMessageDialog(this, "La base de datos no permite registrar 'Español'.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        BaseDatos bd = new BaseDatos();
        Connection conexion = bd.getConexion();
        boolean exito = false;
        
        String sql = "INSERT INTO IDIOMAS_CANDIDATOS (numero_identificacion_candidato, nombre, habla, lectura, escritura) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = conexion.prepareStatement(sql);
            pst.setLong(1, this.idCandidato);
            pst.setString(2, idioma);
            pst.setString(3, cbHabla.getSelectedItem().toString());
            pst.setString(4, cbLectura.getSelectedItem().toString());
            pst.setString(5, cbEscritura.getSelectedItem().toString());

            if (pst.executeUpdate() > 0) {
                exito = true;
            }
            conexion.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar idioma: " + e.getMessage());
        }
        return exito;
    }

    private void limpiarCampos() {
        txtNombreIdioma.setText("");
        cbHabla.setSelectedIndex(0);
        cbLectura.setSelectedIndex(0);
        cbEscritura.setSelectedIndex(0);
        txtNombreIdioma.requestFocus();
    }
    
    private void saltarExperiencia() {
        this.dispose();
        new FormularioExperiencia(this.idCandidato).setVisible(true);
    }
    
}