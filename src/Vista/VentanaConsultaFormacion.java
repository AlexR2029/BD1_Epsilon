package Vista;

import BaseDatos.BaseDatos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class VentanaConsultaFormacion extends javax.swing.JFrame {

    public VentanaConsultaFormacion() {
        initComponents();
        setSize(850, 500); // <-- Asegura un tamaño inicial visible para evitar la pantalla gris
        setLocationRelativeTo(null);
        cargarDatosFormacion();
    }

    private void cargarDatosFormacion() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre Completo");
        modelo.addColumn("Correo");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Grado Básica");
        modelo.addColumn("Título Básica");
        modelo.addColumn("Año Básica");
        modelo.addColumn("Título Superior");
        modelo.addColumn("Modalidad");
        modelo.addColumn("Semestres");
        modelo.addColumn("Graduado");

        jTable1.setModel(modelo);

        String sql = "SELECT c.numero_identificacion, "
                + "CONCAT(c.nombre, ' ', c.primer_apellido, ' ', IFNULL(c.segundo_apellido, '')) AS nombre_completo, "
                + "c.email, c.telefono, "
                + "eb.ultimo_grado_aprobado, eb.titulo_obtenido AS titulo_basica, eb.anio_grado, "
                + "es.titulo_obtenido AS titulo_superior, es.modalidad_academica, es.semestres_aprobados, es.graduado "
                + "FROM candidatos c "
                + "LEFT JOIN educacion_basica_candidatos eb ON c.numero_identificacion = eb.numero_identificacion_candidato "
                + "LEFT JOIN educacion_superior_candidatos es ON c.numero_identificacion = es.numero_identificacion_candidato";

        try {
            BaseDatos bd = new BaseDatos();
            Connection con = bd.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getLong("numero_identificacion"),
                    rs.getString("nombre_completo"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getObject("ultimo_grado_aprobado") != null ? rs.getObject("ultimo_grado_aprobado") : "N/A",
                    rs.getString("titulo_basica") != null ? rs.getString("titulo_basica") : "N/A",
                    rs.getObject("anio_grado") != null ? rs.getObject("anio_grado") : "N/A",
                    rs.getString("titulo_superior") != null ? rs.getString("titulo_superior") : "Sin registro",
                    rs.getString("modalidad_academica") != null ? rs.getString("modalidad_academica") : "",
                    rs.getObject("semestres_aprobados") != null ? rs.getObject("semestres_aprobados") : "",
                    rs.getObject("graduado") == null ? "" : (rs.getBoolean("graduado") ? "Sí" : "No")
                });
            }

            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cédula", "Nombre completo", "Correo", "Teléfono", "Grado básica", "Titulo básica", "Año básica", "Titulo superior", "Modalidad", "Semestres", "Graduado"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
