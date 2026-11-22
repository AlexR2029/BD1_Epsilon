/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import BaseDatos.HojaVidaDAO;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Lista de candidatos y detalle de su hoja de vida. Según la consulta muestra todas las secciones
 * o solo las que necesita (formación académica o experiencia laboral).
 *
 * @author pablo
 */

public class VentanaConsultaDetallada extends javax.swing.JFrame {

    /** Secciones del detalle. PERSONAL incluye los datos personales y el domicilio. */
    enum Seccion { PERSONAL, LIBRETA, BASICA, SUPERIOR, IDIOMAS, EXPERIENCIA, OCUPACIONES, VERIFICACIONES }

    private final Set<Seccion> secciones;

    /** Consulta detallada completa, con todos los candidatos. */
    public VentanaConsultaDetallada() {
        this("Hojas de vida detalladas - seleccione una para ver el detalle", null, EnumSet.allOf(Seccion.class));
    }

    /** Formación académica del candidato indicado: datos personales, educación básica y superior, e idiomas. */
    public static VentanaConsultaDetallada formacion(long identificacion) {
        return new VentanaConsultaDetallada("Formación académica - seleccione una para ver el detalle", identificacion,
                EnumSet.of(Seccion.PERSONAL, Seccion.BASICA, Seccion.SUPERIOR, Seccion.IDIOMAS));
    }

    /** Experiencia laboral del candidato indicado: datos personales, experiencia laboral y ocupaciones. */
    public static VentanaConsultaDetallada experiencia(long identificacion) {
        return new VentanaConsultaDetallada("Experiencia laboral - seleccione una para ver el detalle", identificacion,
                EnumSet.of(Seccion.PERSONAL, Seccion.EXPERIENCIA, Seccion.OCUPACIONES));
    }

