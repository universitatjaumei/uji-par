package es.uji.apps.par.ficheros.registros;

import java.text.SimpleDateFormat;
import java.util.Locale;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.utils.FicherosUtils;

public class RegistroPelicula
{
    private static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("ddMMyy");

    private String codigoSala;
    private int codigoPelicula;
    private String codigoExpediente;
    private String titulo;
    private String codigoDistribuidora;
    private String nombreDistribuidora;
    private String versionOriginal;
    private String versionLinguistica;
    private String idiomaSubtitulos;
    private String formatoProyeccion;

    public String getCodigoSala()
    {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala)
    {
        this.codigoSala = codigoSala;
    }

    public int getCodigoPelicula()
    {
        return codigoPelicula;
    }

    public void setCodigoPelicula(int codigoPelicula)
    {
        this.codigoPelicula = codigoPelicula;
    }

    public String getCodigoExpediente()
    {
        return codigoExpediente;
    }

    public void setCodigoExpediente(String codigoExpediente)
    {
        this.codigoExpediente = codigoExpediente;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getCodigoDistribuidora()
    {
        return codigoDistribuidora;
    }

    public void setCodigoDistribuidora(String codigoDistribuidora)
    {
        this.codigoDistribuidora = codigoDistribuidora;
    }

    public String getNombreDistribuidora()
    {
        return nombreDistribuidora;
    }

    public void setNombreDistribuidora(String nombreDistribuidora)
    {
        this.nombreDistribuidora = nombreDistribuidora;
    }

    public String getVersionOriginal()
    {
        return versionOriginal;
    }

    public void setVersionOriginal(String versionOriginal)
    {
        this.versionOriginal = versionOriginal;
    }

    public String getVersionLinguistica()
    {
        return versionLinguistica;
    }

    public void setVersionLinguistica(String versionLinguistica)
    {
        this.versionLinguistica = versionLinguistica;
    }

    public String getIdiomaSubtitulos()
    {
        return idiomaSubtitulos;
    }

    public void setIdiomaSubtitulos(String idiomaSubtitulos)
    {
        this.idiomaSubtitulos = idiomaSubtitulos;
    }

    public String getFormatoProyeccion()
    {
        return formatoProyeccion;
    }

    public void setFormatoProyeccion(String formatoProyeccion)
    {
        this.formatoProyeccion = formatoProyeccion;
    }

    public String serializa() throws RegistroSerializaException
    {
        if (codigoSala == null)
            throw new RegistroSerializaException("El codigoSala es null");

        if (codigoExpediente == null)
            throw new RegistroSerializaException("El codigoExpediente es null");

        if (titulo == null)
            throw new RegistroSerializaException("El titulo es null");

        if (codigoDistribuidora == null)
            throw new RegistroSerializaException("El codigoDistribuidora es null");

        if (nombreDistribuidora == null)
            throw new RegistroSerializaException("El nombreDistribuidora es null");

        if (versionOriginal == null)
            throw new RegistroSerializaException("La versionOriginal es null");

        if (versionLinguistica == null)
            throw new RegistroSerializaException("La versionLinguistica es null");

        if (idiomaSubtitulos == null)
            throw new RegistroSerializaException("La idiomaSubtitulos es null");

        if (formatoProyeccion == null)
            throw new RegistroSerializaException("La formatoProyeccion es null");

        FicherosUtils.compruebaCodigoSala(codigoSala);

        if (Integer.toString(codigoPelicula).length() > 5)
            throw new RegistroSerializaException("El codigoPelicula tiene más de 5 carácteres: codigoPelicula="
                    + codigoPelicula);

        if (codigoExpediente.length() > 12)
            throw new RegistroSerializaException("El codigoExpediente tiene más de 12 carácteres: codigoExpediente="
                    + codigoExpediente);

        if (codigoDistribuidora.length() > 12)
            throw new RegistroSerializaException(
                    "El codigoDistribuidora tiene más de 12 carácteres: codigoDistribuidora=" + codigoDistribuidora);

        if (nombreDistribuidora.length() > 50)
            throw new RegistroSerializaException(
                    "El nombreDistribuidora tiene más de 12 carácteres: nombreDistribuidora=" + nombreDistribuidora);

        if (versionOriginal.length() != 1)
            throw new RegistroSerializaException("La versionOriginal tiene que ser 1 dígito: versionOriginal="
                    + versionOriginal);

        if (versionLinguistica.length() != 1)
            throw new RegistroSerializaException("La versionLinguistica tiene que ser 1 dígito: versionLinguistica="
                    + versionLinguistica);

        if (idiomaSubtitulos.length() != 1)
            throw new RegistroSerializaException("El idiomaSubtitulos tiene que ser 1 dígito: idiomaSubtitulos="
                    + idiomaSubtitulos);

        String tituloAjustado;

        if (titulo.length() > 50)
            tituloAjustado = titulo.substring(0, 50);
        else
            tituloAjustado = titulo;

        String result = String.format(Locale.ENGLISH, "4%-12s%05d%-12s%-50s%-12s%-50s%s%s%s%s", codigoSala,
                codigoPelicula, codigoExpediente, tituloAjustado, codigoDistribuidora, nombreDistribuidora,
                versionOriginal, versionLinguistica, idiomaSubtitulos, formatoProyeccion);

        return result;
    }
}
