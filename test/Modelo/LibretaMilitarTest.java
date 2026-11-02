package Modelo;

import org.junit.Test;
import static org.junit.Assert.*;

public class LibretaMilitarTest {

    @Test
    public void debeGuardarTodosLosDatos() {
        LibretaMilitar libreta = new LibretaMilitar("123", ClaseLibreta.PRIMERA_CLASE, 5);

        assertEquals("123", libreta.getNumero());
        assertEquals(ClaseLibreta.PRIMERA_CLASE, libreta.getClase());
        assertEquals(5, libreta.getDistritoMilitar());
        assertEquals("123 (PRIMERA_CLASE), distrito 5", libreta.toString());
    }

    @Test
    public void debePermitirModificarDatos() {
        LibretaMilitar libreta = new LibretaMilitar();
        libreta.setNumero("456");
        libreta.setClase(ClaseLibreta.SEGUNDA_CLASE);
        libreta.setDistritoMilitar(10);

        assertEquals("456", libreta.getNumero());
        assertEquals(ClaseLibreta.SEGUNDA_CLASE, libreta.getClase());
        assertEquals(10, libreta.getDistritoMilitar());
    }
}
