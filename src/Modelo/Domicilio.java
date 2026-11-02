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
public class Domicilio implements Serializable {

    private static final long serialVersionUID = 1L;
    private String direccion;
    private String pais;
    private String departamento;
    private String municipio;

    public Domicilio() {
    }

    public Domicilio(String direccion, String pais, String departamento, String municipio) {
        this.direccion = direccion;
        this.pais = pais;
        this.departamento = departamento;
        this.municipio = municipio;
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", direccion, municipio, departamento, pais);
    }
}
