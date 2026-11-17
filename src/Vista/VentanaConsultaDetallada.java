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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
            ResultSet rs = st.executeQuery("SELECT numero_identificacion, nombre, "
                    + "CONCAT(primer_apellido, ' ', IFNULL(segundo_apellido, '')) AS apellidos "
                    + "FROM CANDIDATOS ORDER BY primer_apellido, nombre");

            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getLong(1);
                fila[1] = rs.getString("nombre");
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
        try (Connection con = new BaseDatos().getConexion()) {
            if (con == null) {
                jTextArea1.setText("No fue posible conectarse a la base de datos.");
                return;
            }

            // Datos personales, lugar de nacimiento, domicilio y libreta
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT c.*, m.nombre AS mun_nac, m.departamento AS dep_nac, m.pais AS pais_nac, "
                    + "d.direccion, md.nombre AS mun_dom, md.departamento AS dep_dom, md.pais AS pais_dom, "
                    + "lm.numero AS libreta, lm.clase AS clase_libreta, lm.distrito_militar "
                    + "FROM candidatos c "
                    + "JOIN municipios m ON m.id_municipio = c.id_municipio_nacimiento "
                    + "LEFT JOIN domicilios_candidatos d ON d.numero_identificacion_candidato = c.numero_identificacion "
                    + "LEFT JOIN municipios md ON md.id_municipio = d.id_municipio "
                    + "LEFT JOIN libretas_militares lm ON lm.numero_identificacion_candidato = c.numero_identificacion "
                    + "WHERE c.numero_identificacion = ?")) {
                pst.setLong(1, idCandidato);
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        jTextArea1.setText("No existe la hoja de vida " + idCandidato + ".");
                        return;
                    }
                    sb.append("=== HOJA DE VIDA ===\n");
                    sb.append("Documento: ").append(rs.getString("tipo_identificacion"))
                      .append(" ").append(rs.getLong("numero_identificacion")).append("\n");
                    sb.append("Nombre completo: ").append(rs.getString("nombre")).append(" ")
                      .append(rs.getString("primer_apellido"));
                    String segundo = rs.getString("segundo_apellido");
                    if (segundo != null) sb.append(" ").append(segundo);
                    sb.append("\n");
                    sb.append("Sexo: ").append(rs.getString("sexo"))
                      .append(" | Nacionalidad: ").append(rs.getString("nacionalidad")).append("\n");
                    sb.append("Fecha de nacimiento: ").append(rs.getDate("fecha_nacimiento")).append("\n");
                    sb.append("Lugar de nacimiento: ").append(rs.getString("mun_nac")).append(" - ")
                      .append(rs.getString("dep_nac")).append(" (").append(rs.getString("pais_nac")).append(")\n");
                    sb.append("Correo: ").append(rs.getString("email"))
                      .append(" | Teléfono: ").append(rs.getString("telefono")).append("\n");
                    sb.append("Manifiesta juramento: ").append(rs.getBoolean("manifiesta_juramento") ? "Sí" : "No")
                      .append(" | Verificada: ").append(rs.getBoolean("verificada") ? "Sí" : "No").append("\n");
                    String observaciones = rs.getString("observaciones_rrhh");
                    if (observaciones != null) sb.append("Observaciones RRHH: ").append(observaciones).append("\n");

                    sb.append("\n--- DOMICILIO ---\n");
                    if (rs.getString("direccion") == null) {
                        sb.append("No registrado\n");
                    } else {
                        sb.append(rs.getString("direccion")).append(", ").append(rs.getString("mun_dom"))
                          .append(" - ").append(rs.getString("dep_dom"))
                          .append(" (").append(rs.getString("pais_dom")).append(")\n");
                    }

                    sb.append("\n--- LIBRETA MILITAR ---\n");
                    if (rs.getString("libreta") == null) {
                        sb.append("No registra\n");
                    } else {
                        sb.append("Número ").append(rs.getString("libreta"))
                          .append(" | Clase ").append(rs.getString("clase_libreta"))
                          .append(" | Distrito ").append(rs.getString("distrito_militar")).append("\n");
                    }
                }
            }

            sb.append("\n--- EDUCACIÓN BÁSICA ---\n");
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM educacion_basica_candidatos WHERE numero_identificacion_candidato = ?")) {
                pst.setLong(1, idCandidato);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        sb.append("• Último grado aprobado: ").append(rs.getInt("ultimo_grado_aprobado"))
                          .append(" | Título: ").append(rs.getString("titulo_obtenido"))
                          .append(" | Terminación: ").append(rs.getInt("mes_grado"))
                          .append("/").append(rs.getInt("anio_grado")).append("\n");
                    }
                }
            }

            sb.append("\n--- EDUCACIÓN SUPERIOR ---\n");
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM educacion_superior_candidatos WHERE numero_identificacion_candidato = ?")) {
                pst.setLong(1, idCandidato);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        sb.append("• Título: ").append(rs.getString("titulo_obtenido"))
                          .append(" | Modalidad: ").append(rs.getString("modalidad_academica"))
                          .append(" | Semestres: ").append(rs.getInt("semestres_aprobados"))
                          .append(" | Graduado: ").append(rs.getBoolean("graduado") ? "Sí" : "No")
                          .append(" | Tarjeta profesional: ").append(rs.getString("numero_tarjeta_profesional"))
                          .append(" | Terminación: ").append(rs.getInt("mes_terminacion"))
                          .append("/").append(rs.getInt("anio_terminacion")).append("\n");
                    }
                }
            }

            sb.append("\n--- IDIOMAS ---\n");
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM idiomas_candidatos WHERE numero_identificacion_candidato = ?")) {
                pst.setLong(1, idCandidato);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        sb.append("• ").append(rs.getString("nombre"))
                          .append(" | Habla: ").append(rs.getString("habla"))
                          .append(", Lee: ").append(rs.getString("lectura"))
                          .append(", Escribe: ").append(rs.getString("escritura")).append("\n");
                    }
                }
            }

            sb.append("\n--- EXPERIENCIA LABORAL ---\n");
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT ec.cargo, ec.dependencia, ec.fecha_ingreso, ec.fecha_retiro, "
                    + "e.nombre, e.tipo_empresa, e.email_empresa, e.telefono, e.direccion AS dir_empresa, "
                    + "m.nombre AS mun, m.departamento AS dep, m.pais "
                    + "FROM empleos_candidatos ec "
                    + "JOIN empresas e ON e.nombre = ec.nombre_empresa "
                    + "JOIN municipios m ON m.id_municipio = e.id_municipio "
                    + "WHERE ec.numero_identificacion_candidato = ? ORDER BY ec.fecha_ingreso")) {
                pst.setLong(1, idCandidato);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        sb.append("• Empresa: ").append(rs.getString("nombre"))
                          .append(" (").append(rs.getString("tipo_empresa")).append(") en ")
                          .append(rs.getString("mun")).append(" - ").append(rs.getString("dep"))
                          .append(" (").append(rs.getString("pais")).append(")\n")
                          .append("  Correo: ").append(rs.getString("email_empresa"))
                          .append(" | Teléfono: ").append(rs.getString("telefono"))
                          .append(" | Dirección: ").append(rs.getString("dir_empresa")).append("\n")
                          .append("  Cargo: ").append(rs.getString("cargo"))
                          .append(" | Dependencia: ").append(rs.getString("dependencia"))
                          .append(" | Ingreso: ").append(rs.getDate("fecha_ingreso"))
                          .append(" | Retiro: ").append(rs.getDate("fecha_retiro") == null ? "vigente" : rs.getDate("fecha_retiro").toString())
                          .append("\n");
                    }
                }
            }

            sb.append("\n--- OCUPACIONES ---\n");
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM experiencias_ocupaciones_candidatos WHERE numero_identificacion_candidato = ?")) {
                pst.setLong(1, idCandidato);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        sb.append("• Ocupación: ").append(rs.getString("ocupacion"))
                          .append(" | Experiencia: ").append(rs.getInt("anio_experiencia")).append(" años y ")
                          .append(rs.getInt("meses_experiencia")).append(" meses\n");
                    }
                }
            }

            sb.append("\n--- VERIFICACIONES ---\n");
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT v.fecha_verificacion, v.aprobada, v.observaciones, p.nombre_completo "
                    + "FROM verificaciones_hoja_vida v "
                    + "JOIN personal_rrhh p ON p.numero_identificacion = v.identificacion_verificador "
                    + "WHERE v.numero_identificacion_candidato = ? ORDER BY v.fecha_verificacion")) {
                pst.setLong(1, idCandidato);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        sb.append("• ").append(rs.getTimestamp("fecha_verificacion"))
                          .append(" | Verificador: ").append(rs.getString("nombre_completo"))
                          .append(" | ").append(rs.getBoolean("aprobada") ? "Aprobada" : "No aprobada");
                        String obs = rs.getString("observaciones");
                        if (obs != null && !obs.isEmpty()) sb.append(" | ").append(obs);
                        sb.append("\n");
                    }
                }
            }

            jTextArea1.setText(sb.toString());
            jTextArea1.setCaretPosition(0);
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
