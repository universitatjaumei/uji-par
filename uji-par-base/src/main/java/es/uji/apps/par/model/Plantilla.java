package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.PlantillaDTO;

@XmlRootElement
public class Plantilla {
	private long id;
	private String nombre;
	
	public Plantilla() {
		
	}
	
	public Plantilla(int idPlantilla) {
		this.id = idPlantilla;
	}
	
	public Plantilla(String nombre)
    {
        this.nombre = nombre;
    }
	
	public static Plantilla plantillaPreciosDTOtoPlantillaPrecios(PlantillaDTO plantillaDTO) {
		Plantilla plantillaPrecios = new Plantilla();
		plantillaPrecios.setId(plantillaDTO.getId());
		plantillaPrecios.setNombre(plantillaDTO.getNombre());
		
		return plantillaPrecios;
	}
	
	public static PlantillaDTO plantillaPreciosToPlantillaPreciosDTO(Plantilla plantillaPrecios) {
		PlantillaDTO plantillaDTO = new PlantillaDTO();
		plantillaDTO.setId(plantillaPrecios.getId());
		plantillaDTO.setNombre(plantillaPrecios.getNombre());
		
		return plantillaDTO;
	}

    public Plantilla(PlantillaDTO plantillaDTO)
    {
        this.id = plantillaDTO.getId();
        this.nombre = plantillaDTO.getNombre();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
