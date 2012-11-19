package es.uji.apps.par.db;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PAR_USUARIOS database table.
 * 
 */
@Entity
@Table(name="PAR_USUARIOS")
public class ParUsuarioDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String mail;

	private String nombre;

	private String usuario;

	public ParUsuarioDTO() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}