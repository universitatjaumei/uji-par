package es.uji.apps.par.ficheros.registros;

import java.util.Locale;

import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.model.Sala;

public class RegistroSala
{
    private String codigo;
    private String nombre;

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String serializa() throws RegistroSerializaException
    {
        Sala.checkValidity(nombre, codigo);
        String result = String.format(Locale.ENGLISH, "1%-12s%-30s", codigo, nombre);
        return result;
    }
}
