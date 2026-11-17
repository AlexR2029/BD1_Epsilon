package Vista;

import BaseDatos.MunicipioDAO;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/** Resuelve el id de un municipio: reutiliza uno existente o pregunta si hay nombres parecidos. */
final class SelectorMunicipio {

    private SelectorMunicipio() {}

    static int resolver(Component padre, MunicipioDAO dao, String nombre, String depto, String pais) throws SQLException {
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el municipio");
        }
        List<MunicipioDAO.Municipio> parecidos = dao.buscarParecidos(nombre);

        // Mismo nombre y mismo departamento: es el mismo municipio, no se pregunta
        for (MunicipioDAO.Municipio m : parecidos) {
            if (m.nombre().equalsIgnoreCase(nombre) && m.departamento().equalsIgnoreCase(depto)) {
                return m.id();
            }
        }

        if (!parecidos.isEmpty()) {
            Object[] opciones = new Object[parecidos.size() + 1];
            for (int i = 0; i < parecidos.size(); i++) opciones[i] = parecidos.get(i);
            opciones[parecidos.size()] = "Es otro municipio";

            int elegido = JOptionPane.showOptionDialog(padre,
                    "Encontramos municipios parecidos a \"" + nombre + "\". ¿Cuál es?",
                    "Municipio", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opciones, opciones[0]);

            if (elegido == JOptionPane.CLOSED_OPTION) {
                throw new IllegalStateException("Registro cancelado en la selección de municipio");
            }
            if (elegido < parecidos.size()) {
                return parecidos.get(elegido).id();
            }
        }

        if (depto.isEmpty() || pais.isEmpty()) {
            throw new IllegalArgumentException("Para el municipio nuevo \"" + nombre + "\" indique departamento y país");
        }
        return dao.registrar(nombre, depto, pais);
    }
}
