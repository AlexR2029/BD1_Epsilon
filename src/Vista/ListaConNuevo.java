package Vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** Lista desplegable con los registros existentes y un botón que abre el formulario para agregar uno nuevo. */
final class ListaConNuevo<T> extends JPanel {

    private final JComboBox<T> combo = new JComboBox<>() {
        @Override
        public Dimension getPreferredSize() {
            // ancho fijo: si dependiera del nombre más largo, un registro largo ensancharía todo el formulario
            return new Dimension(ANCHO_LISTA, super.getPreferredSize().height);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
    };
    private static final int ANCHO_LISTA = 120;
    private final Callable<List<T>> cargador;
    private final Function<T, String> texto;
    private Runnable despuesDeAgregar = () -> { };

    /**
     * @param cargador        lee de la base los registros existentes
     * @param formularioNuevo crea el formulario que registra uno nuevo
     * @param texto           cómo se muestra cada registro en la lista
     * @param textoBoton      texto del botón de agregar
     */
    ListaConNuevo(Callable<List<T>> cargador, Supplier<JFrame> formularioNuevo,
            Function<T, String> texto, String textoBoton) {
        super(new BorderLayout(5, 0));
        this.cargador = cargador;
        this.texto = texto;

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            @SuppressWarnings("unchecked")
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                    boolean seleccionado, boolean conFoco) {
                return super.getListCellRendererComponent(lista, valor == null ? "" : texto.apply((T) valor),
                        indice, seleccionado, conFoco);
            }
        });

        JButton boton = new JButton(textoBoton);
        boton.addActionListener(e -> {
            JFrame formulario = formularioNuevo.get();
            formulario.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent ev) {
                    recargar(true);
                    despuesDeAgregar.run();
                }
            });
            formulario.setVisible(true);
        });

        add(combo, BorderLayout.CENTER);
        add(boton, BorderLayout.EAST);
        recargar(false);
    }

    /** Registro elegido, o null si la lista está vacía. */
    @SuppressWarnings("unchecked")
    T seleccionado() {
        return (T) combo.getSelectedItem();
    }

    /** Código a ejecutar al cerrar el formulario de agregar (para refrescar otras listas). */
    void despuesDeAgregar(Runnable accion) {
        this.despuesDeAgregar = accion;
    }

    /** Vuelve a leer la lista; con seleccionarNuevo deja elegido el registro que antes no estaba. */
    void recargar(boolean seleccionarNuevo) {
        Set<String> antes = new HashSet<>();
        for (int i = 0; i < combo.getItemCount(); i++) {
            antes.add(texto.apply(combo.getItemAt(i)));
        }
        T previo = seleccionado();
        String textoPrevio = previo == null ? null : texto.apply(previo);

        combo.removeAllItems();
        try {
            T nuevo = null;
            T mismoDeAntes = null;
            for (T item : cargador.call()) {
                combo.addItem(item);
                String t = texto.apply(item);
                if (nuevo == null && !antes.contains(t)) nuevo = item;
                if (t.equals(textoPrevio)) mismoDeAntes = item;
            }
            T elegido = seleccionarNuevo && nuevo != null ? nuevo : mismoDeAntes;
            if (elegido != null) combo.setSelectedItem(elegido);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la lista: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
