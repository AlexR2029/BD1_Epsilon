/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BaseDatos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class BaseDatos {
    private final String bd = "hoja_vida";
    private final String url = System.getProperty("mysql.url",
            "jdbc:mysql://localhost:3306/" + bd + "?useSSL=false&serverTimezone=UTC");
    private final String user = System.getProperty("mysql.user",
            System.getenv().getOrDefault("MYSQL_USER", ""));
    private final String password = System.getProperty("mysql.password",
            System.getenv().getOrDefault("MYSQL_PASSWORD", ""));
    
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