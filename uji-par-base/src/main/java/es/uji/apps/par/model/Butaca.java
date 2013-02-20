package es.uji.apps.par.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.ButacaDTO;

@XmlRootElement
public class Butaca
{
    private long id;
    private String fila;
    private String numero;
    private BigDecimal precio;
    private String localizacion;
    private String x;
    private String y;
    private String tipo;

    public Butaca()
    {
    }

    public Butaca(ButacaDTO butacaDTO)
    {
        id = butacaDTO.getId();
        fila = butacaDTO.getFila();
        numero = butacaDTO.getNumero();
        precio = butacaDTO.getPrecio();
        localizacion = butacaDTO.getParLocalizacion().getCodigo();
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getFila()
    {
        return fila;
    }

    public void setFila(String fila)
    {
        this.fila = fila;
    }

    public String getNumero()
    {
        return numero;
    }

    public void setNumero(String numero)
    {
        this.numero = numero;
    }

    public BigDecimal getPrecio()
    {
        return precio;
    }

    public void setPrecio(BigDecimal precio)
    {
        this.precio = precio;
    }

    public String getLocalizacion()
    {
        return localizacion;
    }

    public void setLocalizacion(String localizacion)
    {
        this.localizacion = localizacion;
    }

    public String getX()
    {
        return x;
    }

    public void setX(String x)
    {
        this.x = x;
    }

    public String getY()
    {
        return y;
    }

    public void setY(String y)
    {
        this.y = y;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

}