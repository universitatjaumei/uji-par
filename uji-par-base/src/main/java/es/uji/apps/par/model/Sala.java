package es.uji.apps.par.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.SalaDTO;

@XmlRootElement
public class Sala
{
    private long id;
    private String codigo;
    private String nombre;
    private int asientos;
    private int asientosDiscapacitados;
    private int asientosNoReservados;    
    private String tipo;
    private String formato;
    private String subtitulo;

    public Sala(long id)
    {
        this.id = id;
    }
    
    public Sala(String nombre)
    {
        this.nombre = nombre;
    }
    
    public Sala(SalaDTO salaDTO)
    {
        this.setId(salaDTO.getId());
        this.setCodigo(salaDTO.getCodigo());
        this.setNombre(salaDTO.getNombre());
        this.setAsientos(salaDTO.getAsientos());
        this.setAsientosDiscapacitados(salaDTO.getAsientosDiscapacitados());
        this.setAsientosNoReservados(salaDTO.getAsientosNoReservados());
        this.setTipo(salaDTO.getTipo());
        this.setFormato(salaDTO.getFormato());
        this.setSubtitulo(salaDTO.getSubtitulo());
    }

    public static Sala salaDTOtoSala(SalaDTO salaDTO)
    {
        return new Sala(salaDTO);
    }
    
    public static SalaDTO salaToSalaDTO(Sala sala)
    {
        SalaDTO salaDTO = new SalaDTO();
        
        salaDTO.setId(sala.getId());
        salaDTO.setCodigo(sala.getCodigo());
        salaDTO.setNombre(sala.getNombre());
        salaDTO.setAsientos(sala.getAsientos());
        salaDTO.setAsientosDiscapacitados(sala.getAsientosDiscapacitados());
        salaDTO.setAsientosNoReservados(sala.getAsientosNoReservados());
        salaDTO.setTipo(sala.getTipo());
        salaDTO.setFormato(sala.getFormato());
        salaDTO.setSubtitulo(sala.getSubtitulo());
        
        return salaDTO;
    }

    public static List<Sala> salasDTOtoSalas(List<SalaDTO> salasDTO)
    {
        ArrayList<Sala> salas = new ArrayList<Sala>();
        
        for (SalaDTO salaDTO:salasDTO)
            salas.add(Sala.salaDTOtoSala(salaDTO));
        
        return salas;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

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

    public int getAsientos()
    {
        return asientos;
    }

    public void setAsientos(int asientos)
    {
        this.asientos = asientos;
    }

    public int getAsientosDiscapacitados()
    {
        return asientosDiscapacitados;
    }

    public void setAsientosDiscapacitados(int asientosDiscapacitados)
    {
        this.asientosDiscapacitados = asientosDiscapacitados;
    }

    public int getAsientosNoReservados()
    {
        return asientosNoReservados;
    }

    public void setAsientosNoReservados(int asientosNoReservados)
    {
        this.asientosNoReservados = asientosNoReservados;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public String getFormato()
    {
        return formato;
    }

    public void setFormato(String formato)
    {
        this.formato = formato;
    }

    public String getSubtitulo()
    {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo)
    {
        this.subtitulo = subtitulo;
    }
}