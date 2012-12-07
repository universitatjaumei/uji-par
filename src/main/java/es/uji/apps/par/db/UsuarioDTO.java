package es.uji.apps.par.db;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the PAR_USUARIOS database table.
 * 
 */
@Entity
@Table(name = "PAR_USUARIOS")
public class UsuarioDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PAR_USUARIOS_ID_GENERATOR", sequenceName = "HIBERNATE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAR_USUARIOS_ID_GENERATOR")
    private long id;

    private String mail;

    private String nombre;

    private String usuario;

    public UsuarioDTO()
    {
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getMail()
    {
        return this.mail;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public String getNombre()
    {
        return this.nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getUsuario()
    {
        return this.usuario;
    }

    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }

}