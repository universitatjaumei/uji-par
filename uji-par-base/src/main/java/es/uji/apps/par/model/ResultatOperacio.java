package es.uji.apps.par.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ResultatOperacio {
	private boolean success;
	private List dades;
	private int total;
	private int codi;
	private String descripcio;
	
	
	@SuppressWarnings("unchecked")
	public ResultatOperacio(boolean success, Object dada) {
		this.success = success;
		this.dades = new ArrayList();
		this.dades.add(dada);
	}
	
	public ResultatOperacio(boolean success, int codi, String descripcio) {
		this.success = success;
		this.codi = codi;
		this.descripcio = descripcio;
	}
	
	public ResultatOperacio(boolean b) {
		this.success = true;
	}
	
	public ResultatOperacio() {
		
	}

	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List getDades() {
		return dades;
	}
	public void setDades(List dades) {
		this.dades = dades;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	public String getDescripcio() {
		return descripcio;
	}

	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	public int getCodi() {
		return codi;
	}

	public void setCodi(int codi) {
		this.codi = codi;
	}
	
}
