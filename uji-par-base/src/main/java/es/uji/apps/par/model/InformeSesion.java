package es.uji.apps.par.model;

import java.math.BigDecimal;
import java.util.List;

import es.uji.apps.par.report.InformeModelReport;

public class InformeSesion {
	private Sala sala;
	private Evento evento;
	private Sesion sesion;
	private Integer vendidas;
	private Integer anuladas;
	private BigDecimal total;
	private Integer tipoIncidenciaId;
	private List<InformeModelReport> compras;

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}

	public Integer getVendidas() {
		return vendidas;
	}

	public void setVendidas(Integer vendidas) {
		this.vendidas = vendidas;
	}

	public Integer getAnuladas() {
		return anuladas;
	}

	public void setAnuladas(Integer anuladas) {
		this.anuladas = anuladas;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<InformeModelReport> getCompras() {
		return compras;
	}

	public void setCompras(List<InformeModelReport> compras) {
		this.compras = compras;
	}

	public Integer getTipoIncidenciaId() {
		return tipoIncidenciaId;
	}

	public void setTipoIncidenciaId(Integer tipoIncidenciaId) {
		this.tipoIncidenciaId = tipoIncidenciaId;
	}
}
