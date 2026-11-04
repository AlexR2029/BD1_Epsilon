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

public class FormularioDomicilio extends JFrame {

    private long idCandidato;
    private JTextField txtDireccion, txtPais, txtDepartamento, txtMunicipio;
    private JButton btnGuardar;

    public FormularioDomicilio(long idCandidato) {
        this.idCandidato = idCandidato;
        
        setTitle("Registro de Domicilio (ID: " + idCandidato + ")");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel(" ID Candidato:")); 
        JTextField txtIdMostrar = new JTextField(String.valueOf(idCandidato));
        txtIdMostrar.setEditable(false);
        add(txtIdMostrar);

        add(new JLabel(" Dirección:")); txtDireccion = new JTextField(); add(txtDireccion);
        add(new JLabel(" País:")); txtPais = new JTextField("COLOMBIA"); add(txtPais);
        add(new JLabel(" Departamento:")); txtDepartamento = new JTextField(); add(txtDepartamento);
        add(new JLabel(" Municipio:")); txtMunicipio = new JTextField(); add(txtMunicipio);

        add(new JLabel("")); 
        btnGuardar = new JButton("Guardar y Continuar"); 
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarDomicilio());
    }

    private void guardarDomicilio() {
        BaseDatos bd = new BaseDatos();
        Connection conexion = bd.getConexion();
        
        String sql = "INSERT INTO DOMICILIOS_CANDIDATOS (numero_identificacion_candidato, direccion, pais, departamento, municipio) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = conexion.prepareStatement(sql);
            
            pst.setLong(1, this.idCandidato);
            pst.setString(2, txtDireccion.getText().trim());
            pst.setString(3, txtPais.getText().trim());
            pst.setString(4, txtDepartamento.getText().trim());
            pst.setString(5, txtMunicipio.getText().trim());

            int resultado = pst.executeUpdate();
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Domicilio registrado exitosamente.");
                
                this.dispose();
                
                new FormularioLibretaMilitar(this.idCandidato).setVisible(true);
            }
            
            conexion.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar domicilio: " + e.getMessage());
        }
    }
}