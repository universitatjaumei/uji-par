package es.uji.apps.par.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the PAR_SESIONES database table.
 * 
 */
@Entity
@Table(name = "PAR_SESIONES")
public class SesionDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PAR_SESIONES_ID_GENERATOR", sequenceName = "HIBERNATE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAR_SESIONES_ID_GENERATOR")
    private long id;

    @Column(name = "CANAL_INTERNET")
    private BigDecimal canalInternet;

    @Column(name = "CANAL_TAQUILLA")
    private BigDecimal canalTaquilla;

    @Column(name = "FECHA_CELEBRACION")
    private Timestamp fechaCelebracion;

    @Column(name = "FECHA_FIN_VENTA_ONLINE")
    private Timestamp fechaFinVentaOnline;

    @Column(name = "FECHA_INICIO_VENTA_ONLINE")
    private Timestamp fechaInicioVentaOnline;

    @Column(name = "HORA_APERTURA")
    private String horaApertura;

    // bi-directional many-to-one association to ParEventoDTO
    @ManyToOne
    @JoinColumn(name = "EVENTO_ID")
    private EventoDTO parEvento;

    public SesionDTO()
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

    public BigDecimal getCanalInternet()
    {
        return this.canalInternet;
    }

    public void setCanalInternet(BigDecimal canalInternet)
    {
        this.canalInternet = canalInternet;
    }

    public BigDecimal getCanalTaquilla()
    {
        return this.canalTaquilla;
    }

    public void setCanalTaquilla(BigDecimal canalTaquilla)
    {
        this.canalTaquilla = canalTaquilla;
    }

    public Timestamp getFechaCelebracion()
    {
        return this.fechaCelebracion;
    }

    public void setFechaCelebracion(Timestamp fechaCelebracion)
    {
        this.fechaCelebracion = fechaCelebracion;
    }

    public Timestamp getFechaFinVentaOnline()
    {
        return this.fechaFinVentaOnline;
    }

    public void setFechaFinVentaOnline(Timestamp fechaFinVentaOnline)
    {
        this.fechaFinVentaOnline = fechaFinVentaOnline;
    }

    public Timestamp getFechaInicioVentaOnline()
    {
        return this.fechaInicioVentaOnline;
    }

    public void setFechaInicioVentaOnline(Timestamp fechaInicioVentaOnline)
    {
        this.fechaInicioVentaOnline = fechaInicioVentaOnline;
    }

    public String getHoraApertura()
    {
        return this.horaApertura;
    }

    public void setHoraApertura(String horaApertura)
    {
        this.horaApertura = horaApertura;
    }

    public EventoDTO getParEvento()
    {
        return this.parEvento;
    }

    public void setParEvento(EventoDTO parEvento)
    {
        this.parEvento = parEvento;
    }

}