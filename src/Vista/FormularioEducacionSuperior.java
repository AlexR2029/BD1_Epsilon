/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;
import BaseDatos.BaseDatos;
import Modelo.EducacionSuperior;
import Modelo.ModalidadAcademica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
/**
 *
 * @author pablo
 */
public class FormularioEducacionSuperior extends javax.swing.JFrame {

    private long numeroIdentificacionCandidato;

    public FormularioEducacionSuperior(long numeroIdentificacionCandidato) {
        this.numeroIdentificacionCandidato = numeroIdentificacionCandidato;
        initComponents();
        setLocationRelativeTo(null);

        ButtonGroup grupoGraduado = new ButtonGroup();
        grupoGraduado.add(jRadioButton1);
        grupoGraduado.add(jRadioButton2);
        jRadioButton1.setSelected(true);

        jComboBox1.removeAllItems();
        jComboBox1.addItem("TC");
        jComboBox1.addItem("TL");
        jComboBox1.addItem("TE");
        jComboBox1.addItem("UN");
        jComboBox1.addItem("ES");
        jComboBox1.addItem("MG");
        jComboBox1.addItem("DOC");
    }

    private boolean registrarEstudio() {
        try {
            String titulo = jTextField1.getText().trim();
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del título es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                jTextField1.requestFocus();
                return false;
            }

            String modalidadStr = jComboBox1.getSelectedItem().toString();
            ModalidadAcademica modalidad = ModalidadAcademica.valueOf(modalidadStr);

            String semestresStr = jTextField2.getText().trim();
            if (semestresStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar los semestres aprobados.", "Aviso", JOptionPane.WARNING_MESSAGE);
                jTextField2.requestFocus();
                return false;
            }
            int semestres = Integer.parseInt(semestresStr);

            boolean graduado = jRadioButton1.isSelected(); 

            Integer mes = jTextField3.getText().trim().isEmpty() ? null : Integer.parseInt(jTextField3.getText().trim());
            Integer anio = jTextField4.getText().trim().isEmpty() ? null : Integer.parseInt(jTextField4.getText().trim());

            String tarjetaStr = jTextField5.getText().trim();
            String tarjetaProfesional = tarjetaStr.isEmpty() ? null : tarjetaStr;

            EducacionSuperior eduSup = new EducacionSuperior(titulo, mes, anio, modalidad, semestres, graduado, tarjetaProfesional);

            BaseDatos bd = new BaseDatos();
            Connection conexion = bd.getConexion();

            String sql = "INSERT INTO EDUCACION_SUPERIOR_CANDIDATOS (numero_identificacion_candidato, titulo_obtenido, numero_tarjeta_profesional, modalidad_academica, semestres_aprobados, graduado, mes_terminacion, anio_terminacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conexion.prepareStatement(sql);
            pst.setLong(1, numeroIdentificacionCandidato);
            pst.setString(2, eduSup.getTituloObtenido());
            
            if (eduSup.getNumeroTarjetaProfesional() != null && !eduSup.getNumeroTarjetaProfesional().isEmpty()) {
                pst.setLong(3, Long.parseLong(eduSup.getNumeroTarjetaProfesional()));
            } else {
                pst.setNull(3, java.sql.Types.BIGINT);
            }
            
            pst.setString(4, eduSup.getModalidadAcademica().name());
            pst.setInt(5, eduSup.getSemestresAprobados());
            pst.setBoolean(6, eduSup.isGraduado());

            if (eduSup.getMesTerminacion() != null) {
                pst.setInt(7, eduSup.getMesTerminacion());
            } else {
                pst.setNull(7, java.sql.Types.TINYINT);
            }

            if (eduSup.getAnioTerminacion() != null) {
                pst.setInt(8, eduSup.getAnioTerminacion());
            } else {
                pst.setNull(8, java.sql.Types.SMALLINT);
            }

            pst.executeUpdate();
            conexion.close();
            return true;

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Por favor verifique que los campos numéricos (Semestres, Mes, Año, Tarjeta) contengan valores válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    private void limpiarFormulario() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jComboBox1.setSelectedIndex(0);
        jRadioButton1.setSelected(true);
        jTextField1.requestFocus();
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Diligencie este punto en estricto orden cronológico");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("TC (Técnica), TL (Tecnólogica), TE (Tecnólogica Especializada) UN (Universitaria), ES (Especialización), MG (Maestría O Magister), DOC (Doctorado O PHD)");

        jLabel3.setText("NOMBRE DE LOS ESTUDIOS O TÍTULO OBTENIDO");

        jLabel4.setText("MODALIDAD ACADÉMICA");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TC", "TL", "TE", "UN", "ES", "MG", "DOG" }));

        jLabel5.setText("SEMESTRES APROVADOS");

        jLabel6.setText("¿GRADUADO?");

        jRadioButton1.setText("Si");

        jRadioButton2.setText("No");

        jLabel7.setText("FECHA DE TERMINACIÓN");

        jLabel8.setText("Mes:");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel9.setText("Año:");

        jLabel10.setText("NÚMERO DE TARJETA PROFECIONAL");

        jButton1.setText("Agregar otra formación");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Continuar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(1, 1, 1)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(jLabel9)
                                .addGap(1, 1, 1)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (registrarEstudio()) {
            JOptionPane.showMessageDialog(this, "¡Título superior registrado con éxito! Puede ingresar otro.");
            limpiarFormulario();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!jTextField1.getText().trim().isEmpty()) {
            if (registrarEstudio()) {
                JOptionPane.showMessageDialog(this, "¡Formación superior registrada con éxito!");
                this.dispose();
                new FormularioIdioma(numeroIdentificacionCandidato).setVisible(true);

            }
        } else {
            this.dispose();
            new FormularioIdioma(numeroIdentificacionCandidato).setVisible(true);
        }     
    }//GEN-LAST:event_jButton2ActionPerformed

public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormularioEducacionSuperior(0L).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
