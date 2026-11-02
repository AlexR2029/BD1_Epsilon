/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;

/**
 *
 * @author Alessandro Herrera Medina (22500113) (alessandro.herrera@uao.edu.co)
 */
public abstract class Educacion implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String tituloObtenido;
    protected Integer mesTerminacion;
    protected Integer anioTerminacion;

    public Educacion() {
    }

    public Educacion(String tituloObtenido, Integer mesTerminacion, Integer anioTerminacion) {
        this.tituloObtenido = tituloObtenido;
        this.mesTerminacion = mesTerminacion;
        this.anioTerminacion = anioTerminacion;
    }

    public String getTituloObtenido() { return tituloObtenido; }
    public void setTituloObtenido(String tituloObtenido) { this.tituloObtenido = tituloObtenido; }
    public Integer getMesTerminacion() { return mesTerminacion; }
    public void setMesTerminacion(Integer mesTerminacion) { this.mesTerminacion = mesTerminacion; }
    public Integer getAnioTerminacion() { return anioTerminacion; }
    public void setAnioTerminacion(Integer anioTerminacion) { this.anioTerminacion = anioTerminacion; }

    @Override
    public String toString() {
        return String.format("%s (%02d/%d)", tituloObtenido, mesTerminacion, anioTerminacion);
    }
}
