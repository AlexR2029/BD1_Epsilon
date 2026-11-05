/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;
import BaseDatos.BaseDatos;
import Modelo.Idioma;
import Modelo.NivelCompetencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
/**
 *
 * @author pablo
 */
public class FormularioIdioma extends javax.swing.JFrame {

private long numeroIdentificacionCandidato;

    // Constructor que recibe la cédula del candidato
    public FormularioIdioma(long numeroIdentificacionCandidato) {
        this.numeroIdentificacionCandidato = numeroIdentificacionCandidato;
        initComponents();
        setLocationRelativeTo(null);

        // Llenar los ComboBox de niveles con las opciones R, B, MB
        configurarComboBoxes();
    }

    private void configurarComboBoxes() {
        javax.swing.JComboBox[] combos = {jComboBox1, jComboBox2, jComboBox3}; // Asumiendo que estos son tus 3 ComboBox de niveles
        for (javax.swing.JComboBox combo : combos) {
            combo.removeAllItems();
            combo.addItem("R");
            combo.addItem("B");
            combo.addItem("MB");
        }
    }

    private boolean registrarIdiomaBD() {
        try {
            String nombreIdioma = jTextField1.getText().trim().toUpperCase();
            
            if (nombreIdioma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del idioma.", "Aviso", JOptionPane.WARNING_MESSAGE);
                jTextField1.requestFocus();
                return false;
            }

            // Validar la regla de la base de datos que prohíbe el español
            if (nombreIdioma.equals("ESPAÑOL") || nombreIdioma.equals("SPANISH")) {
                JOptionPane.showMessageDialog(this, "El script de la base de datos prohíbe registrar el español como idioma extranjero.", "Validación", JOptionPane.WARNING_MESSAGE);
                jTextField1.requestFocus();
                return false;
            }

            // Obtener los niveles seleccionados (R, B, MB)
            String hablaStr = jComboBox1.getSelectedItem().toString();
            String lecturaStr = jComboBox2.getSelectedItem().toString();
            String escrituraStr = jComboBox3.getSelectedItem().toString();

            NivelCompetencia habla = NivelCompetencia.valueOf(hablaStr);
            NivelCompetencia lectura = NivelCompetencia.valueOf(lecturaStr);
            NivelCompetencia escritura = NivelCompetencia.valueOf(escrituraStr);

            // Crear objeto del modelo del equipo
            Idioma idioma = new Idioma(nombreIdioma, habla, lectura, escritura);

            // Conexión y centencia SQL para IDIOMAS_CANDIDATOS
            BaseDatos bd = new BaseDatos();
            Connection conexion = bd.getConexion();

            String sql = "INSERT INTO IDIOMAS_CANDIDATOS (numero_identificacion_candidato, nombre, habla, lectura, escritura) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conexion.prepareStatement(sql);
            pst.setLong(1, numeroIdentificacionCandidato);
            pst.setString(2, idioma.getNombreIdioma());
            pst.setString(3, idioma.getHabla().name());
            pst.setString(4, idioma.getLectura().name());
            pst.setString(5, idioma.getEscritura().name());

            pst.executeUpdate();
            conexion.close();
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el idioma en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void limpiarFormulario() {
        jTextField1.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
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
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Especifique los idiomas diferentes al ESPAÑOL que domine ");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Regular (R), Bien (B), Muy Bien (MB)");

        jLabel3.setText("NOMBRE DEL IDIOMA");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Califique sus habilidades");

        jLabel5.setText("Lo habla");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "R", "B", "MB" }));

        jLabel6.setText("Lo lee");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "R", "B", "MB" }));

        jLabel7.setText("Lo escribe");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "R", "B", "MB" }));

        jButton1.setText("Agregar otro idioma");
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
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (registrarIdiomaBD()) {
            JOptionPane.showMessageDialog(this, "¡Idioma agregado con éxito! Puede registrar otro.");
            limpiarFormulario();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!jTextField1.getText().trim().isEmpty()) {
            if (registrarIdiomaBD()) {
                JOptionPane.showMessageDialog(this, "¡Proceso de registro de idioma realizado con éxito!");
                this.dispose();
                new FormularioExperienciaLaboral(numeroIdentificacionCandidato).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Proceso de registro de idioma realizado con éxito!");
            this.dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormularioIdioma(0L).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
