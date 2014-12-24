package es.uji.apps.par.model;

import es.uji.apps.par.db.TpvsDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Tpv {
    private long id;
    private String nombre;

    public Tpv()
    {
    }

    public Tpv(TpvsDTO tpv)
    {
        this.id = tpv.getId();
        this.nombre = tpv.getNombre();
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

    public static TpvsDTO tpvToTpvDTO(Tpv parTpv) {
        TpvsDTO tpvDTO = new TpvsDTO();

        tpvDTO.setId(parTpv.getId());
        tpvDTO.setNombre(parTpv.getNombre());

        return tpvDTO;
    }
}
