package Modelo;

import java.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class CandidatoTest {

    @Test
    public void debeInicializarColeccionesVacias() {
        Candidato candidato = new Candidato();

        assertNotNull(candidato.getEducaciones());
        assertNotNull(candidato.getExperiencias());
        assertNotNull(candidato.getIdiomas());
        assertTrue(candidato.getEducaciones().isEmpty());
    }

    @Test
    public void debeGuardarDatosBasicos() {
        Candidato candidato = new Candidato("123", TipoDocumento.CC, "Ana", "Lopez",
                "Diaz", Sexo.F, Nacionalidad.COLOMBIA, LocalDate.of(2000, 1, 1),
                "ana@example.com", "3000000000");

        assertEquals("123", candidato.getNumeroIdentificacion());
        assertEquals(TipoDocumento.CC, candidato.getTipoIdentificacion());
        assertEquals("Ana", candidato.getNombre());
        assertEquals(26, candidato.calcularEdad());
    }

    @Test
    public void debeCalcularEdadCeroParaFechaFutura() {
        Candidato candidato = new Candidato();
        candidato.setFechaNacimiento(LocalDate.now().plusDays(1));

        assertEquals(0, candidato.calcularEdad());
    }

    @Test
    public void debeReemplazarColeccionesNulasPorListasVacias() {
        Candidato candidato = new Candidato();

        candidato.setEducaciones(null);
        candidato.setExperiencias(null);
        candidato.setIdiomas(null);

        assertNotNull(candidato.getEducaciones());
        assertNotNull(candidato.getExperiencias());
        assertNotNull(candidato.getIdiomas());
    }
}
