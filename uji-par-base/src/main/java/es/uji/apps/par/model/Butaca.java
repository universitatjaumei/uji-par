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
    private Localizacion parLocalizacion;
    
    public Butaca()
    {
    }
    
    public Butaca(ButacaDTO butacaDTO)
    {
        id = butacaDTO.getId();
        fila = butacaDTO.getFila();
        numero = butacaDTO.getNumero();
        precio = butacaDTO.getPrecio();
        
        parLocalizacion = Localizacion.localizacionDTOtoLocalizacion(butacaDTO.getParLocalizacion());
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

    public Localizacion getParLocalizacion()
    {
        return parLocalizacion;
    }

    public void setParLocalizacion(Localizacion parLocalizacion)
    {
        this.parLocalizacion = parLocalizacion;
    }

}