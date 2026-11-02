package Vista;

import Modelo.Candidato;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class USACandidatoTest {

    @Test
    public void debeMostrarListaVaciaSinTexto() {
        USACandidato vista = new USACandidato();

        assertEquals("", vista.mostrarTodos(new ArrayList<Candidato>()));
    }

    @Test
    public void debeAgregarYMostrarCandidato() {
        USACandidato vista = new USACandidato();
        Candidato candidato = new Candidato();
        candidato.setNombre("Ana");

        vista.agregarCandidato(candidato);

        assertTrue(vista.mostrarTodos(new ArrayList<Candidato>() {{
            add(candidato);
        }}).contains("Ana"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noDebeAceptarCandidatoNulo() {
        new USACandidato().agregarCandidato(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noDebeMostrarListaNula() {
        new USACandidato().mostrarTodos(null);
    }
}
