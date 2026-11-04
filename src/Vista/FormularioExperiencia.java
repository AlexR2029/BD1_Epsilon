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

public class FormularioExperiencia extends JFrame {

    private long idCandidato;
    
    private JTextField txtEmpresa, txtPais, txtDep, txtMun, txtEmailEmp, txtTelEmp, txtDirEmp;
    private JComboBox<String> cbTipoEmpresa;
    private JTextField txtCargo, txtDependencia, txtFechaIngreso, txtFechaRetiro;
    private JTextField txtOcupacion, txtAniosExp, txtMesesExp;
    
    private JButton btnGuardar, btnAgregarOtro, btnOmitir;

    public FormularioExperiencia(long idCandidato) {
        this.idCandidato = idCandidato;
        
        setTitle("Experiencia Laboral (ID: " + idCandidato + ")");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        setLayout(new GridLayout(0, 2, 5, 5));

        //empresa
        add(new JLabel(" --- DATOS DE LA EMPRESA ---")); add(new JLabel(""));
        add(new JLabel(" Nombre Empresa:")); txtEmpresa = new JTextField(); add(txtEmpresa);
        add(new JLabel(" Tipo Empresa:")); cbTipoEmpresa = new JComboBox<>(new String[]{"PUBLICA", "PRIVADA"}); add(cbTipoEmpresa);
        add(new JLabel(" País:")); txtPais = new JTextField("COLOMBIA"); add(txtPais);
        add(new JLabel(" Departamento:")); txtDep = new JTextField(); add(txtDep);
        add(new JLabel(" Municipio:")); txtMun = new JTextField(); add(txtMun);
        add(new JLabel(" Email Empresa:")); txtEmailEmp = new JTextField(); add(txtEmailEmp);
        add(new JLabel(" Teléfono Empresa:")); txtTelEmp = new JTextField(); add(txtTelEmp);
        add(new JLabel(" Dirección Empresa:")); txtDirEmp = new JTextField(); add(txtDirEmp);

        //empleo
        add(new JLabel(" --- DATOS DEL CARGO ---")); add(new JLabel(""));
        add(new JLabel(" Cargo:")); txtCargo = new JTextField(); add(txtCargo);
        add(new JLabel(" Dependencia/Área:")); txtDependencia = new JTextField(); add(txtDependencia);
        add(new JLabel(" Fecha Ingreso (YYYY-MM-DD):")); txtFechaIngreso = new JTextField(); add(txtFechaIngreso);
        add(new JLabel(" Fecha Retiro (Opcional):")); txtFechaRetiro = new JTextField(); add(txtFechaRetiro);

        //info ocupacion
        add(new JLabel(" --- RESUMEN EXPERIENCIA ---")); add(new JLabel(""));
        add(new JLabel(" Ocupación (Ej: Ingeniero):")); txtOcupacion = new JTextField(); add(txtOcupacion);
        add(new JLabel(" Años de Experiencia:")); txtAniosExp = new JTextField("0"); add(txtAniosExp);
        add(new JLabel(" Meses de Experiencia (0-11):")); txtMesesExp = new JTextField("0"); add(txtMesesExp);

        btnOmitir = new JButton("Omitir / Finalizar"); 
        add(btnOmitir);
        
        btnAgregarOtro = new JButton("Guardar y Agregar Otra"); 
        add(btnAgregarOtro);

        add(new JLabel("")); // Espaciador
        btnGuardar = new JButton("Guardar y Finalizar"); 
        add(btnGuardar);

        btnAgregarOtro.addActionListener(e -> {
            if(registrarEnBD()) {
                JOptionPane.showMessageDialog(this, "Experiencia guardada. Puedes ingresar la siguiente.");
                limpiarCampos();
            }
        });
        
        btnGuardar.addActionListener(e -> {
            if(registrarEnBD()) {
                JOptionPane.showMessageDialog(this, "¡Hoja de Vida registrada completamente con éxito!");
                finalizarProceso();
            }
        });
        
        btnOmitir.addActionListener(e -> finalizarProceso());
    }

    private boolean registrarEnBD() {
        if(txtEmpresa.getText().trim().isEmpty() || txtCargo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre de empresa y cargo son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        BaseDatos bd = new BaseDatos();
        Connection conexion = bd.getConexion();
        boolean exito = false;
        
        String sqlEmpresa = "INSERT IGNORE INTO EMPRESAS (nombre, tipo_empresa, pais, departamento, municipio, email_empresa, telefono, direccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlEmpleo = "INSERT INTO EMPLEOS_CANDIDATOS (numero_identificacion_candidato, nombre_empresa, fecha_ingreso, cargo, fecha_retiro, dependencia) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlOcupacion = "INSERT IGNORE INTO EXPERIENCIAS_OCUPACIONES_CANDIDATOS (numero_identificacion_candidato, ocupacion, anio_experiencia, meses_experiencia) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pst1 = conexion.prepareStatement(sqlEmpresa);
            pst1.setString(1, txtEmpresa.getText().trim());
            pst1.setString(2, cbTipoEmpresa.getSelectedItem().toString());
            pst1.setString(3, txtPais.getText().trim());
            pst1.setString(4, txtDep.getText().trim());
            pst1.setString(5, txtMun.getText().trim());
            pst1.setString(6, txtEmailEmp.getText().trim());
            pst1.setString(7, txtTelEmp.getText().trim());
            pst1.setString(8, txtDirEmp.getText().trim());
            pst1.executeUpdate();

            PreparedStatement pst2 = conexion.prepareStatement(sqlEmpleo);
            pst2.setLong(1, this.idCandidato);
            pst2.setString(2, txtEmpresa.getText().trim());
            pst2.setString(3, txtFechaIngreso.getText().trim());
            pst2.setString(4, txtCargo.getText().trim());
            
            String fechaRetiro = txtFechaRetiro.getText().trim();
            if(fechaRetiro.isEmpty()) pst2.setNull(5, java.sql.Types.DATE);
            else pst2.setString(5, fechaRetiro);
            
            pst2.setString(6, txtDependencia.getText().trim());
            pst2.executeUpdate();

            PreparedStatement pst3 = conexion.prepareStatement(sqlOcupacion);
            pst3.setLong(1, this.idCandidato);
            pst3.setString(2, txtOcupacion.getText().trim());
            pst3.setInt(3, Integer.parseInt(txtAniosExp.getText().trim()));
            pst3.setInt(4, Integer.parseInt(txtMesesExp.getText().trim()));
            pst3.executeUpdate();

            exito = true;
            conexion.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar experiencia: " + e.getMessage());
        }
        return exito;
    }

    private void limpiarCampos() {
        txtEmpresa.setText("");
        cbTipoEmpresa.setSelectedIndex(0);
        txtDep.setText("");
        txtMun.setText("");
        txtEmailEmp.setText("");
        txtTelEmp.setText("");
        txtDirEmp.setText("");
        
        txtCargo.setText("");
        txtDependencia.setText("");
        txtFechaIngreso.setText("");
        txtFechaRetiro.setText("");
        
        txtOcupacion.setText("");
        txtAniosExp.setText("0");
        txtMesesExp.setText("0");
        
        txtEmpresa.requestFocus();
    }
    
    private void finalizarProceso() {
        this.dispose();
    }
}