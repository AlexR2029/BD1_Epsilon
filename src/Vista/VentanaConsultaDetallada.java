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

    private void cargarCandidatos() {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("Cédula");
    modelo.addColumn("Nombres");
    modelo.addColumn("Apellidos");

    try {
        BaseDatos bd = new BaseDatos();
        Connection con = bd.getConexion();
        Statement st = con.createStatement();
        // Usamos numero_identificacion, nombre y primer_apellido
        ResultSet rs = st.executeQuery("SELECT numero_identificacion, nombre, primer_apellido FROM candidatos");

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

        // 1. Datos básicos del candidato (incluyendo fecha de nacimiento y verificación)
        PreparedStatement pst1 = con.prepareStatement("SELECT * FROM candidatos WHERE numero_identificacion = ?");
        pst1.setLong(1, idCandidato);
        ResultSet rs1 = pst1.executeQuery();
        if (rs1.next()) {
            sb.append("=== HOJA DE VIDA DETALLADA ===\n");
            sb.append("Cédula: ").append(rs1.getLong("numero_identificacion")).append(" (").append(rs1.getString("tipo_identificacion")).append(")\n");
            sb.append("Nombre Completo: ").append(rs1.getString("nombre")).append(" ").append(rs1.getString("primer_apellido"));
            if (rs1.getString("segundo_apellido") != null) {
                sb.append(" ").append(rs1.getString("segundo_apellido"));
            }
            sb.append("\n");
            sb.append("Sexo: ").append(rs1.getString("sexo")).append(" | Nacionalidad: ").append(rs1.getString("nacionalidad")).append("\n");
            sb.append("Fecha de Nacimiento: ").append(rs1.getDate("fecha_nacimiento")).append("\n");
            sb.append("Lugar de Nacimiento: ").append(rs1.getString("municipio_nacimiento")).append(", ").append(rs1.getString("departamento_nacimiento")).append(" (").append(rs1.getString("pais_nacimiento")).append(")\n");
            sb.append("Correo: ").append(rs1.getString("email")).append(" | Teléfono: ").append(rs1.getString("telefono")).append("\n");
            sb.append("Juramento: ").append(rs1.getByte("manifiesta_juramento") == 1 ? "Sí" : "No").append("\n");
            sb.append("Observaciones RRHH: ").append(rs1.getString("observaciones_rrhh") != null ? rs1.getString("observaciones_rrhh") : "Ninguna").append("\n\n");
        }

        // 2. Domicilio del candidato
        PreparedStatement pstDom = con.prepareStatement("SELECT d.direccion, m.nombre AS municipio, m.departamento FROM domicilios_candidatos d JOIN municipios m ON d.id_municipio = m.id_municipio WHERE d.numero_identificacion_candidato = ?");
        pstDom.setLong(1, idCandidato);
        ResultSet rsDom = pstDom.executeQuery();
        sb.append("--- DOMICILIO ---\n");
        if (rsDom.next()) {
            sb.append("• Dirección: ").append(rsDom.getString("direccion"))
              .append(" (").append(rsDom.getString("municipio")).append(", ").append(rsDom.getString("departamento")).append(")\n");
        } else {
            sb.append("• No registra domicilio.\n");
        }
        sb.append("\n");

        // 3. Libreta Militar
        PreparedStatement pstLib = con.prepareStatement("SELECT * FROM libretas_militares WHERE numero_identificacion_candidato = ?");
        pstLib.setLong(1, idCandidato);
        ResultSet rsLib = pstLib.executeQuery();
        sb.append("--- LIBRETA MILITAR ---\n");
        if (rsLib.next()) {
            sb.append("• Número: ").append(rsLib.getLong("numero"))
              .append(" | Clase: ").append(rsLib.getString("clase"))
              .append(" | Distrito: ").append(rsLib.getString("distrito_militar")).append("\n");
        } else {
            sb.append("• No registra libreta militar (o no aplica).\n");
        }
        sb.append("\n");

        // 4. Educación Básica
        PreparedStatement pst2 = con.prepareStatement("SELECT * FROM educacion_basica_candidatos WHERE numero_identificacion_candidato = ?");
        pst2.setLong(1, idCandidato);
        ResultSet rs2 = pst2.executeQuery();
        sb.append("--- EDUCACIÓN BÁSICA ---\n");
        while (rs2.next()) {
            sb.append("• Grado Aprobado: ").append(rs2.getInt("ultimo_grado_aprobado"))
              .append(" | Título: ").append(rs2.getString("titulo_obtenido") != null ? rs2.getString("titulo_obtenido") : "N/A")
              .append(" (Grado: ").append(rs2.getInt("mes_grado")).append("/").append(rs2.getInt("anio_grado")).append(")\n");
        }
        sb.append("\n");

        // 5. Educación Superior
        PreparedStatement pst3 = con.prepareStatement("SELECT * FROM educacion_superior_candidatos WHERE numero_identificacion_candidato = ?");
        pst3.setLong(1, idCandidato);
        ResultSet rs3 = pst3.executeQuery();
        sb.append("--- EDUCACIÓN SUPERIOR ---\n");
        while (rs3.next()) {
            sb.append("• Título: ").append(rs3.getString("titulo_obtenido"))
              .append(" [Modalidad: ").append(rs3.getString("modalidad_academica")).append("]")
              .append(" - Tarjeta Prof: ").append(rs3.getLong("numero_tarjeta_profesional") != 0 ? rs3.getLong("numero_tarjeta_profesional") : "N/A")
              .append(" - Semestres: ").append(rs3.getInt("semestres_aprobados"))
              .append(" - Graduado: ").append(rs3.getBoolean("graduado") ? "Sí" : "No").append("\n");
        }
        sb.append("\n");

        // 6. Idiomas
        PreparedStatement pst4 = con.prepareStatement("SELECT * FROM idiomas_candidatos WHERE numero_identificacion_candidato = ?");
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

        // 7. Experiencia / Ocupaciones
        PreparedStatement pst5 = con.prepareStatement("SELECT * FROM experiencias_ocupaciones_candidatos WHERE numero_identificacion_candidato = ?");
        pst5.setLong(1, idCandidato);
        ResultSet rs5 = pst5.executeQuery();
        sb.append("--- EXPERIENCIA / OCUPACIONES ---\n");
        while (rs5.next()) {
            sb.append("• Ocupación: ").append(rs5.getString("ocupacion"))
              .append(" | Experiencia: ").append(rs5.getInt("anio_experiencia")).append(" años y ")
              .append(rs5.getInt("meses_experiencia")).append(" meses\n");
        }
        sb.append("\n");

        // 8. Empleos Anteriores
        PreparedStatement pstEmp = con.prepareStatement("SELECT * FROM empleos_candidatos WHERE numero_identificacion_candidato = ?");
        pstEmp.setLong(1, idCandidato);
        ResultSet rsEmp = pstEmp.executeQuery();
        sb.append("--- EMPLEOS ANTERIORES ---\n");
        while (rsEmp.next()) {
            sb.append("• Empresa: ").append(rsEmp.getString("nombre_empresa"))
              .append(" | Cargo: ").append(rsEmp.getString("cargo"))
              .append(" | Ingreso: ").append(rsEmp.getDate("fecha_ingreso"))
              .append(" | Retiro: ").append(rsEmp.getDate("fecha_retiro") != null ? rsEmp.getDate("fecha_retiro") : "Actualidad")
              .append(" | Dependencia: ").append(rsEmp.getString("dependencia")).append("\n");
        }

        jTextArea1.setText(sb.toString());
        con.close();
    } catch (Exception e) {
        jTextArea1.setText("Error al cargar el detalle: " + e.getMessage());
    }
}

public VentanaConsultaDetallada() {
    initComponents();
    setSize(850, 650);
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
