package Vista;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import org.junit.Test;

public class ListaConNuevoTest {

    private final List<String> datos = new ArrayList<>(List.of("Bogotá", "Cali"));

    private ListaConNuevo<String> lista() {
        return new ListaConNuevo<>(() -> new ArrayList<>(datos), JFrame::new, s -> s, "Nuevo");
    }

    @SuppressWarnings("unchecked")
    private static JComboBox<String> combo(ListaConNuevo<String> lista) {
        return (JComboBox<String>) lista.getComponent(0);
    }

    @Test
    public void alCargarSeleccionaElPrimero() {
        assertEquals("Bogotá", lista().seleccionado());
    }

    @Test
    public void listaVaciaNoTieneSeleccion() {
        datos.clear();
        assertNull(lista().seleccionado());
    }

    @Test
    public void recargarConservaLaSeleccionAunqueHayaNuevos() {
        ListaConNuevo<String> lista = lista();
        combo(lista).setSelectedItem("Cali");
        datos.add("Armenia");
        lista.recargar(false);
        assertEquals("Cali", lista.seleccionado());
    }

    @Test
    public void recargarConNuevoSeleccionaElRegistroAgregado() {
        ListaConNuevo<String> lista = lista();
        datos.add("Medellín");
        lista.recargar(true);
        assertEquals("Medellín", lista.seleccionado());
    }

    @Test
    public void recargarConNuevoSinNuevosConservaLaSeleccion() {
        ListaConNuevo<String> lista = lista();
        combo(lista).setSelectedItem("Cali");
        lista.recargar(true); // el usuario cerró el formulario sin agregar nada
        assertEquals("Cali", lista.seleccionado());
    }
}
