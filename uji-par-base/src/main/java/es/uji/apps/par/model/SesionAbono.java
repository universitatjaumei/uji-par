package es.uji.apps.par.model;

import es.uji.apps.par.db.SesionAbonoDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SesionAbono {

    private long id;
    private Sesion sesion;
    private Abono abono;

    public SesionAbono()
    {
    }

    public SesionAbono(SesionAbonoDTO sesionAbonoDTO) {
        this.id = sesionAbonoDTO.getId();
        this.sesion = Sesion.SesionDTOToSesion(sesionAbonoDTO.getParSesion());
        this.abono =  Abono.AbonoDTOToAbono(sesionAbonoDTO.getParAbono());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public Abono getAbono() {
        return abono;
    }

    public void setAbono(Abono abono) {
        this.abono = abono;
    }
}
