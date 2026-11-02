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
public class Idioma implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombreIdioma;
    private NivelCompetencia habla;
    private NivelCompetencia lectura;
    private NivelCompetencia escritura;

    public Idioma() {
    }

    public Idioma(String nombreIdioma, NivelCompetencia habla, NivelCompetencia lectura,
            NivelCompetencia escritura) {
        this.nombreIdioma = nombreIdioma;
        this.habla = habla;
        this.lectura = lectura;
        this.escritura = escritura;
    }

    public String getNombreIdioma() { return nombreIdioma; }
    public void setNombreIdioma(String nombreIdioma) { this.nombreIdioma = nombreIdioma; }
    public NivelCompetencia getHabla() { return habla; }
    public void setHabla(NivelCompetencia habla) { this.habla = habla; }
    public NivelCompetencia getLectura() { return lectura; }
    public void setLectura(NivelCompetencia lectura) { this.lectura = lectura; }
    public NivelCompetencia getEscritura() { return escritura; }
    public void setEscritura(NivelCompetencia escritura) { this.escritura = escritura; }

    @Override
    public String toString() {
        return String.format("%s (habla: %s, lectura: %s, escritura: %s)",
                nombreIdioma, habla, lectura, escritura);
    }
}
