package es.uji.apps.par.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.PlantaSalaDTO;

@XmlRootElement
public class PlantaSala
{
    private long id;
    private String nombre;
    private Sala sala;

    public PlantaSala()
    {
    }

    public static PlantaSala plantaSalaDTOToPlantaSala(PlantaSalaDTO plantaSalaDTO)
    {
        PlantaSala plantaSala = new PlantaSala();

        plantaSala.setId(plantaSalaDTO.getId());
        plantaSala.setNombre(plantaSalaDTO.getNombre());

        plantaSala.setSala(Sala.salaDTOtoSala(plantaSalaDTO.getParSala()));

        return plantaSala;
    }

    public static PlantaSalaDTO plantaSalaToPlantaSalaDTO(PlantaSala plantaSala)
    {
        PlantaSalaDTO plantaSalaDTO = new PlantaSalaDTO();

        plantaSalaDTO.setId(plantaSala.getId());
        plantaSalaDTO.setNombre(plantaSala.getNombre());

        plantaSalaDTO.setParSala(Sala.salaToSalaDTO(plantaSala.getSala()));

        return plantaSalaDTO;
    }

    public static List<PlantaSala> plantasSalasDTOToPlantasSalas(List<PlantaSalaDTO> plantasSalasDTO)
    {
        List<PlantaSala> plantasSalas = new ArrayList<PlantaSala>();

        for (PlantaSalaDTO plantaSalaDTO : plantasSalasDTO)
        {
            plantasSalas.add(PlantaSala.plantaSalaDTOToPlantaSala(plantaSalaDTO));
        }

        return plantasSalas;
    }

    public static List<PlantaSalaDTO> plantasSalasToPlantasSalasDTO(List<PlantaSala> plantasSalas)
    {
        List<PlantaSalaDTO> plantasSalasDTO = new ArrayList<PlantaSalaDTO>();

        for (PlantaSala plantaSala : plantasSalas)
        {
            plantasSalasDTO.add(PlantaSala.plantaSalaToPlantaSalaDTO(plantaSala));
        }

        return plantasSalasDTO;
    }

    public PlantaSala(String nombre, Sala sala)
    {
        this.nombre = nombre;
        this.sala = sala;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public Sala getSala()
    {
        return sala;
    }

    public void setSala(Sala sala)
    {
        this.sala = sala;
    }
}