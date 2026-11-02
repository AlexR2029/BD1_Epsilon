package Modelo;

import org.junit.Test;
import static org.junit.Assert.*;

public class EducacionBasicaTest {

    @Test
    public void debeGuardarTodosLosDatos() {
        EducacionBasica educacion = new EducacionBasica("Bachiller", 11, 2018, 11);

        assertEquals("Bachiller", educacion.getTituloObtenido());
        assertEquals(11, educacion.getUltimoGradoAprobado());
        assertTrue(educacion.toString().contains("11"));
    }

    @Test
    public void debePermitirModificarElUltimoGrado() {
        EducacionBasica educacion = new EducacionBasica();
        educacion.setUltimoGradoAprobado(9);

        assertEquals(9, educacion.getUltimoGradoAprobado());
    }
}
