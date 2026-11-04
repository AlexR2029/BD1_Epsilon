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

public class FormularioCandidato extends JFrame {

    // Campos de texto y selectores
    private JTextField txtId, txtNombre, txtApellido1, txtApellido2, txtTelefono, txtEmail, txtFechaNac, txtPais, txtDep, txtMun;
    private JComboBox<String> cbTipoId, cbSexo, cbNacionalidad;
    private JCheckBox chkJuramento;
    private JButton btnGuardar;

    public FormularioCandidato() {
        setTitle("Registro Básico de Candidato");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel(" Número Identificación:")); txtId = new JTextField(); add(txtId);
        add(new JLabel(" Tipo ID:")); cbTipoId = new JComboBox<>(new String[]{"CC", "CE", "PAS"}); add(cbTipoId);
        add(new JLabel(" Nombre:")); txtNombre = new JTextField(); add(txtNombre);
        add(new JLabel(" Primer Apellido:")); txtApellido1 = new JTextField(); add(txtApellido1);
        add(new JLabel(" Segundo Apellido:")); txtApellido2 = new JTextField(); add(txtApellido2);
        add(new JLabel(" Sexo:")); cbSexo = new JComboBox<>(new String[]{"F", "M"}); add(cbSexo);
        add(new JLabel(" Nacionalidad:")); cbNacionalidad = new JComboBox<>(new String[]{"COLOMBIA", "EXTRANJERO"}); add(cbNacionalidad);
        add(new JLabel(" Teléfono:")); txtTelefono = new JTextField(); add(txtTelefono);
        add(new JLabel(" Email:")); txtEmail = new JTextField(); add(txtEmail);
        add(new JLabel(" Fecha Nac (YYYY-MM-DD):")); txtFechaNac = new JTextField(); add(txtFechaNac);
        add(new JLabel(" País Nacimiento:")); txtPais = new JTextField("COLOMBIA"); add(txtPais);
        add(new JLabel(" Departamento Nac:")); txtDep = new JTextField(); add(txtDep);
        add(new JLabel(" Municipio Nac:")); txtMun = new JTextField(); add(txtMun);
        add(new JLabel(" Manifiesta Juramento:")); chkJuramento = new JCheckBox(); add(chkJuramento);

        add(new JLabel("")); 
        btnGuardar = new JButton("Guardar y Continuar"); 
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarDatos());
    }

    private void guardarDatos() {
        BaseDatos con = new BaseDatos();
        Connection conexion = con.getConexion();
        
        String sql = "INSERT INTO CANDIDATOS (numero_identificacion, tipo_identificacion, nombre, primer_apellido, "
                   + "segundo_apellido, sexo, nacionalidad, telefono, email, manifiesta_juramento, observaciones_rrhh, "
                   + "fecha_nacimiento, pais_nacimiento, departamento_nacimiento, municipio_nacimiento) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = conexion.prepareStatement(sql);
            
            pst.setLong(1, Long.parseLong(txtId.getText().trim()));
            pst.setString(2, cbTipoId.getSelectedItem().toString());
            pst.setString(3, txtNombre.getText().trim());
            pst.setString(4, txtApellido1.getText().trim());
            pst.setString(5, txtApellido2.getText().trim().isEmpty() ? null : txtApellido2.getText().trim());
            pst.setString(6, cbSexo.getSelectedItem().toString());
            pst.setString(7, cbNacionalidad.getSelectedItem().toString());
            pst.setString(8, txtTelefono.getText().trim());
            pst.setString(9, txtEmail.getText().trim());
            pst.setBoolean(10, chkJuramento.isSelected());
            pst.setString(11, txtFechaNac.getText().trim());
            pst.setString(12, txtPais.getText().trim());
            pst.setString(13, txtDep.getText().trim());
            pst.setString(14, txtMun.getText().trim());

            int resultado = pst.executeUpdate();
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Candidato registrado exitosamente.");
                
                long id = Long.parseLong(txtId.getText().trim()); 
                
                this.dispose(); 
                
                new FormularioDomicilio(id).setVisible(true);
            }
            
            conexion.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormularioCandidato().setVisible(true));
    }
}