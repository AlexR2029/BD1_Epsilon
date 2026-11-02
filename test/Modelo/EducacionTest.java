package Modelo;

import org.junit.Test;
import static org.junit.Assert.*;

public class EducacionTest {

    @Test
    public void debeGuardarDatosDeLaClaseBase() {
        Educacion educacion = new EducacionImpl("Bachiller", 11, 2018);

        assertEquals("Bachiller", educacion.getTituloObtenido());
        assertEquals(Integer.valueOf(11), educacion.getMesTerminacion());
        assertEquals(Integer.valueOf(2018), educacion.getAnioTerminacion());
    }

    @Test
    public void debePermitirModificarDatosDeLaClaseBase() {
        Educacion educacion = new EducacionImpl();
        educacion.setTituloObtenido("Tecnico");
        educacion.setMesTerminacion(6);
        educacion.setAnioTerminacion(2020);

        assertEquals("Tecnico", educacion.getTituloObtenido());
        assertEquals(Integer.valueOf(6), educacion.getMesTerminacion());
        assertEquals(Integer.valueOf(2020), educacion.getAnioTerminacion());
    }

    private static class EducacionImpl extends Educacion {
        EducacionImpl() {
        }

        EducacionImpl(String titulo, Integer mes, Integer anio) {
            super(titulo, mes, anio);
        }
    }
}
