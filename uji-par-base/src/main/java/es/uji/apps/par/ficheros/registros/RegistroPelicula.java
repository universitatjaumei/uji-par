package es.uji.apps.par.ficheros.registros;

import java.util.Locale;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Sala;

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
    	Sala.checkValidity(codigoSala);
    	Evento.checkValidity(codigoPelicula, codigoExpediente, titulo, codigoDistribuidora, nombreDistribuidora, versionOriginal, 
    			versionLinguistica, idiomaSubtitulos, formatoProyeccion);
        
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
