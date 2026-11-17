/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BaseDatos;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class BaseDatos {
    // Orden: variable de entorno, luego archivo .env (en la raíz del proyecto), luego valor por defecto.
    private static final Map<String, String> ARCHIVO_ENV = cargarArchivoEnv(".env");

    private final String url = env("DB_URL");
    private final String user = env("DB_USER");
    private final String password = env("DB_PASSWORD");

    private static String env(String nombre) {
        String valor = System.getenv(nombre);
        if (valor == null) valor = ARCHIVO_ENV.get(nombre);
        return valor;
    }

    private static Map<String, String> cargarArchivoEnv(String ruta) {
        Map<String, String> datos = new HashMap<>();
        try {
            for (String linea : Files.readAllLines(Paths.get(ruta), StandardCharsets.UTF_8)) {
                linea = linea.trim();
                int igual = linea.indexOf('=');
                if (linea.isEmpty() || linea.startsWith("#") || igual < 0) continue;
                datos.put(linea.substring(0, igual).trim(), linea.substring(igual + 1).trim());
            }
        } catch (IOException e) {
            // Sin .env: se usan variables de entorno o los valores por defecto
        }
        return datos;
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: Falta el Driver de MySQL");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos: " + e.getMessage());
        }
        return con;
    }
}