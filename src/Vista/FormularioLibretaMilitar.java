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

public class FormularioLibretaMilitar extends JFrame {

    private long idCandidato;
    private JTextField txtNumero, txtDistrito;
    private JComboBox<String> cbClase;
    private JButton btnGuardar, btnOmitir;

    public FormularioLibretaMilitar(long idCandidato) {
        this.idCandidato = idCandidato;
        
        setTitle("Libreta Militar (ID: " + idCandidato + ")");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel(" ID Candidato:")); 
        JTextField txtIdMostrar = new JTextField(String.valueOf(idCandidato));
        txtIdMostrar.setEditable(false);
        add(txtIdMostrar);

        add(new JLabel(" Número de Libreta:")); txtNumero = new JTextField(); add(txtNumero);
        add(new JLabel(" Clase:")); cbClase = new JComboBox<>(new String[]{"PRIMERA", "SEGUNDA"}); add(cbClase);
        add(new JLabel(" Distrito Militar:")); txtDistrito = new JTextField(); add(txtDistrito);

        btnOmitir = new JButton("Omitir (No aplica)"); 
        add(btnOmitir);
        
        btnGuardar = new JButton("Guardar y Continuar"); 
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarLibreta());
        btnOmitir.addActionListener(e -> saltarEducacion());
    }

    private void guardarLibreta() {
        BaseDatos bd = new BaseDatos();
        Connection conexion = bd.getConexion();
        
        String sql = "INSERT INTO LIBRETAS_MILITARES (numero, clase, distrito_militar, numero_identificacion_candidato) "
                   + "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pst = conexion.prepareStatement(sql);
            
            pst.setLong(1, Long.parseLong(txtNumero.getText().trim()));
            pst.setString(2, cbClase.getSelectedItem().toString());
            pst.setString(3, txtDistrito.getText().trim());
            pst.setLong(4, this.idCandidato);

            int resultado = pst.executeUpdate();
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Libreta Militar registrada exitosamente.");
                saltarEducacion(); 
            }
            
            conexion.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar libreta: " + e.getMessage());
        }
    }
    
    private void saltarEducacion() {
        this.dispose();
        new FormularioEducacion(this.idCandidato).setVisible(true);
    }
}