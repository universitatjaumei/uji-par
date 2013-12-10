package es.uji.apps.par.ficheros;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.utils.FicherosUtils;

public class RegistroSesion
{
    private static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("ddMMyy");

    private String codigoSala;
    private Date fecha;
    private String hora;
    private int peliculas;
    private int espectadores;
    private BigDecimal recaudacion;
    private TipoIncidencia incidencia;

    public String getCodigoSala()
    {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala)
    {
        this.codigoSala = codigoSala;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public String getHora()
    {
        return hora;
    }

    public void setHora(String hora)
    {
        this.hora = hora;
    }

    public int getPeliculas()
    {
        return peliculas;
    }

    public void setPeliculas(int peliculas)
    {
        this.peliculas = peliculas;
    }

    public int getEspectadores()
    {
        return espectadores;
    }

    public void setEspectadores(int espectadores)
    {
        this.espectadores = espectadores;
    }

    public BigDecimal getRecaudacion()
    {
        return recaudacion;
    }

    public void setRecaudacion(BigDecimal recaudacion)
    {
        this.recaudacion = recaudacion;
    }

    public TipoIncidencia getIncidencia()
    {
        return incidencia;
    }

    public void setIncidencia(TipoIncidencia incidencia)
    {
        this.incidencia = incidencia;
    }

    public String serializa() throws RegistroSerializaException
    {
        if (codigoSala == null)
            throw new RegistroSerializaException("El codigoSala es null");

        if (fecha == null)
            throw new RegistroSerializaException("La fecha es null");

        if (hora == null)
            throw new RegistroSerializaException("La hora es null");

        if (recaudacion == null)
            throw new RegistroSerializaException("La recaudacion es null");

        if (incidencia == null)
            throw new RegistroSerializaException("La incidencia es null");

        FicherosUtils.compruebaCodigoSala(codigoSala);

        if (hora.length() != 4)
            throw new RegistroSerializaException("hora es un string de tamaño distinto de 4 carácteres: hora=" + hora);

        String result = String.format(Locale.ENGLISH, "2%-12s%s%s%02d%05d%08.2f%s", codigoSala,
                DAY_FORMAT.format(fecha), hora, peliculas, espectadores, recaudacion, incidencia.getCodigo());

        return result;
    }
}
