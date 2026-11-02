/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Alessandro Herrera Medina (22500113) (alessandro.herrera@uao.edu.co)
 */
public class EducacionBasica extends Educacion {

    private int ultimoGradoAprobado;

    public EducacionBasica() {
    }

    public EducacionBasica(String tituloObtenido, Integer mesTerminacion, Integer anioTerminacion,
            int ultimoGradoAprobado) {
        super(tituloObtenido, mesTerminacion, anioTerminacion);
        this.ultimoGradoAprobado = ultimoGradoAprobado;
    }

    public int getUltimoGradoAprobado() { return ultimoGradoAprobado; }
    public void setUltimoGradoAprobado(int ultimoGradoAprobado) {
        this.ultimoGradoAprobado = ultimoGradoAprobado;
    }

    @Override
    public String toString() {
        return String.format("%s, último grado aprobado: %d", super.toString(), ultimoGradoAprobado);
    }
}
