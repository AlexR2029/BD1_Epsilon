package Modelo;

import org.junit.Test;
import static org.junit.Assert.*;

public class EducacionSuperiorTest {

    @Test
    public void debeGuardarTodosLosDatos() {
        EducacionSuperior educacion = new EducacionSuperior("Ingenieria", 6, 2024,
                ModalidadAcademica.UN, 10, true, "TP-123");

        assertEquals("Ingenieria", educacion.getTituloObtenido());
        assertEquals(ModalidadAcademica.UN, educacion.getModalidadAcademica());
        assertEquals(10, educacion.getSemestresAprobados());
        assertTrue(educacion.isGraduado());
        assertEquals("TP-123", educacion.getNumeroTarjetaProfesional());
    }

    @Test
    public void debePermitirModificarDatos() {
        EducacionSuperior educacion = new EducacionSuperior();
        educacion.setModalidadAcademica(ModalidadAcademica.MG);
        educacion.setSemestresAprobados(4);
        educacion.setGraduado(false);
        educacion.setNumeroTarjetaProfesional("TP-456");

        assertEquals(ModalidadAcademica.MG, educacion.getModalidadAcademica());
        assertEquals(4, educacion.getSemestresAprobados());
        assertFalse(educacion.isGraduado());
        assertEquals("TP-456", educacion.getNumeroTarjetaProfesional());
    }
}
