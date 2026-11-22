package BaseDatos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class MunicipioDAO {

    public record Municipio(int id, String nombre, String departamento, String pais) {
        @Override public String toString() { return nombre + " - " + departamento; }
    }

    private static final int DISTANCIA_MAXIMA = 2;

    private final Connection con;

    // La conexión la abre y cierra quien llama, para poder usarla dentro de una transacción
    public MunicipioDAO(Connection con) { this.con = con; }
    public MunicipioDAO() { this(null); }

    /** Municipios con nombre parecido (o igual) al escrito. */
    public List<Municipio> buscarParecidos(String nombre) throws SQLException {
        String buscado = normalizar(nombre);
        List<Municipio> parecidos = new ArrayList<>();
        Connection conexion = con != null ? con : abrir();
        try {
            try (PreparedStatement st = conexion.prepareStatement(
                    "SELECT id_municipio, nombre, departamento, pais FROM municipios");
                 ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    String existente = normalizar(rs.getString("nombre"));
                    if (distancia(buscado, existente) <= DISTANCIA_MAXIMA) {
                        parecidos.add(new Municipio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                    }
                }
            }
        } finally {
            if (con == null) conexion.close();
        }
        return parecidos;
    }

    /** Todos los municipios, ordenados por nombre (para llenar combos). */
    public List<Municipio> listar() throws SQLException {
        List<Municipio> lista = new ArrayList<>();
        Connection conexion = con != null ? con : abrir();
        try {
            try (PreparedStatement st = conexion.prepareStatement(
                    "SELECT id_municipio, nombre, departamento, pais FROM municipios ORDER BY nombre, departamento");
                 ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Municipio(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }
            }
        } finally {
            if (con == null) conexion.close();
        }
        return lista;
    }

    /** Devuelve el id del municipio, creándolo si no existe (llama a sp_registrar_municipio). */
    public int registrar(String nombre, String departamento, String pais) throws SQLException {
        Connection conexion = con != null ? con : abrir();
        try {
            try (CallableStatement cs = conexion.prepareCall("{CALL sp_registrar_municipio(?, ?, ?, ?)}")) {
                cs.setString(1, nombre);
                cs.setString(2, departamento);
                cs.setString(3, pais);
                cs.registerOutParameter(4, Types.INTEGER);
                cs.execute();
                return cs.getInt(4);
            }
        } finally {
            if (con == null) conexion.close();
        }
    }

    private Connection abrir() throws SQLException {
        Connection conexion = new BaseDatos().getConexion();
        if (conexion == null) throw new SQLException("No fue posible abrir la conexión a la base de datos");
        return conexion;
    }

    /** Minúsculas y sin tildes, para comparar "Bogotá" con "bogota". */
    static String normalizar(String s) {
        return Normalizer.normalize(s.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
    }

    /** Distancia de Levenshtein: cuántas letras hay que cambiar para pasar de a a b. */
    static int distancia(String a, String b) {
        int[] anterior = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) anterior[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            int[] actual = new int[b.length() + 1];
            actual[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int costo = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                actual[j] = Math.min(Math.min(actual[j - 1] + 1, anterior[j] + 1), anterior[j - 1] + costo);
            }
            anterior = actual;
        }
        return anterior[b.length()];
    }
}
