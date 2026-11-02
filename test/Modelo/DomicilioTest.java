package Modelo;

import org.junit.Test;
import static org.junit.Assert.*;

public class DomicilioTest {

    @Test
    public void debeGuardarTodosLosDatos() {
        Domicilio domicilio = new Domicilio("Calle 1", "Colombia", "Valle", "Cali");

        assertEquals("Calle 1", domicilio.getDireccion());
        assertEquals("Colombia", domicilio.getPais());
        assertEquals("Valle", domicilio.getDepartamento());
        assertEquals("Cali", domicilio.getMunicipio());
        assertEquals("Calle 1, Cali, Valle, Colombia", domicilio.toString());
    }

    @Test
    public void debePermitirModificarDatos() {
        Domicilio domicilio = new Domicilio();
        domicilio.setDireccion("Carrera 2");
        domicilio.setPais("Colombia");
        domicilio.setDepartamento("Antioquia");
        domicilio.setMunicipio("Medellin");

        assertEquals("Carrera 2", domicilio.getDireccion());
        assertEquals("Medellin", domicilio.getMunicipio());
    }
}
