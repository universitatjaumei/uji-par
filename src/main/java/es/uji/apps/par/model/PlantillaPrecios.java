package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

import es.uji.apps.par.db.PlantillaPreciosDTO;

@XmlRootElement
public class PlantillaPrecios {
	private long id;
	private String nombre;
	
	public PlantillaPrecios() {
		
	}
	
	public PlantillaPrecios(int idPlantilla) {
		this.id = idPlantilla;
	}
	
	public PlantillaPrecios(String nombre)
    {
        this.nombre = nombre;
    }
	
	public static PlantillaPrecios plantillaPreciosDTOtoPlantillaPrecios(PlantillaPreciosDTO plantillaPreciosDTO) {
		PlantillaPrecios plantillaPrecios = new PlantillaPrecios();
		plantillaPrecios.setId(plantillaPreciosDTO.getId());
		plantillaPrecios.setNombre(plantillaPreciosDTO.getNombre());
		
		return plantillaPrecios;
	}
	
	public static PlantillaPreciosDTO plantillaPreciosToPlantillaPreciosDTO(PlantillaPrecios plantillaPrecios) {
		PlantillaPreciosDTO plantillaPreciosDTO = new PlantillaPreciosDTO();
		plantillaPreciosDTO.setId(plantillaPrecios.getId());
		plantillaPreciosDTO.setNombre(plantillaPrecios.getNombre());
		
		return plantillaPreciosDTO;
	}

    public PlantillaPrecios(PlantillaPreciosDTO plantillaPreciosDTO)
    {
        this.id = plantillaPreciosDTO.getId();
        this.nombre = plantillaPreciosDTO.getNombre();
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
