/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
public class Candidato {
    
    private long numeroIdentificacion;
    private String tipoIdentificacion;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String sexo;
    private String nacionalidad;
    private String telefono;
    private String email;
    private boolean manifiestaJuramento;
    private String observacionesRrhh;
    private String fechaNacimiento;
    private String paisNacimiento;
    private String departamentoNacimiento;
    private String municipioNacimiento;

    public Candidato() {
    }

    
    public long getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(long numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isManifiestaJuramento() { return manifiestaJuramento; }
    public void setManifiestaJuramento(boolean manifiestaJuramento) { this.manifiestaJuramento = manifiestaJuramento; }

    public String getObservacionesRrhh() { return observacionesRrhh; }
    public void setObservacionesRrhh(String observacionesRrhh) { this.observacionesRrhh = observacionesRrhh; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getPaisNacimiento() { return paisNacimiento; }
    public void setPaisNacimiento(String paisNacimiento) { this.paisNacimiento = paisNacimiento; }

    public String getDepartamentoNacimiento() { return departamentoNacimiento; }
    public void setDepartamentoNacimiento(String departamentoNacimiento) { this.departamentoNacimiento = departamentoNacimiento; }

    public String getMunicipioNacimiento() { return municipioNacimiento; }
    public void setMunicipioNacimiento(String municipioNacimiento) { this.municipioNacimiento = municipioNacimiento; }
}