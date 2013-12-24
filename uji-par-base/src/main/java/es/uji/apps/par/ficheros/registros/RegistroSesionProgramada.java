package es.uji.apps.par.ficheros.registros;

import java.text.SimpleDateFormat;
import java.util.Locale;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.utils.FicherosUtils;

public class RegistroSesionProgramada
{
    private static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("ddMMyy");

    private String codigoSala;
    private String fechaSesion;
    private int numeroSesiones;

    public String getCodigoSala()
    {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala)
    {
        this.codigoSala = codigoSala;
    }

    public String getFechaSesion()
    {
        return fechaSesion;
    }

    public void setFechaSesion(String fechaSesion)
    {
        this.fechaSesion = fechaSesion;
    }

    public int getNumeroSesiones()
    {
        return numeroSesiones;
    }

    public void setNumeroSesiones(int numeroSesiones)
    {
        this.numeroSesiones = numeroSesiones;
    }

    public String serializa() throws RegistroSerializaException
    {
        if (codigoSala == null)
            throw new RegistroSerializaException("El codigoSala es null");

        if (fechaSesion == null)
            throw new RegistroSerializaException("La fechaSesion es null");

        FicherosUtils.compruebaCodigoSala(codigoSala);

        String result = String.format(Locale.ENGLISH, "5%-12s%s%02d", codigoSala, fechaSesion,
                numeroSesiones);

        return result;
    }
}