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
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private TipoEmpresa tipo;
    private String pais;
    private String departamento;
    private String municipio;
    private String email;
    private String telefono;
    private String direccion;

    public Empresa() {
    }

    public Empresa(String nombre, TipoEmpresa tipo, String pais, String departamento,
            String municipio, String email, String telefono, String direccion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.pais = pais;
        this.departamento = departamento;
        this.municipio = municipio;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoEmpresa getTipo() { return tipo; }
    public void setTipo(TipoEmpresa tipo) { this.tipo = tipo; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        return String.format("%s (%s), %s, %s, %s", nombre, tipo, direccion, municipio, pais);
    }
}
