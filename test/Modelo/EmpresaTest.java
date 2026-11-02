package Modelo;

import org.junit.Test;
import static org.junit.Assert.*;

public class EmpresaTest {

    @Test
    public void debeGuardarTodosLosDatos() {
        Empresa empresa = new Empresa("Acme", TipoEmpresa.PRIVADA, "Colombia", "Valle",
                "Cali", "info@acme.com", "3000000000", "Calle 1");

        assertEquals("Acme", empresa.getNombre());
        assertEquals(TipoEmpresa.PRIVADA, empresa.getTipo());
        assertEquals("Colombia", empresa.getPais());
        assertEquals("Valle", empresa.getDepartamento());
        assertEquals("Cali", empresa.getMunicipio());
        assertEquals("info@acme.com", empresa.getEmail());
        assertEquals("3000000000", empresa.getTelefono());
        assertEquals("Calle 1", empresa.getDireccion());
        assertTrue(empresa.toString().contains("Acme"));
    }

    @Test
    public void debePermitirModificarDatos() {
        Empresa empresa = new Empresa();
        empresa.setNombre("Nueva empresa");
        empresa.setTipo(TipoEmpresa.PUBLICA);
        empresa.setEmail("contacto@example.com");

        assertEquals("Nueva empresa", empresa.getNombre());
        assertEquals(TipoEmpresa.PUBLICA, empresa.getTipo());
        assertEquals("contacto@example.com", empresa.getEmail());
    }
}
