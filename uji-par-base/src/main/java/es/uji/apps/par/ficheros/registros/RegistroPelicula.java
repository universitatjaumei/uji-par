package es.uji.apps.par.ficheros.registros;

import java.util.Locale;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.ficheros.utils.FicherosUtils;

public class RegistroPelicula
{
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
            throw new RegistroSerializaException("El código de sala en la película es nulo");
        
        if (codigoPelicula == 0)
        	throw new RegistroSerializaException("El código de la película es nulo");

        if (codigoExpediente == null)
            throw new RegistroSerializaException("El código de expediente en la película es nulo");

        if (titulo == null)
            throw new RegistroSerializaException("El titulo de la película es nulo");

        if (codigoDistribuidora == null)
            throw new RegistroSerializaException("El codigo de la distribuidora de la película es nulo");

        if (nombreDistribuidora == null)
            throw new RegistroSerializaException("El nombre de la distribuidora de la película es nulo");

        if (versionOriginal == null)
            throw new RegistroSerializaException("El campo versión original de la película es nulo");

        if (versionLinguistica == null)
            throw new RegistroSerializaException("La version linguística de la película es nula");

        if (idiomaSubtitulos == null)
            throw new RegistroSerializaException("El campo de idioma de los subtitulos de la película es nulo");

        if (formatoProyeccion == null)
            throw new RegistroSerializaException("El formato de proyeccion de la película es nulo");

        FicherosUtils.compruebaCodigoSala(codigoSala);

        if (Integer.toString(codigoPelicula).length() > 5)
            throw new RegistroSerializaException("El código de la película tiene más de 5 carácteres: codigoPelicula="
                    + codigoPelicula);

        if (codigoExpediente.length() > 12)
            throw new RegistroSerializaException("El codigo de expediente de la película tiene más de 12 carácteres: codigoExpediente="
                    + codigoExpediente);

        if (codigoDistribuidora.length() > 12)
            throw new RegistroSerializaException(
                    "El codigo de la distribuidora de la película tiene más de 12 carácteres: codigoDistribuidora=" + codigoDistribuidora);

        if (nombreDistribuidora.length() > 50)
            throw new RegistroSerializaException(
                    "El nombre de la distribuidora de la película tiene más de 50 carácteres: nombreDistribuidora=" + nombreDistribuidora);

        if (versionOriginal.length() != 1)
            throw new RegistroSerializaException("El campo versionOriginal de la película tiene que ser 1 dígito: versionOriginal="
                    + versionOriginal);

        if (versionLinguistica.length() != 1)
            throw new RegistroSerializaException("El campo versionLinguistica de la película tiene que ser 1 dígito: versionLinguistica="
                    + versionLinguistica);

        if (idiomaSubtitulos.length() != 1)
            throw new RegistroSerializaException("El campo idiomaSubtitulos de la película tiene que ser 1 dígito: idiomaSubtitulos="
                    + idiomaSubtitulos);
        
        if (formatoProyeccion.length() != 1)
        	throw new RegistroSerializaException("El campo formatoProyeccion de la película tiene que ser 1 dígito: formatoProyeccion="
        			+ formatoProyeccion);

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
