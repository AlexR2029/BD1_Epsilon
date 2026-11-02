/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Alessandro Herrera Medina (22500113) (alessandro.herrera@uao.edu.co)
 */
public class EducacionSuperior extends Educacion {

    private ModalidadAcademica modalidadAcademica;
    private int semestresAprobados;
    private boolean graduado;
    private String numeroTarjetaProfesional;

    public EducacionSuperior() {
    }

    public EducacionSuperior(String tituloObtenido, Integer mesTerminacion, Integer anioTerminacion,
            ModalidadAcademica modalidadAcademica, int semestresAprobados, boolean graduado,
            String numeroTarjetaProfesional) {
        super(tituloObtenido, mesTerminacion, anioTerminacion);
        this.modalidadAcademica = modalidadAcademica;
        this.semestresAprobados = semestresAprobados;
        this.graduado = graduado;
        this.numeroTarjetaProfesional = numeroTarjetaProfesional;
    }

    public ModalidadAcademica getModalidadAcademica() { return modalidadAcademica; }
    public void setModalidadAcademica(ModalidadAcademica modalidadAcademica) {
        this.modalidadAcademica = modalidadAcademica;
    }
    public int getSemestresAprobados() { return semestresAprobados; }
    public void setSemestresAprobados(int semestresAprobados) {
        this.semestresAprobados = semestresAprobados;
    }
    public boolean isGraduado() { return graduado; }
    public void setGraduado(boolean graduado) { this.graduado = graduado; }
    public String getNumeroTarjetaProfesional() { return numeroTarjetaProfesional; }
    public void setNumeroTarjetaProfesional(String numeroTarjetaProfesional) {
        this.numeroTarjetaProfesional = numeroTarjetaProfesional;
    }

    @Override
    public String toString() {
        return String.format("%s, modalidad: %s, semestres: %d, graduado: %s",
                super.toString(), modalidadAcademica, semestresAprobados, graduado);
    }
}
