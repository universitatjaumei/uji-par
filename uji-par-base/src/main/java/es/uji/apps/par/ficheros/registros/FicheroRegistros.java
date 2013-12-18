package es.uji.apps.par.ficheros.registros;

import java.util.List;

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

}
