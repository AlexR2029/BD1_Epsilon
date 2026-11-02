package Modelo;

import org.junit.Test;
import static org.junit.Assert.*;

public class IdiomaTest {

    @Test
    public void debeGuardarTodosLosDatos() {
        Idioma idioma = new Idioma("Ingles", NivelCompetencia.MB,
                NivelCompetencia.B, NivelCompetencia.R);

        assertEquals("Ingles", idioma.getNombreIdioma());
        assertEquals(NivelCompetencia.MB, idioma.getHabla());
        assertEquals(NivelCompetencia.B, idioma.getLectura());
        assertEquals(NivelCompetencia.R, idioma.getEscritura());
        assertTrue(idioma.toString().contains("Ingles"));
    }

    @Test
    public void debePermitirModificarDatos() {
        Idioma idioma = new Idioma();
        idioma.setNombreIdioma("Frances");
        idioma.setHabla(NivelCompetencia.B);
        idioma.setLectura(NivelCompetencia.MB);
        idioma.setEscritura(NivelCompetencia.B);

        assertEquals("Frances", idioma.getNombreIdioma());
        assertEquals(NivelCompetencia.MB, idioma.getLectura());
    }
}
