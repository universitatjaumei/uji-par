package es.uji.apps.par.model;

import es.uji.apps.par.db.CompraDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Cliente {
    private long id;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String poblacion;
    private String cp;
    private String provincia;
    private String telefono;
    private String email;

    public Cliente() {
    }

    public Cliente(CompraDTO compraDTO) {
        this.id = compraDTO.getId();
        this.nombre = compraDTO.getNombre();
        this.apellidos = compraDTO.getApellidos();
        this.direccion = compraDTO.getDireccion();
        this.poblacion = compraDTO.getPoblacion();
        this.cp = compraDTO.getCp();
        this.provincia = compraDTO.getProvincia();
        this.telefono = compraDTO.getTelefono();
        this.email = compraDTO.getEmail();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
