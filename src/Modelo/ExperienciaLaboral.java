/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author Alessandro Herrera Medina (22500113) (alessandro.herrera@uao.edu.co)
 */
public class ExperienciaLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    private Empresa empresa;
    private String cargo;
    private String dependencia;
    private LocalDate fechaIngreso;
    private LocalDate fechaRetiro;

    public ExperienciaLaboral() {
    }

    public ExperienciaLaboral(Empresa empresa, String cargo, String dependencia,
            LocalDate fechaIngreso, LocalDate fechaRetiro) {
        this.empresa = empresa;
        this.cargo = cargo;
        this.dependencia = dependencia;
        this.fechaIngreso = fechaIngreso;
        this.fechaRetiro = fechaRetiro;
    }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getDependencia() { return dependencia; }
    public void setDependencia(String dependencia) { this.dependencia = dependencia; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public LocalDate getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(LocalDate fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    public Period calcularTiempoServicio() {
        if (fechaIngreso == null) {
            return Period.ZERO;
        }
        LocalDate fin = fechaRetiro == null ? LocalDate.now() : fechaRetiro;
        if (fin.isBefore(fechaIngreso)) {
            return Period.ZERO;
        }
        return Period.between(fechaIngreso, fin);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s a %s)", empresa, cargo, fechaIngreso, fechaRetiro);
    }
}
