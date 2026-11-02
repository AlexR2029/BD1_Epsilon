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
public class LibretaMilitar implements Serializable {

    private static final long serialVersionUID = 1L;
    private String numero;
    private ClaseLibreta clase;
    private int distritoMilitar;

    public LibretaMilitar() {
    }

    public LibretaMilitar(String numero, ClaseLibreta clase, int distritoMilitar) {
        this.numero = numero;
        this.clase = clase;
        this.distritoMilitar = distritoMilitar;
    }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public ClaseLibreta getClase() { return clase; }
    public void setClase(ClaseLibreta clase) { this.clase = clase; }
    public int getDistritoMilitar() { return distritoMilitar; }
    public void setDistritoMilitar(int distritoMilitar) { this.distritoMilitar = distritoMilitar; }

    @Override
    public String toString() {
        return String.format("%s (%s), distrito %d", numero, clase, distritoMilitar);
    }
}
