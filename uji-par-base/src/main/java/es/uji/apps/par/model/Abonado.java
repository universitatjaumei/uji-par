package es.uji.apps.par.model;

import es.uji.apps.par.db.AbonadoDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class Abonado {

    private long id;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String poblacion;
    private String cp;
    private String provincia;
    private String telefono;
    private String email;
    private Boolean infoPeriodica;
    private Boolean anulado;
    private BigDecimal importe;
    private Abono abono;

    public Abonado()
    {
    }

    public Abonado(AbonadoDTO abonadoDTO) {
        this.id = abonadoDTO.getId();
        this.nombre = abonadoDTO.getNombre();
        this.apellidos = abonadoDTO.getApellidos();
        this.direccion = abonadoDTO.getDireccion();
        this.poblacion = abonadoDTO.getPoblacion();
        this.cp = abonadoDTO.getCp();
        this.provincia = abonadoDTO.getProvincia();
        this.telefono = abonadoDTO.getTelefono();
        this.email = abonadoDTO.getEmail();
        this.infoPeriodica = abonadoDTO.getInfoPeriodica();
        this.anulado = abonadoDTO.getAnulado();
        this.importe = abonadoDTO.getImporte();
        this.abono = Abono.AbonoDTOToAbono(abonadoDTO.getParAbono());
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Abono getAbono() {
        return abono;
    }

    public void setAbono(Abono abono) {
        this.abono = abono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getInfoPeriodica() {
        return infoPeriodica;
    }

    public void setInfoPeriodica(Boolean infoPeriodica) {
        this.infoPeriodica = infoPeriodica;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Boolean getAnulado() {
        return anulado;
    }

    public void setAnulado(Boolean anulado) {
        this.anulado = anulado;
    }
}
