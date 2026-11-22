package BaseDatos;

import Modelo.Candidato;
import Modelo.ClaseLibreta;
import Modelo.Domicilio;
import Modelo.Educacion;
import Modelo.EducacionBasica;
import Modelo.EducacionSuperior;
import Modelo.Empresa;
import Modelo.ExperienciaLaboral;
import Modelo.Idioma;
import Modelo.LibretaMilitar;
import Modelo.TipoEmpresa;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Acceso a las operaciones SQL de la hoja de vida.
 *
 * <p>La clase no depende de ningún formulario, por lo que también puede ser
 * usada desde pruebas, servicios o nuevas vistas de NetBeans.</p>
 */
public class HojaVidaDAO {

    /** Última decisión de verificación del candidato (1 aprobada, 0 no aprobada o sin verificar). */
    private static final String SQL_VERIFICADA = "COALESCE((SELECT v.aprobada FROM VERIFICACIONES_HOJA_VIDA v "
            + "WHERE v.numero_identificacion_candidato = c.numero_identificacion "
            + "ORDER BY v.fecha_verificacion DESC, v.id DESC LIMIT 1), 0) AS verificada";

    private final BaseDatos baseDatos;

    public HojaVidaDAO() {
        this(new BaseDatos());
    }

    public HojaVidaDAO(BaseDatos baseDatos) {
        if (baseDatos == null) {
            throw new IllegalArgumentException("La conexión a la base de datos es obligatoria");
        }
        this.baseDatos = baseDatos;
    }

    public int registrarMunicipio(String nombre, String departamento, String pais) throws SQLException {
        validarTexto(nombre, "El nombre del municipio");
        validarTexto(departamento, "El departamento");
        validarTexto(pais, "El país");
        try (Connection conexion = abrir()) {
            return new MunicipioDAO(conexion).registrar(nombre, departamento, pais);
        }
    }

    public void registrarEmpresa(Empresa empresa) throws SQLException {
        if (empresa == null) {
            throw new IllegalArgumentException("La empresa es obligatoria");
        }
        validarTexto(empresa.getNombre(), "El nombre de la empresa");
        try (Connection conexion = abrir()) {
            insertarEmpresa(conexion, empresa);
        }
    }

