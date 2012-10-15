package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.ParUsuarioDTO;

@XmlRootElement
public class ParUsuario
{
    private long id;
    private String nombre;
    private String mail;
    private String usuario;

    public ParUsuario()
    {
    }
    
    public ParUsuario(ParUsuarioDTO usuarioDTO)
    {
        this.id = usuarioDTO.getId();
        this.nombre = usuarioDTO.getNombre();
        this.mail = usuarioDTO.getMail();
        this.usuario = usuarioDTO.getUsuario();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}