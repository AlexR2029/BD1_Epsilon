package Modelo;

import java.time.LocalDate;
import java.time.Period;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExperienciaLaboralTest {

    @Test
    public void debeGuardarTodosLosDatos() {
        Empresa empresa = new Empresa();
        LocalDate ingreso = LocalDate.of(2020, 1, 1);
        LocalDate retiro = LocalDate.of(2022, 6, 1);
        ExperienciaLaboral experiencia = new ExperienciaLaboral(empresa, "Analista",
                "Tecnologia", ingreso, retiro);

        assertSame(empresa, experiencia.getEmpresa());
        assertEquals("Analista", experiencia.getCargo());
        assertEquals("Tecnologia", experiencia.getDependencia());
        assertEquals(ingreso, experiencia.getFechaIngreso());
        assertEquals(retiro, experiencia.getFechaRetiro());
        assertEquals(Period.ofYears(2).plusMonths(5), experiencia.calcularTiempoServicio());
    }

    @Test
    public void debeContarHastaHoyCuandoNoTieneFechaDeRetiro() {
        ExperienciaLaboral experiencia = new ExperienciaLaboral();
        experiencia.setFechaIngreso(LocalDate.now().minusYears(1));

        assertEquals(Period.ofYears(1), experiencia.calcularTiempoServicio());
    }

    @Test
    public void debeDevolverPeriodoCeroSinFechaDeIngreso() {
        assertEquals(Period.ZERO, new ExperienciaLaboral().calcularTiempoServicio());
    }
}