    /** Todas las empresas registradas, ordenadas por nombre (para llenar listas desplegables). */
    public List<Empresa> listarEmpresas() throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        for (Map<String, Object> f : consultar("SELECT e.nombre, e.tipo_empresa, e.email_empresa, e.telefono, "
                + "e.direccion, m.nombre AS municipio, m.departamento, m.pais FROM EMPRESAS e "
                + "JOIN MUNICIPIOS m ON m.id_municipio = e.id_municipio ORDER BY e.nombre")) {
            empresas.add(new Empresa((String) f.get("nombre"), TipoEmpresa.valueOf((String) f.get("tipo_empresa")),
                    (String) f.get("pais"), (String) f.get("departamento"), (String) f.get("municipio"),
                    (String) f.get("email_empresa"), (String) f.get("telefono"), (String) f.get("direccion")));
        }
        return empresas;
    }

    public void registrarExperienciaLaboral(long identificacionCandidato, ExperienciaLaboral experiencia)
            throws SQLException {
        if (experiencia == null) {
            throw new IllegalArgumentException("La experiencia laboral es obligatoria");
        }
        try (Connection conexion = abrir()) {
            boolean confirmacionAnterior = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            try {
                insertarExperiencia(conexion, String.valueOf(identificacionCandidato), experiencia);
                conexion.commit();
                conexion.setAutoCommit(confirmacionAnterior);
            } catch (SQLException | RuntimeException ex) {
                conexion.rollback();
                conexion.setAutoCommit(confirmacionAnterior);
                throw ex;
            }
        }
    }

    public void registrarEducacionBasica(long identificacionCandidato, EducacionBasica educacion)
            throws SQLException {
        try (Connection conexion = abrir()) {
            insertarEducacion(conexion, String.valueOf(identificacionCandidato), educacion);
        }
    }

    public void registrarEducacionSuperior(long identificacionCandidato, EducacionSuperior educacion)
            throws SQLException {
        try (Connection conexion = abrir()) {
            insertarEducacion(conexion, String.valueOf(identificacionCandidato), educacion);
        }
    }

    public void registrarIdioma(long identificacionCandidato, Idioma idioma) throws SQLException {
        try (Connection conexion = abrir()) {
            insertarIdioma(conexion, String.valueOf(identificacionCandidato), idioma);
        }
    }

    public void registrarExperienciaOcupacion(long identificacionCandidato, String ocupacion,
            byte anios, byte meses) throws SQLException {
        try (Connection conexion = abrir()) {
            ejecutar(conexion, "INSERT INTO EXPERIENCIAS_OCUPACIONES_CANDIDATOS "
                            + "(numero_identificacion_candidato, ocupacion, anio_experiencia, meses_experiencia) "
                            + "VALUES (?, ?, ?, ?)",
                    identificacionCandidato, ocupacion, anios, meses);
        }
    }

    public void registrarPersonalRRHH(long identificacion, String nombreCompleto, String correo,
            String usuario, String contrasena) throws SQLException {
        ejecutar("INSERT INTO PERSONAL_RRHH "
                        + "(numero_identificacion, nombre_completo, correo_empresarial, usuario, contraseña) "
                        + "VALUES (?, ?, ?, ?, ?)",
                identificacion, nombreCompleto.trim(), correo, usuario, contrasena);
    }

    /**
     * Registra el candidato y todos sus datos dependientes en una sola
     * transacción. Si una inserción falla, no queda una hoja incompleta.
     */
    public void registrarHojaVidaCompleta(Candidato candidato) throws SQLException {
        validarCandidato(candidato);
        try (Connection conexion = abrir()) {
            boolean confirmacionAnterior = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            try {
                insertarCandidato(conexion, candidato);
                insertarDomicilio(conexion, candidato);
                insertarLibreta(conexion, candidato);
                for (Educacion educacion : candidato.getEducaciones()) {
                    insertarEducacion(conexion, candidato.getNumeroIdentificacion(), educacion);
                }
                for (ExperienciaLaboral experiencia : candidato.getExperiencias()) {
                    insertarExperiencia(conexion, candidato.getNumeroIdentificacion(), experiencia);
                }
                for (Idioma idioma : candidato.getIdiomas()) {
                    insertarIdioma(conexion, candidato.getNumeroIdentificacion(), idioma);
                }
                conexion.commit();
                conexion.setAutoCommit(confirmacionAnterior);
            } catch (SQLException | RuntimeException ex) {
                conexion.rollback();
                conexion.setAutoCommit(confirmacionAnterior);
                throw ex;
            }
        }
    }

    /** Guarda una verificación nueva en VERIFICACIONES_HOJA_VIDA. El estado "verificada" se calcula al consultar. */
    public void registrarVerificacion(long identificacionCandidato, long identificacionVerificador,
            boolean aprobada, String observaciones) throws SQLException {
        ejecutar("INSERT INTO VERIFICACIONES_HOJA_VIDA "
                        + "(numero_identificacion_candidato, identificacion_verificador, aprobada, observaciones) "
                        + "VALUES (?, ?, ?, ?)",
                identificacionCandidato, identificacionVerificador, aprobada, observaciones);
    }

    /** Devuelve la fila (numero_identificacion y nombre_completo) si usuario y contraseña coinciden; si no, null. */
    public Map<String, Object> autenticarPersonalRRHH(String usuario, String contrasena) throws SQLException {
        // BINARY: la collation ignora mayúsculas; el usuario y la contraseña deben coincidir exactamente
        List<Map<String, Object>> filas = consultar(
                "SELECT numero_identificacion, nombre_completo FROM PERSONAL_RRHH "
                        + "WHERE BINARY usuario = ? AND BINARY contraseña = ?", usuario, contrasena);
        return filas.isEmpty() ? null : filas.get(0);
    }

    public boolean existeCandidato(long identificacion) throws SQLException {
        return !consultar("SELECT 1 FROM CANDIDATOS WHERE numero_identificacion = ?", identificacion).isEmpty();
    }

    public List<Map<String, Object>> consultarHojasVidaResumidas() throws SQLException {
        return consultar("SELECT numero_identificacion, tipo_identificacion, nombre, "
                        + "primer_apellido, segundo_apellido, sexo, nacionalidad, email, telefono "
                        + "FROM CANDIDATOS ORDER BY primer_apellido, nombre");
    }

    public List<Map<String, Object>> consultarHojasVidaDetalladas() throws SQLException {
        return consultar("SELECT c.*, d.direccion, m.pais AS pais_domicilio, "
                        + "m.departamento AS departamento_domicilio, m.nombre AS municipio_domicilio, "
                        + "lm.numero AS numero_libreta, lm.clase AS clase_libreta, "
                        + "lm.distrito_militar FROM CANDIDATOS c "
                        + "LEFT JOIN DOMICILIOS_CANDIDATOS d ON d.numero_identificacion_candidato = c.numero_identificacion "
                        + "LEFT JOIN MUNICIPIOS m ON m.id_municipio = d.id_municipio "
                        + "LEFT JOIN LIBRETAS_MILITARES lm ON lm.numero_identificacion_candidato = c.numero_identificacion "
                        + "ORDER BY c.primer_apellido, c.nombre");
    }

    public List<Map<String, Object>> consultarCandidatosDetallados() throws SQLException {
        return consultar("SELECT c.numero_identificacion, c.tipo_identificacion, "
                        + "CONCAT_WS(' ', c.nombre, c.primer_apellido, c.segundo_apellido) AS nombre_completo, "
                        + "c.sexo, c.nacionalidad, c.fecha_nacimiento, "
                        + "CONCAT(m.nombre, ' - ', m.departamento) AS lugar, c.email, c.telefono, "
                        + "CONCAT(d.direccion, ', ', md.nombre) AS domicilio, "
                        + "CONCAT(lm.numero, ' (', lm.clase, ')') AS libreta, " + SQL_VERIFICADA + " "
                        + "FROM CANDIDATOS c "
                        + "JOIN MUNICIPIOS m ON m.id_municipio = c.id_municipio_nacimiento "
                        + "LEFT JOIN DOMICILIOS_CANDIDATOS d ON d.numero_identificacion_candidato = c.numero_identificacion "
                        + "LEFT JOIN MUNICIPIOS md ON md.id_municipio = d.id_municipio "
                        + "LEFT JOIN LIBRETAS_MILITARES lm ON lm.numero_identificacion_candidato = c.numero_identificacion "
                        + "ORDER BY c.primer_apellido, c.nombre");
    }

    public List<Map<String, Object>> consultarDetallePersonal(long identificacion) throws SQLException {
        return consultar("SELECT c.*, m.nombre AS mun_nac, m.departamento AS dep_nac, m.pais AS pais_nac, "
                        + "d.direccion, md.nombre AS mun_dom, md.departamento AS dep_dom, md.pais AS pais_dom, "
                        + "lm.numero AS libreta, lm.clase AS clase_libreta, lm.distrito_militar, "
                        // Va después de c.*: al mapear, la última columna con el mismo nombre gana
                        + SQL_VERIFICADA + " "
                        + "FROM CANDIDATOS c "
                        + "JOIN MUNICIPIOS m ON m.id_municipio = c.id_municipio_nacimiento "
                        + "LEFT JOIN DOMICILIOS_CANDIDATOS d ON d.numero_identificacion_candidato = c.numero_identificacion "
                        + "LEFT JOIN MUNICIPIOS md ON md.id_municipio = d.id_municipio "
                        + "LEFT JOIN LIBRETAS_MILITARES lm ON lm.numero_identificacion_candidato = c.numero_identificacion "
                        + "WHERE c.numero_identificacion = ?", identificacion);
    }

    public List<Map<String, Object>> consultarEducacionBasica(long identificacion) throws SQLException {
        return consultar("SELECT * FROM EDUCACION_BASICA_CANDIDATOS "
                        + "WHERE numero_identificacion_candidato = ?", identificacion);
    }

    public List<Map<String, Object>> consultarEducacionSuperior(long identificacion) throws SQLException {
        return consultar("SELECT * FROM EDUCACION_SUPERIOR_CANDIDATOS "
                        + "WHERE numero_identificacion_candidato = ?", identificacion);
    }

    public List<Map<String, Object>> consultarIdiomas(long identificacion) throws SQLException {
        return consultar("SELECT * FROM IDIOMAS_CANDIDATOS "
                        + "WHERE numero_identificacion_candidato = ?", identificacion);
    }

    public List<Map<String, Object>> consultarExperienciaLaboralDetallada(long identificacion) throws SQLException {
        return consultar("SELECT ec.cargo, ec.dependencia, ec.fecha_ingreso, ec.fecha_retiro, "
                        + "e.nombre, e.tipo_empresa, e.email_empresa, e.telefono, e.direccion AS dir_empresa, "
                        + "m.nombre AS mun, m.departamento AS dep, m.pais "
                        + "FROM EMPLEOS_CANDIDATOS ec "
                        + "JOIN EMPRESAS e ON e.nombre = ec.nombre_empresa "
                        + "JOIN MUNICIPIOS m ON m.id_municipio = e.id_municipio "
                        + "WHERE ec.numero_identificacion_candidato = ? ORDER BY ec.fecha_ingreso", identificacion);
    }

    public List<Map<String, Object>> consultarOcupaciones(long identificacion) throws SQLException {
        return consultar("SELECT * FROM EXPERIENCIAS_OCUPACIONES_CANDIDATOS "
                        + "WHERE numero_identificacion_candidato = ?", identificacion);
    }

    public List<Map<String, Object>> consultarVerificaciones(long identificacion) throws SQLException {
        return consultar("SELECT v.fecha_verificacion, v.aprobada, v.observaciones, "
                        + "p.nombre_completo "
                        + "FROM VERIFICACIONES_HOJA_VIDA v "
                        + "JOIN PERSONAL_RRHH p ON p.numero_identificacion = v.identificacion_verificador "
                        + "WHERE v.numero_identificacion_candidato = ? ORDER BY v.fecha_verificacion", identificacion);
    }

    private void insertarCandidato(Connection conexion, Candidato c) throws SQLException {
        ejecutar(conexion, "INSERT INTO CANDIDATOS (numero_identificacion, tipo_identificacion, nombre, "
                        + "primer_apellido, segundo_apellido, sexo, nacionalidad, telefono, email, "
                        + "manifiesta_juramento, fecha_nacimiento, id_municipio_nacimiento) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Long.parseLong(c.getNumeroIdentificacion()), enumName(c.getTipoIdentificacion()), c.getNombre(),
                c.getPrimerApellido(), c.getSegundoApellido(), enumName(c.getSexo()), enumName(c.getNacionalidad()),
                c.getTelefono(), c.getEmail(), c.isManifiestaJuramento(), Date.valueOf(c.getFechaNacimiento()),
                idMunicipio(conexion, c.getMunicipioNacimiento(), c.getDeptoNacimiento(), c.getPaisNacimiento()));
    }

    private void insertarDomicilio(Connection conexion, Candidato c) throws SQLException {
        Domicilio d = c.getDomicilio();
        if (d != null) {
            ejecutar(conexion, "INSERT INTO DOMICILIOS_CANDIDATOS "
                            + "(numero_identificacion_candidato, direccion, id_municipio) VALUES (?, ?, ?)",
                    Long.parseLong(c.getNumeroIdentificacion()), d.getDireccion(),
                    idMunicipio(conexion, d.getMunicipio(), d.getDepartamento(), d.getPais()));
        }
    }

    private void insertarLibreta(Connection conexion, Candidato c) throws SQLException {
        LibretaMilitar l = c.getLibretaMilitar();
        if (l != null && l.getClase() != null && l.getClase() != ClaseLibreta.NINGUNA) {
            // la base guarda 'PRIMERA' o 'SEGUNDA'; el modelo usa PRIMERA_CLASE y SEGUNDA_CLASE
            String clase = l.getClase() == ClaseLibreta.PRIMERA_CLASE ? "PRIMERA" : "SEGUNDA";
            ejecutar(conexion, "INSERT INTO LIBRETAS_MILITARES "
                            + "(numero, clase, distrito_militar, numero_identificacion_candidato) VALUES (?, ?, ?, ?)",
                    l.getNumero(), clase, l.getDistritoMilitar(),
                    Long.parseLong(c.getNumeroIdentificacion()));
        }
    }

    private void insertarEducacion(Connection conexion, String id, Educacion e) throws SQLException {
        if (e instanceof EducacionBasica) {
            EducacionBasica basica = (EducacionBasica) e;
            ejecutar(conexion, "INSERT INTO EDUCACION_BASICA_CANDIDATOS "
                            + "(numero_identificacion_candidato, ultimo_grado_aprobado, titulo_obtenido, mes_grado, anio_grado) "
                            + "VALUES (?, ?, ?, ?, ?)", Long.parseLong(id), basica.getUltimoGradoAprobado(),
                    basica.getTituloObtenido(), basica.getMesTerminacion(), basica.getAnioTerminacion());
        } else if (e instanceof EducacionSuperior) {
            EducacionSuperior superior = (EducacionSuperior) e;
            ejecutar(conexion, "INSERT INTO EDUCACION_SUPERIOR_CANDIDATOS "
                            + "(numero_identificacion_candidato, titulo_obtenido, numero_tarjeta_profesional, "
                            + "modalidad_academica, semestres_aprobados, graduado, mes_terminacion, anio_terminacion) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Long.parseLong(id), superior.getTituloObtenido(),
                    superior.getNumeroTarjetaProfesional(), enumName(superior.getModalidadAcademica()),
                    superior.getSemestresAprobados(), superior.isGraduado(), superior.getMesTerminacion(),
                    superior.getAnioTerminacion());
        } else {
            throw new IllegalArgumentException("Tipo de educación no soportado: " + e.getClass().getName());
        }
    }

    private void insertarExperiencia(Connection conexion, String id, ExperienciaLaboral x) throws SQLException {
        if (x.getEmpresa() == null || x.getFechaIngreso() == null) {
            throw new IllegalArgumentException("La experiencia debe tener empresa y fecha de ingreso");
        }
        Empresa e = x.getEmpresa();
        insertarEmpresa(conexion, e);
        ejecutar(conexion, "INSERT INTO EMPLEOS_CANDIDATOS "
                        + "(numero_identificacion_candidato, nombre_empresa, cargo, dependencia, fecha_ingreso, fecha_retiro) "
                        + "VALUES (?, ?, ?, ?, ?, ?)", Long.parseLong(id), e.getNombre(), x.getCargo(),
                x.getDependencia(), Date.valueOf(x.getFechaIngreso()),
                x.getFechaRetiro() == null ? null : Date.valueOf(x.getFechaRetiro()));
    }

    // Si la empresa ya existe solo se actualiza su tipo, igual que en FormularioExperienciaLaboral
    private void insertarEmpresa(Connection conexion, Empresa e) throws SQLException {
        ejecutar(conexion, "INSERT INTO EMPRESAS (nombre, tipo_empresa, email_empresa, telefono, direccion, id_municipio) "
                        + "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
                        + "tipo_empresa = VALUES(tipo_empresa), email_empresa = VALUES(email_empresa), "
                        + "telefono = VALUES(telefono), direccion = VALUES(direccion), "
                        + "id_municipio = VALUES(id_municipio)",
                e.getNombre(), enumName(e.getTipo()), e.getEmail(), e.getTelefono(), e.getDireccion(),
                idMunicipio(conexion, e.getMunicipio(), e.getDepartamento(), e.getPais()));
    }

    private int idMunicipio(Connection conexion, String municipio, String departamento, String pais) throws SQLException {
        validarTexto(municipio, "El municipio");
        validarTexto(departamento, "El departamento");
        validarTexto(pais, "El país");
        return new MunicipioDAO(conexion).registrar(municipio, departamento, pais);
    }

    private void insertarIdioma(Connection conexion, String id, Idioma i) throws SQLException {
        ejecutar(conexion, "INSERT INTO IDIOMAS_CANDIDATOS "
                        + "(numero_identificacion_candidato, nombre, habla, lectura, escritura) VALUES (?, ?, ?, ?, ?)",
                Long.parseLong(id), i.getNombreIdioma(), enumName(i.getHabla()), enumName(i.getLectura()),
                enumName(i.getEscritura()));
    }

    private List<Map<String, Object>> consultar(String sql, Object... parametros) throws SQLException {
        try (Connection conexion = abrir(); PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignar(sentencia, parametros);
            try (ResultSet resultados = sentencia.executeQuery()) {
                List<Map<String, Object>> filas = new ArrayList<>();
                int columnas = resultados.getMetaData().getColumnCount();
                while (resultados.next()) {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    for (int i = 1; i <= columnas; i++) {
                        fila.put(resultados.getMetaData().getColumnLabel(i), resultados.getObject(i));
                    }
                    filas.add(fila);
                }
                return filas;
            }
        }
    }

    private void ejecutar(String sql, Object... parametros) throws SQLException {
        try (Connection conexion = abrir()) {
            ejecutar(conexion, sql, parametros);
        }
    }

    private void ejecutar(Connection conexion, String sql, Object... parametros) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignar(sentencia, parametros);
            sentencia.executeUpdate();
        }
    }

    private void asignar(PreparedStatement sentencia, Object... parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            if (parametros[i] == null) {
                sentencia.setNull(i + 1, Types.VARCHAR);
            } else {
                sentencia.setObject(i + 1, parametros[i]);
            }
        }
    }

    private Connection abrir() throws SQLException {
        Connection conexion = baseDatos.getConexion();
        if (conexion == null) {
            throw new SQLException("No fue posible abrir la conexión a la base de datos");
        }
        return conexion;
    }

    private static void validarCandidato(Candidato c) {
        if (c == null || c.getNumeroIdentificacion() == null || c.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("El candidato y sus datos básicos son obligatorios");
        }
    }

    private static void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
    }

    private static String enumName(Enum<?> valor) {
        return valor == null ? null : valor.name();
    }
}
