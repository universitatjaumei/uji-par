package es.uji.apps.par.model;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    public static ButacaDTO butacaToButacaDTO(Butaca butaca)
    {
        ButacaDTO butacaDTO = new ButacaDTO();

        butacaDTO.setId(butaca.getId());
        butacaDTO.setFila(butaca.getFila());
        butacaDTO.setNumero(butaca.getNumero());
        butacaDTO.setPrecio(butaca.getPrecio());

        return butacaDTO;
    }

    public Butaca()
    {
    }

    public Butaca(String localizacion, String tipo)
    {
        this.localizacion = localizacion;
        this.tipo = tipo;
    }

    public Butaca(ButacaDTO butacaDTO)
    {
        id = butacaDTO.getId();
        fila = butacaDTO.getFila();
        numero = butacaDTO.getNumero();
        precio = butacaDTO.getPrecio();
        localizacion = butacaDTO.getParLocalizacion().getCodigo();
        tipo = butacaDTO.getTipo();
    }

    public static List<Butaca> parseaJSON(String jsonButacas)
    {
        Gson gson = new Gson();
        return gson.fromJson(jsonButacas, new TypeToken<List<Butaca>>()
        {
        }.getType());
    }
    
    public static String toJSON(List<Butaca> butacas)
    {
        Gson gson = new Gson();
        return gson.toJson(butacas, new TypeToken<List<Butaca>>()
        {
        }.getType());
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
    
    @Override
    public String toString()
    {
        StringBuffer buff = new StringBuffer();
        
        buff.append("<id=");
        buff.append(id);
        buff.append(", localizacion=");
        buff.append(localizacion);
        buff.append(", fila=");
        buff.append(fila);
        buff.append(", numero=");
        buff.append(numero);
        buff.append(", tipo=");
        buff.append(tipo);        
        
        return buff.toString();
    }

}