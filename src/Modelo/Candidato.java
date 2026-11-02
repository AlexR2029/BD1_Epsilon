/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alessandro Herrera Medina (22500113) (alessandro.herrera@uao.edu.co)
 */
public class Candidato implements Serializable {

    private static final long serialVersionUID = 1L;
    private String numeroIdentificacion;
    private TipoDocumento tipoIdentificacion;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private Sexo sexo;
    private Nacionalidad nacionalidad;
    private LocalDate fechaNacimiento;
    private String paisNacimiento;
    private String deptoNacimiento;
    private String municipioNacimiento;
    private String email;
    private String telefono;
    private boolean manifiestaJuramento;
    private String observacionesRRHH;
    private Domicilio domicilio;
    private LibretaMilitar libretaMilitar;
    private List<Educacion> educaciones;
    private List<ExperienciaLaboral> experiencias;
    private List<Idioma> idiomas;

    public Candidato() {
        educaciones = new ArrayList<>();
        experiencias = new ArrayList<>();
        idiomas = new ArrayList<>();
    }

    public Candidato(String numeroIdentificacion, TipoDocumento tipoIdentificacion, String nombre,
            String primerApellido, String segundoApellido, Sexo sexo, Nacionalidad nacionalidad,
            LocalDate fechaNacimiento, String email, String telefono) {
        this();
        this.numeroIdentificacion = numeroIdentificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.sexo = sexo;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.telefono = telefono;
    }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String value) { numeroIdentificacion = value; }
    public TipoDocumento getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(TipoDocumento value) { tipoIdentificacion = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { nombre = value; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String value) { primerApellido = value; }
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String value) { segundoApellido = value; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo value) { sexo = value; }
    public Nacionalidad getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(Nacionalidad value) { nacionalidad = value; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate value) { fechaNacimiento = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String value) { telefono = value; }
    public Domicilio getDomicilio() { return domicilio; }
    public void setDomicilio(Domicilio value) { domicilio = value; }
    public LibretaMilitar getLibretaMilitar() { return libretaMilitar; }
    public void setLibretaMilitar(LibretaMilitar value) { libretaMilitar = value; }
    public List<Educacion> getEducaciones() { return educaciones; }
    public void setEducaciones(List<Educacion> value) {
        educaciones = value == null ? new ArrayList<>() : value;
    }
    public List<ExperienciaLaboral> getExperiencias() { return experiencias; }
    public void setExperiencias(List<ExperienciaLaboral> value) {
        experiencias = value == null ? new ArrayList<>() : value;
    }
    public List<Idioma> getIdiomas() { return idiomas; }
    public void setIdiomas(List<Idioma> value) {
        idiomas = value == null ? new ArrayList<>() : value;
    }

    public int calcularEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        LocalDate hoy = LocalDate.now();
        return fechaNacimiento.isAfter(hoy) ? 0 : Period.between(fechaNacimiento, hoy).getYears();
    }

    public String calcularTiempoTotalExperiencia() {
        int meses = experiencias.stream()
                .mapToInt(experiencia -> {
                    Period tiempo = experiencia.calcularTiempoServicio();
                    return tiempo.getYears() * 12 + tiempo.getMonths();
                })
                .sum();
        return String.format("%d años y %d meses", meses / 12, meses % 12);
    }

    public String desplegarInformacionCompleta() {
        return String.format("%s %s %s%nIdentificación: %s %s%nEdad: %d%nNacimiento: %s, %s, %s%n"
                + "Email: %s%nTeléfono: %s%nJuramento: %s%nObservaciones RRHH: %s%n"
                + "Educación: %s%nExperiencia: %s%nIdiomas: %s",
                nombre, primerApellido, segundoApellido, tipoIdentificacion, numeroIdentificacion,
                calcularEdad(), municipioNacimiento, deptoNacimiento, paisNacimiento, email, telefono,
                manifiestaJuramento, observacionesRRHH, educaciones, experiencias, idiomas);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s (%s)", nombre, primerApellido, segundoApellido,
                numeroIdentificacion);
    }
}
