package es.uji.apps.par.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.CompraDTO;

@XmlRootElement
public class Compra
{
    private long id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;
    private List<Butaca> parButacas;

    public Compra(CompraDTO compraDTO)
    {
        this.setId(compraDTO.getId());
        this.setNombre(compraDTO.getNombre());
        this.setApellidos(compraDTO.getApellidos());
        this.setTelefono(compraDTO.getTelefono());
        this.setEmail(compraDTO.getEmail());
    }

    public static Compra compraDTOtoCompra(CompraDTO compraDTO)
    {
        return new Compra(compraDTO);
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public void setApellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public List<Butaca> getParButacas()
    {
        return parButacas;
    }

    public void setParButacas(List<Butaca> parButacas)
    {
        this.parButacas = parButacas;
    }

}