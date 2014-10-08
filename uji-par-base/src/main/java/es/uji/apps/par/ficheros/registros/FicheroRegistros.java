package es.uji.apps.par.ficheros.registros;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

import es.uji.apps.par.exceptions.RegistroSerializaException;

public class FicheroRegistros
{
    private RegistroBuzon registroBuzon;
    private List<RegistroSala> registrosSalas;
    private List<RegistroSesion> registrosSesiones;
    private List<RegistroSesionPelicula> registrosSesionesPeliculas;
    private List<RegistroPelicula> registrosPeliculas;
    private List<RegistroSesionProgramada> registrosSesionesProgramadas;

    public RegistroBuzon getRegistroBuzon()
    {
        return registroBuzon;
    }

    public void setRegistroBuzon(RegistroBuzon registroBuzon)
    {
        this.registroBuzon = registroBuzon;
    }

    public List<RegistroSala> getRegistrosSalas()
    {
        return registrosSalas;
    }

    public void setRegistrosSalas(List<RegistroSala> registrosSalas)
    {
        this.registrosSalas = registrosSalas;
    }

    public List<RegistroSesion> getRegistrosSesiones()
    {
        return registrosSesiones;
    }

    public void setRegistrosSesiones(List<RegistroSesion> registrosSesiones)
    {
        this.registrosSesiones = registrosSesiones;
    }

    public List<RegistroSesionPelicula> getRegistrosSesionesPeliculas()
    {
        return registrosSesionesPeliculas;
    }

    public void setRegistrosSesionesPeliculas(List<RegistroSesionPelicula> registrosSesionesPeliculas)
    {
        this.registrosSesionesPeliculas = registrosSesionesPeliculas;
    }

    public List<RegistroPelicula> getRegistrosPeliculas()
    {
        return registrosPeliculas;
    }

    public void setRegistrosPeliculas(List<RegistroPelicula> registrosPeliculas)
    {
        this.registrosPeliculas = registrosPeliculas;
    }

    public List<RegistroSesionProgramada> getRegistrosSesionesProgramadas()
    {
        return registrosSesionesProgramadas;
    }

    public void setRegistrosSesionesProgramadas(List<RegistroSesionProgramada> registrosSesionesProgramadas)
    {
        this.registrosSesionesProgramadas = registrosSesionesProgramadas;
    }

    public String serializa() throws RegistroSerializaException
    {
        StringBuffer buff = new StringBuffer();

        buff.append(registroBuzon.serializa());
        buff.append("\n");

        for (RegistroSala registroSala : registrosSalas)
        {
            buff.append(registroSala.serializa());
            buff.append("\n");
        }

        for (RegistroSesion registroSesion : registrosSesiones)
        {
            buff.append(registroSesion.serializa());
            buff.append("\n");
        }

        for (RegistroSesionPelicula registroSesionPelicula : registrosSesionesPeliculas)
        {
            buff.append(registroSesionPelicula.serializa());
            buff.append("\n");
        }

        for (RegistroPelicula registroPelicula : registrosPeliculas)
        {
            buff.append(registroPelicula.serializa());
            buff.append("\n");
        }

        for (RegistroSesionProgramada registroSesionProgramada : registrosSesionesProgramadas)
        {
            buff.append(registroSesionProgramada.serializa());
            buff.append("\n");
        }

        return buff.toString();
    }

	public byte[] toByteArray() throws RegistroSerializaException {
		String contenidor = this.serializa();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(outputStream);
		out.write(contenidor);
		out.close();
		return outputStream.toByteArray();
	}
}
