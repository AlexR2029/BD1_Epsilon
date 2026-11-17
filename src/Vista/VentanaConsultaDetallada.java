/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import BaseDatos.BaseDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pablo 
 */

public class VentanaConsultaDetallada extends javax.swing.JFrame {

    public VentanaConsultaDetallada() {
        initComponents();
        setLocationRelativeTo(null);
        cargarCandidatos();

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                int fila = jTable1.getSelectedRow();
                long idCandidato = Long.parseLong(jTable1.getValueAt(fila, 0).toString());
                cargarDetalleCompleto(idCandidato);
            }
        });
    }

    private void cargarCandidatos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");

        try {
            BaseDatos bd = new BaseDatos();
            Connection con = bd.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT numero_identificacion_candidato, nombres, apellidos FROM CANDIDATOS");

            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getLong(1);
                fila[1] = rs.getString(2);
                fila[2] = rs.getString(3);
                modelo.addRow(fila);
            }
            jTable1.setModel(modelo);
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar candidatos: " + e.getMessage());
        }
    }

    private void cargarDetalleCompleto(long idCandidato) {
        StringBuilder sb = new StringBuilder();
        try {
            BaseDatos bd = new BaseDatos();
            Connection con = bd.getConexion();

            PreparedStatement pst1 = con.prepareStatement("SELECT * FROM CANDIDATOS WHERE numero_identificacion_candidato = ?");
            pst1.setLong(1, idCandidato);
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                sb.append("=== HOJA DE VIDA DETALLADA ===\n");
                sb.append("Cédula: ").append(rs1.getLong("numero_identificacion_candidato")).append("\n");
                sb.append("Nombre Completo: ").append(rs1.getString("nombres")).append(" ").append(rs1.getString("apellidos")).append("\n");
                sb.append("Correo: ").append(rs1.getString("correo_electronico")).append("\n");
                sb.append("Teléfono: ").append(rs1.getString("telefono")).append("\n\n");
            }

            PreparedStatement pst2 = con.prepareStatement("SELECT * FROM EDUCACION_BASICA_CANDIDATOS WHERE numero_identificacion_candidato = ?");
            pst2.setLong(1, idCandidato);
            ResultSet rs2 = pst2.executeQuery();
            sb.append("--- EDUCACIÓN BÁSICA ---\n");
            while (rs2.next()) {
                sb.append("• Grado: ").append(rs2.getInt("grado_aprobado"))
                  .append(" | Título: ").append(rs2.getString("titulo_obtenido"))
                  .append(" (Terminación: ").append(rs2.getInt("mes_terminacion")).append("/").append(rs2.getInt("anio_terminacion")).append(")\n");
            }
            sb.append("\n");

            PreparedStatement pst3 = con.prepareStatement("SELECT * FROM EDUCACION_SUPERIOR_CANDIDATOS WHERE numero_identificacion_candidato = ?");
            pst3.setLong(1, idCandidato);
            ResultSet rs3 = pst3.executeQuery();
            sb.append("--- EDUCACIÓN SUPERIOR ---\n");
            while (rs3.next()) {
                sb.append("• Título: ").append(rs3.getString("titulo_obtenido"))
                  .append(" [Modalidad: ").append(rs3.getString("modalidad_academica")).append("]")
                  .append(" - Semestres: ").append(rs3.getInt("semestres_aprobados"))
                  .append(" - Graduado: ").append(rs3.getBoolean("graduado") ? "Sí" : "No").append("\n");
            }
            sb.append("\n");

            PreparedStatement pst4 = con.prepareStatement("SELECT * FROM IDIOMAS_CANDIDATOS WHERE numero_identificacion_candidato = ?");
            pst4.setLong(1, idCandidato);
            ResultSet rs4 = pst4.executeQuery();
            sb.append("--- IDIOMAS ---\n");
            while (rs4.next()) {
                sb.append("• Idioma: ").append(rs4.getString("nombre"))
                  .append(" | Habla: ").append(rs4.getString("habla"))
                  .append(", Lee: ").append(rs4.getString("lectura"))
                  .append(", Escribe: ").append(rs4.getString("escritura")).append("\n");
            }
            sb.append("\n");

            PreparedStatement pst5 = con.prepareStatement("SELECT * FROM EXPERIENCIAS_OCUPACIONES_CANDIDATOS WHERE numero_identificacion_candidato = ?");
            pst5.setLong(1, idCandidato);
            ResultSet rs5 = pst5.executeQuery();
            sb.append("--- EXPERIENCIA / OCUPACIONES ---\n");
            while (rs5.next()) {
                sb.append("• Ocupación: ").append(rs5.getString("ocupacion"))
                  .append(" | Experiencia: ").append(rs5.getInt("anio_experiencia")).append(" años y ")
                  .append(rs5.getInt("meses_experiencia")).append(" meses\n");
            }

            jTextArea1.setText(sb.toString());
            con.close();
        } catch (Exception e) {
            jTextArea1.setText("Error al cargar el detalle: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Cédula", "Nombres", "Apellidos"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
