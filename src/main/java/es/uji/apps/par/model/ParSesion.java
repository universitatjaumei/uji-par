package es.uji.apps.par.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.uji.apps.par.db.ParEventoDTO;
import es.uji.apps.par.db.ParSesionDTO;
import es.uji.apps.par.util.DateUtils;

@XmlRootElement
public class ParSesion
{
    private long id;
    private ParEventoDTO evento;
    private Date fechaCelebracion;
    private Date fechaInicioVentaOnline;
    private Date fechaFinVentaOnline;
    private String horaAperturaPuertas;
    private BigDecimal canalInternet;
    private BigDecimal canalTaquilla;
    
    public ParSesion() {
    	
    }
    
    public ParSesion(ParSesionDTO sesionDTO) {
    	this.id = sesionDTO.getId();
    	this.evento = sesionDTO.getParEvento();
    	this.fechaCelebracion = sesionDTO.getFechaCelebracion();
    	this.fechaInicioVentaOnline = sesionDTO.getFechaInicioVentaOnline();
    	this.fechaFinVentaOnline = sesionDTO.getFechaFinVentaOnline();
    	this.horaAperturaPuertas = sesionDTO.getHoraApertura();
    	this.canalInternet = sesionDTO.getCanalInternet();
    	this.canalTaquilla = sesionDTO.getCanalTaquilla();
    }
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public ParEventoDTO getEvento() {
		return evento;
	}
	public void setEvento(ParEventoDTO evento) {
		this.evento = evento;
	}
	public Date getFechaCelebracion() {
		return fechaCelebracion;
	}
	/*public void setFechaCelebracion(Date fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}*/
	public Date getFechaInicioVentaOnline() {
		return fechaInicioVentaOnline;
	}
	/*public void setFechaInicioVentaOnline(Date fechaInicioVentaOnline) {
		this.fechaInicioVentaOnline = fechaInicioVentaOnline;
	}*/
	public Date getFechaFinVentaOnline() {
		return fechaFinVentaOnline;
	}
	/*public void setFechaFinVentaOnline(Date fechaFinVentaOnline) {
		this.fechaFinVentaOnline = fechaFinVentaOnline;
	}*/
	public String getHoraAperturaPuertas() {
		return horaAperturaPuertas;
	}
	public void setHoraAperturaPuertas(String horaAperturaPuertas) {
		this.horaAperturaPuertas = horaAperturaPuertas;
	}
	public BigDecimal getCanalInternet() {
		return canalInternet;
	}
	public void setCanalInternet(String canalInternet) {
		this.canalInternet = (canalInternet != null && canalInternet.equals("on"))? new BigDecimal(1): new BigDecimal(0);
	}
	public BigDecimal getCanalTaquilla() {
		return canalTaquilla;
	}
	public void setCanalTaquilla(String canalTaquilla) {
		this.canalTaquilla = (canalTaquilla != null && canalTaquilla.equals("on"))? new BigDecimal(1): new BigDecimal(0);
	}
    
	//TODO
	//definidos porque desde el ext se envía automaticament la fecha con formato dd/mm/YYYY y no serializa bien si el parametro es un Date 
    public void setFechaCelebracion(String fechaCelebracion) {
    	this.fechaCelebracion = DateUtils.spanishStringToDate(fechaCelebracion);
    }
    
    public void setFechaInicioVentaOnline(String fechaInicioVentaOnline) {
    	this.fechaInicioVentaOnline = DateUtils.spanishStringToDate(fechaInicioVentaOnline);
    }
    
    public void setFechaFinVentaOnline(String fechaFinVentaOnline) {
		this.fechaFinVentaOnline = DateUtils.spanishStringToDate(fechaFinVentaOnline);
	}
}