    private VentanaConsultaDetallada(String titulo, Long idInicial, Set<Seccion> secciones) {
        this.secciones = secciones;
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle(titulo);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTextArea1.setEditable(false);
        cargarCandidatos(idInicial);

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                int fila = jTable1.getSelectedRow();
                long idCandidato = Long.parseLong(jTable1.getValueAt(fila, 0).toString());
                cargarDetalle(idCandidato);
            }
        });
        if (idInicial != null) {
            seleccionar(idInicial);
        } else if (jTable1.getRowCount() > 0) {
            jTable1.setRowSelectionInterval(0, 0); // muestra de una vez el detalle del primero
        }
    }

    private void seleccionar(long identificacion) {
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (String.valueOf(jTable1.getValueAt(i, 0)).equals(String.valueOf(identificacion))) {
                jTable1.setRowSelectionInterval(i, i);
                jTable1.scrollRectToVisible(jTable1.getCellRect(i, 0, true));
                return;
            }
        }
        jTextArea1.setText("No existe la hoja de vida " + identificacion + ".");
    }

    private void cargarCandidatos(Long idFiltro) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        for (String columna : new String[]{"Identificación", "Tipo", "Nombre completo", "Sexo", "Nacionalidad",
                "Nacimiento", "Lugar de nacimiento", "Correo", "Teléfono", "Domicilio", "Libreta", "Verificada"}) {
            modelo.addColumn(columna);
        }

        try {
            for (Map<String, Object> fila : new HojaVidaDAO().consultarCandidatosDetallados()) {
                // con idFiltro (formación/experiencia) solo se muestra el candidato consultado
                if (idFiltro != null && !String.valueOf(fila.get("numero_identificacion")).equals(String.valueOf(idFiltro))) {
                    continue;
                }
                modelo.addRow(new Object[]{
                    fila.get("numero_identificacion"), fila.get("tipo_identificacion"),
                    fila.get("nombre_completo"), fila.get("sexo"), fila.get("nacionalidad"),
                    fila.get("fecha_nacimiento"), fila.get("lugar"), fila.get("email"),
                    fila.get("telefono"), fila.get("domicilio"), fila.get("libreta"),
                    ((Number) fila.get("verificada")).intValue() == 1 ? "Sí" : "No"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las hojas de vida: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        jTable1.setModel(modelo);
    }

    private void cargarDetalle(long idCandidato) {
        StringBuilder sb = new StringBuilder();
        try {
            HojaVidaDAO dao = new HojaVidaDAO();
            List<Map<String, Object>> personal = dao.consultarDetallePersonal(idCandidato);
            if (personal.isEmpty()) {
                jTextArea1.setText("No existe la hoja de vida " + idCandidato + ".");
                return;
            }
            Map<String, Object> p = personal.get(0);
            if (secciones.contains(Seccion.PERSONAL)) {
                sb.append("=== HOJA DE VIDA ===\n");
                sb.append("Documento: ").append(p.get("tipo_identificacion")).append(" ").append(p.get("numero_identificacion")).append("\n");
                sb.append("Nombre completo: ").append(p.get("nombre")).append(" ").append(p.get("primer_apellido"));
                if (p.get("segundo_apellido") != null) sb.append(" ").append(p.get("segundo_apellido"));
                sb.append("\nSexo: ").append(p.get("sexo")).append(" | Nacionalidad: ").append(p.get("nacionalidad")).append("\n");
                sb.append("Fecha de nacimiento: ").append(p.get("fecha_nacimiento")).append("\n");
                sb.append("Lugar de nacimiento: ").append(p.get("mun_nac")).append(" - ").append(p.get("dep_nac"))
                  .append(" (").append(p.get("pais_nac")).append(")\n");
                sb.append("Correo: ").append(p.get("email")).append(" | Teléfono: ").append(p.get("telefono")).append("\n");
                sb.append("Manifiesta juramento: ").append(Boolean.TRUE.equals(p.get("manifiesta_juramento")) ? "Sí" : "No")
                  .append(" | Verificada: ").append(((Number) p.get("verificada")).intValue() == 1 ? "Sí" : "No").append("\n");
                if (p.get("observaciones_rrhh") != null) sb.append("Observaciones RRHH: ").append(p.get("observaciones_rrhh")).append("\n");

                sb.append("\n--- DOMICILIO ---\n");
                if (p.get("direccion") == null) sb.append("No registrado\n");
                else sb.append(p.get("direccion")).append(", ").append(p.get("mun_dom")).append(" - ")
                    .append(p.get("dep_dom")).append(" (").append(p.get("pais_dom")).append(")\n");
            }
            if (secciones.contains(Seccion.LIBRETA)) {
                sb.append("\n--- LIBRETA MILITAR ---\n");
                if (p.get("libreta") == null) sb.append("No registra\n");
                else sb.append("Número ").append(p.get("libreta")).append(" | Clase ").append(p.get("clase_libreta"))
                    .append(" | Distrito ").append(p.get("distrito_militar")).append("\n");
            }
            if (secciones.contains(Seccion.BASICA)) {
                sb.append("\n--- EDUCACIÓN BÁSICA ---\n");
                for (Map<String, Object> r : dao.consultarEducacionBasica(idCandidato)) {
                    sb.append("• Último grado aprobado: ").append(r.get("ultimo_grado_aprobado"))
                      .append(" | Título: ").append(r.get("titulo_obtenido")).append(" | Terminación: ")
                      .append(r.get("mes_grado")).append("/").append(r.get("anio_grado")).append("\n");
                }
            }
            if (secciones.contains(Seccion.SUPERIOR)) {
                sb.append("\n--- EDUCACIÓN SUPERIOR ---\n");
                for (Map<String, Object> r : dao.consultarEducacionSuperior(idCandidato)) {
                    sb.append("• Título: ").append(r.get("titulo_obtenido")).append(" | Modalidad: ")
                      .append(r.get("modalidad_academica")).append(" | Semestres: ").append(r.get("semestres_aprobados"))
                      .append(" | Graduado: ").append(Boolean.TRUE.equals(r.get("graduado")) ? "Sí" : "No")
                      .append(" | Tarjeta profesional: ").append(r.get("numero_tarjeta_profesional"))
                      .append(" | Terminación: ").append(r.get("mes_terminacion")).append("/").append(r.get("anio_terminacion")).append("\n");
                }
            }
            if (secciones.contains(Seccion.IDIOMAS)) {
                sb.append("\n--- IDIOMAS ---\n");
                for (Map<String, Object> r : dao.consultarIdiomas(idCandidato)) {
                    sb.append("• ").append(r.get("nombre")).append(" | Habla: ").append(r.get("habla"))
                      .append(", Lee: ").append(r.get("lectura")).append(", Escribe: ").append(r.get("escritura")).append("\n");
                }
            }
            if (secciones.contains(Seccion.EXPERIENCIA)) {
                sb.append("\n--- EXPERIENCIA LABORAL ---\n");
                for (Map<String, Object> r : dao.consultarExperienciaLaboralDetallada(idCandidato)) {
                    sb.append("• Empresa: ").append(r.get("nombre")).append(" (").append(r.get("tipo_empresa")).append(") en ")
                      .append(r.get("mun")).append(" - ").append(r.get("dep")).append(" (").append(r.get("pais")).append(")\n")
                      .append("  Correo: ").append(r.get("email_empresa")).append(" | Teléfono: ").append(r.get("telefono"))
                      .append(" | Dirección: ").append(r.get("dir_empresa")).append("\n")
                      .append("  Cargo: ").append(r.get("cargo")).append(" | Dependencia: ").append(r.get("dependencia"))
                      .append(" | Ingreso: ").append(r.get("fecha_ingreso")).append(" | Retiro: ")
                      .append(r.get("fecha_retiro") == null ? "vigente" : r.get("fecha_retiro").toString()).append("\n");
                }
            }
            if (secciones.contains(Seccion.OCUPACIONES)) {
                sb.append("\n--- OCUPACIONES ---\n");
                for (Map<String, Object> r : dao.consultarOcupaciones(idCandidato)) {
                    sb.append("• Ocupación: ").append(r.get("ocupacion")).append(" | Experiencia: ")
                      .append(r.get("anio_experiencia")).append(" años y ").append(r.get("meses_experiencia")).append(" meses\n");
                }
            }
            if (secciones.contains(Seccion.VERIFICACIONES)) {
                sb.append("\n--- VERIFICACIONES ---\n");
                for (Map<String, Object> r : dao.consultarVerificaciones(idCandidato)) {
                    sb.append("• ").append(r.get("fecha_verificacion")).append(" | Verificador: ").append(r.get("nombre_completo"))
                      .append(" | ").append(Boolean.TRUE.equals(r.get("aprobada")) ? "Aprobada" : "No aprobada");
                    if (r.get("observaciones") != null && !r.get("observaciones").toString().isEmpty())
                        sb.append(" | ").append(r.get("observaciones"));
                    sb.append("\n");
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
