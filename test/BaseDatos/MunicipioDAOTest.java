package BaseDatos;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MunicipioDAOTest {
    @Test public void distanciaIgualEsCero()        { assertEquals(0, MunicipioDAO.distancia("cali", "cali")); }
    @Test public void distanciaUnaLetra()           { assertEquals(1, MunicipioDAO.distancia("cali", "calo")); }
    @Test public void distanciaCuatroLetras()       { assertEquals(4, MunicipioDAO.distancia("cali", "pasto")); }
    @Test public void normalizaTildesYMayusculas()  { assertEquals("bogota", MunicipioDAO.normalizar(" Bogotá ")); }
}
