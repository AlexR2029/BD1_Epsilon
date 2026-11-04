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

public class FormularioEducacion extends JFrame {

    private long idCandidato;
    private JTextField txtGrado, txtTitulo, txtMes, txtAnio;
    private JButton btnGuardar;

    public FormularioEducacion(long idCandidato) {
        this.idCandidato = idCandidato;
        
        setTitle("Educación Básica (ID: " + idCandidato + ")");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel(" ID Candidato:")); 
        JTextField txtIdMostrar = new JTextField(String.valueOf(idCandidato));
        txtIdMostrar.setEditable(false);
        add(txtIdMostrar);

        add(new JLabel(" Último Grado (1-11):")); txtGrado = new JTextField(); add(txtGrado);
        add(new JLabel(" Título Obtenido:")); txtTitulo = new JTextField("Bachiller Académico"); add(txtTitulo);
        add(new JLabel(" Mes de Grado (1-12):")); txtMes = new JTextField(); add(txtMes);
        add(new JLabel(" Año de Grado (Ej: 2015):")); txtAnio = new JTextField(); add(txtAnio);

        add(new JLabel("")); 
        btnGuardar = new JButton("Guardar y Continuar"); 
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarEducacion());
    }

    private void guardarEducacion() {
        BaseDatos bd = new BaseDatos();
        Connection conexion = bd.getConexion();
        
        String sql = "INSERT INTO EDUCACION_BASICA_CANDIDATOS "
                   + "(numero_identificacion_candidato, ultimo_grado_aprobado, titulo_obtenido, mes_grado, anio_grado) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = conexion.prepareStatement(sql);
            
            pst.setLong(1, this.idCandidato);
            pst.setInt(2, Integer.parseInt(txtGrado.getText().trim()));
            pst.setString(3, txtTitulo.getText().trim());
            pst.setInt(4, Integer.parseInt(txtMes.getText().trim()));
            pst.setInt(5, Integer.parseInt(txtAnio.getText().trim()));

            int resultado = pst.executeUpdate();
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Educación registrada exitosamente.");
                
                this.dispose();
                
                new FormularioEducacionSuperior(this.idCandidato).setVisible(true);
            }
            
            conexion.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar educación: " + e.getMessage());
        }
    }
}