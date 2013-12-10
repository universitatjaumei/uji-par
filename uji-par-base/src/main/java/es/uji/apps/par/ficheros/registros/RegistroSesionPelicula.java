package es.uji.apps.par.ficheros.registros;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.utils.FicherosUtils;

public class RegistroSesionPelicula
{
    private static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("ddMMyy");

    private String codigoSala;
    private Date fecha;
    private String hora;
    private int codigoPelicula;

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

    public int getCodigoPelicula()
    {
        return codigoPelicula;
    }

    public void setCodigoPelicula(int codigoPelicula)
    {
        this.codigoPelicula = codigoPelicula;
    }

    public String serializa() throws RegistroSerializaException
    {
        if (codigoSala == null)
            throw new RegistroSerializaException("El codigoSala es null");

        if (fecha == null)
            throw new RegistroSerializaException("La fecha es null");

        if (hora == null)
            throw new RegistroSerializaException("La hora es null");

        FicherosUtils.compruebaCodigoSala(codigoSala);

        if (hora.length() != 4)
            throw new RegistroSerializaException("hora es un string de tamaño distinto de 4 carácteres: hora=" + hora);

        String result = String.format(Locale.ENGLISH, "3%-12s%s%s%05d", codigoSala, DAY_FORMAT.format(fecha), hora,
                codigoPelicula);

        return result;
    }
}
