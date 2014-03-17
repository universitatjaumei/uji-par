package es.uji.apps.par.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.utils.DateUtils;

public class InformeModelReport {
	private String evento;
    private String sesion;
    private String fechaCompra;
    private String tipoEntrada;
    private int numeroEntradas;
    private BigDecimal total;
    private String tipoCompra;
    private BigDecimal iva;
    private String localizacion;
    private long eventoId;
    private long sesionId;
    private Integer vendidasTPV;
    private Integer vendidasMetalico;
    private Integer canceladasTaquilla;
    private Integer vendidasTaquilla;
    private Boolean anulada;

    public InformeModelReport()
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

	public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getTipoCompra() {
		return tipoCompra;
	}

	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
	}

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public long getEventoId() {
        return eventoId;
    }

    public void setEventoId(long eventoId) {
        this.eventoId = eventoId;
    }

    public long getSesionId() {
        return sesionId;
    }

    public void setSesionId(long sesionId) {
        this.sesionId = sesionId;
    }

	public Integer getVendidasTPV() {
		return vendidasTPV;
	}

	public void setVendidasTPV(Integer vendidasTPV) {
		this.vendidasTPV = vendidasTPV;
	}

	public Integer getVendidasMetalico() {
		return vendidasMetalico;
	}

	public void setVendidasMetalico(Integer vendidasMetalico) {
		this.vendidasMetalico = vendidasMetalico;
	}

	public Integer getCanceladasTaquilla() {
		return canceladasTaquilla;
	}

	public void setCanceladasTaquilla(Integer canceladasTaquilla) {
		this.canceladasTaquilla = canceladasTaquilla;
	}

	public Integer getVendidasTaquilla() {
		return vendidasTaquilla;
	}

	public void setVendidasTaquilla(Integer vendidasTaquilla) {
		this.vendidasTaquilla = vendidasTaquilla;
	}

	public static InformeModelReport fromButaca(ButacaDTO butaca) {
		InformeModelReport informeModel = new InformeModelReport();
		informeModel.setSesion(DateUtils.dateToSpanishStringWithHour(butaca.getParSesion().getFechaCelebracion()).toString());
		informeModel.setTipoEntrada(butaca.getTipo());
		informeModel.setNumeroEntradas(1);
		informeModel.setFechaCompra(DateUtils.dateToSpanishString(butaca.getParCompra().getFecha()));
		informeModel.setTotal(butaca.getPrecio());
		informeModel.setLocalizacion(butaca.getParLocalizacion().getNombreVa());
		informeModel.setAnulada(butaca.getAnulada());
		return informeModel;
	}

	public Boolean getAnulada() {
		return anulada;
	}

	public void setAnulada(Boolean anulada) {
		this.anulada = anulada;
	}
}
