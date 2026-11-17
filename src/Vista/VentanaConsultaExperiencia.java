package Vista;

import BaseDatos.BaseDatos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class VentanaConsultaExperiencia extends javax.swing.JFrame {

    public VentanaConsultaExperiencia() {
        initComponents();
        setSize(850, 500); // <-- Asegura un tamaño inicial visible para evitar la pantalla gris
        setLocationRelativeTo(null);
        cargarDatosExperiencia();
    }

    private void cargarDatosExperiencia() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre Completo");
        modelo.addColumn("Correo");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Empresa");
        modelo.addColumn("Tipo Empresa");
        modelo.addColumn("Cargo");
        modelo.addColumn("Dependencia");
        modelo.addColumn("Fecha Ingreso");
        modelo.addColumn("Fecha Retiro");

        jTable1.setModel(modelo);

        String sql = "SELECT c.numero_identificacion, "
                + "CONCAT(c.nombre, ' ', c.primer_apellido, ' ', IFNULL(c.segundo_apellido, '')) AS nombre_completo, "
                + "c.email, c.telefono, "
                + "ec.nombre_empresa, e.tipo_empresa, ec.cargo, ec.dependencia, "
                + "ec.fecha_ingreso, ec.fecha_retiro "
                + "FROM candidatos c "
                + "LEFT JOIN empleos_candidatos ec ON c.numero_identificacion = ec.numero_identificacion_candidato "
                + "LEFT JOIN empresas e ON ec.nombre_empresa = e.nombre";

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
                    rs.getString("nombre_empresa") != null ? rs.getString("nombre_empresa") : "Sin registro",
                    rs.getString("tipo_empresa") != null ? rs.getString("tipo_empresa") : "",
                    rs.getString("cargo") != null ? rs.getString("cargo") : "",
                    rs.getString("dependencia") != null ? rs.getString("dependencia") : "",
                    rs.getDate("fecha_ingreso") != null ? rs.getDate("fecha_ingreso") : "",
                    rs.getDate("fecha_retiro") != null ? rs.getDate("fecha_retiro") : "Actualidad"
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
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cédula", "Nombre completo", "Correo", "Teléfono", "Empresa", "Tipo empresa", "Cargo", "Dependencia", "Fecha ingreso", "Fecha retiro"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}