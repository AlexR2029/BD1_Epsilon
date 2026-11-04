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

public class FormularioEducacionSuperior extends JFrame {

    private long idCandidato;
    private JTextField txtTitulo, txtTarjeta, txtSemestres, txtMes, txtAnio;
    private JComboBox<String> cbModalidad;
    private JCheckBox chkGraduado;
    private JButton btnGuardar, btnAgregarOtro, btnOmitir;

    public FormularioEducacionSuperior(long idCandidato) {
        this.idCandidato = idCandidato;
        
        setTitle("Educación Superior (ID: " + idCandidato + ")");
        setSize(450, 450); // Un poco más alto para los nuevos botones
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel(" ID Candidato:")); 
        JTextField txtIdMostrar = new JTextField(String.valueOf(idCandidato));
        txtIdMostrar.setEditable(false);
        add(txtIdMostrar);

        add(new JLabel(" Título Obtenido:")); txtTitulo = new JTextField(); add(txtTitulo);
        
        add(new JLabel(" Modalidad Académica:")); 
        cbModalidad = new JComboBox<>(new String[]{"TC", "TL", "TE", "UN", "ES", "MG", "DOC"}); 
        add(cbModalidad);
        
        add(new JLabel(" Semestres Aprobados:")); txtSemestres = new JTextField(); add(txtSemestres);
        add(new JLabel(" ¿Graduado?:")); chkGraduado = new JCheckBox(); add(chkGraduado);
        add(new JLabel(" Tarjeta Prof. (Opcional):")); txtTarjeta = new JTextField(); add(txtTarjeta);
        add(new JLabel(" Mes Terminado (Opcional):")); txtMes = new JTextField(); add(txtMes);
        add(new JLabel(" Año Terminado (Opcional):")); txtAnio = new JTextField(); add(txtAnio);

        btnOmitir = new JButton("Omitir (No aplica)"); 
        add(btnOmitir);
        
        btnAgregarOtro = new JButton("Guardar y Agregar Otro"); 
        add(btnAgregarOtro);

        add(new JLabel("")); // Espacio en blanco para empujar el botón a la derecha
        btnGuardar = new JButton("Guardar y Continuar"); 
        add(btnGuardar);

        btnAgregarOtro.addActionListener(e -> {
            if(registrarEnBD()) {
                JOptionPane.showMessageDialog(this, "Título guardado. Puedes ingresar el siguiente.");
                limpiarCampos();
            }
        });
        
        btnGuardar.addActionListener(e -> {
            if(registrarEnBD()) {
                JOptionPane.showMessageDialog(this, "Último título guardado exitosamente.");
                saltarIdiomas();
            }
        });
        
        btnOmitir.addActionListener(e -> saltarIdiomas());
    }

    private boolean registrarEnBD() {
        if(txtTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Título Obtenido no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        BaseDatos bd = new BaseDatos();
        Connection conexion = bd.getConexion();
        boolean exito = false;
        
        String sql = "INSERT INTO EDUCACION_SUPERIOR_CANDIDATOS "
                   + "(numero_identificacion_candidato, titulo_obtenido, numero_tarjeta_profesional, "
                   + "modalidad_academica, semestres_aprobados, graduado, mes_terminacion, anio_terminacion) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = conexion.prepareStatement(sql);
            
            pst.setLong(1, this.idCandidato);
            pst.setString(2, txtTitulo.getText().trim());
            
            String tarjeta = txtTarjeta.getText().trim();
            if(tarjeta.isEmpty()) pst.setNull(3, java.sql.Types.BIGINT);
            else pst.setLong(3, Long.parseLong(tarjeta));
            
            pst.setString(4, cbModalidad.getSelectedItem().toString());
            pst.setInt(5, Integer.parseInt(txtSemestres.getText().trim()));
            pst.setBoolean(6, chkGraduado.isSelected());
            
            String mes = txtMes.getText().trim();
            if(mes.isEmpty()) pst.setNull(7, java.sql.Types.TINYINT);
            else pst.setInt(7, Integer.parseInt(mes));
            
            String anio = txtAnio.getText().trim();
            if(anio.isEmpty()) pst.setNull(8, java.sql.Types.SMALLINT);
            else pst.setInt(8, Integer.parseInt(anio));

            if (pst.executeUpdate() > 0) {
                exito = true;
            }
            conexion.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
        return exito;
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        cbModalidad.setSelectedIndex(0);
        txtSemestres.setText("");
        chkGraduado.setSelected(false);
        txtTarjeta.setText("");
        txtMes.setText("");
        txtAnio.setText("");
        txtTitulo.requestFocus();
    }
    
    private void saltarIdiomas() {
        this.dispose();
        new FormularioIdiomas(this.idCandidato).setVisible(true);
    }
}