package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Informe
{
    private String evento;
    private String sesion;
    private String tipoEntrada;
    private int numeroEntradas;
    private float total;
    private String tipoCompra;

    public Informe()
    {
    	
    }

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getSesion() {
		return sesion;
	}

	public void setSesion(String sesion) {
		this.sesion = sesion;
	}

	public String getTipoEntrada() {
		return tipoEntrada;
	}

	public void setTipoEntrada(String tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}

	public int getNumeroEntradas() {
		return numeroEntradas;
	}

	public void setNumeroEntradas(int numeroEntradas) {
		this.numeroEntradas = numeroEntradas;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getTipoCompra() {
		return tipoCompra;
	}

	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
	}
}