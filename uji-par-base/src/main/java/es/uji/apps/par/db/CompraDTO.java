package es.uji.apps.par.db;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the PAR_COMPRAS database table.
 * 
 */
@Entity
@Table(name = "PAR_COMPRAS")
public class CompraDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAR_COMPRAS_ID_GENERATOR", sequenceName = "HIBERNATE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAR_COMPRAS_ID_GENERATOR")
	private long id;

	@Column(name = "NOMBRE")
	private String nombre;

	@Column(name = "APELLIDOS")
	private String apellidos;

	@Column(name = "TFNO")
	private String telefono;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "FECHA")
	private Timestamp fecha;
	
	@Column(name = "TAQUILLA")
	private Boolean taquilla;

	// bi-directional many-to-one association to ButacaDTO
	@OneToMany(mappedBy = "parCompra")
	private List<ButacaDTO> parButacas;

	public CompraDTO() {
	}

	public CompraDTO(String nombre, String apellidos, String telefono,
			String email, Timestamp fecha, Boolean taquilla) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.email = email;
		this.fecha = fecha;
		this.taquilla = taquilla;
	}

	public long getId() {
		return this.id;
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

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public List<ButacaDTO> getParButacas() {
		return parButacas;
	}

	public void setParButacas(List<ButacaDTO> parButacas) {
		this.parButacas = parButacas;
	}

    public Boolean getTaquilla() {
        return taquilla;
    }

    public void setTaquilla(Boolean taquilla) {
        this.taquilla = taquilla;
    }
}