package Vista;

import java.awt.Component;
import javax.swing.JOptionPane;

/** Valida que los textos quepan en sus columnas de la base antes de guardar. */
final class LimitesTexto {

    private LimitesTexto() {
    }

    /** Recibe tríos (nombre del campo, texto, máximo). Si alguno se pasa, avisa y devuelve true. */
    static boolean excede(Component padre, Object... campos) {
        for (int i = 0; i < campos.length; i += 3) {
            String texto = (String) campos[i + 1];
            int maximo = (Integer) campos[i + 2];
            if (texto != null && texto.length() > maximo) {
                JOptionPane.showMessageDialog(padre, campos[i] + " admite máximo " + maximo
                        + " caracteres y tiene " + texto.length() + ".", "Dato muy largo", JOptionPane.WARNING_MESSAGE);
                return true;
            }
        }
        return false;
    }
}
