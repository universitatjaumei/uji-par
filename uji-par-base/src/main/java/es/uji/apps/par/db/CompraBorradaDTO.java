package es.uji.apps.par.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PAR_COMPRAS_BORRADAS")
public class CompraBorradaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "PAR_COMPRAS_BORRADAS_ID_GENERATOR", sequenceName = "HIBERNATE_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAR_COMPRAS_BORRADAS_ID_GENERATOR")
    private long id;

    @Column(name = "COMPRA_ID")
    private Long compraId;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDOS")
    private String apellidos;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "POBLACION")
    private String poblacion;

    @Column(name = "CP")
    private String cp;

    @Column(name = "PROVINCIA")
    private String provincia;

    @Column(name = "TFNO")
    private String telefono;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "INFO_PERIODICA")
    private Boolean infoPeriodica;

    @Column(name = "FECHA")
    private Timestamp fecha;

    @Column(name = "TAQUILLA")
    private Boolean taquilla;

    @Column(name = "IMPORTE")
    private BigDecimal importe;

    @Column(name = "REFERENCIA_PAGO")
    private String referenciaPago;

    @Column(name = "TIPO")
    private String tipoPago;

    @Column(name = "PORCENTAJE_IVA")
    private BigDecimal porcentajeIva;

    @ManyToOne
    @JoinColumn(name="SESION_ID")
    private SesionDTO parSesion;

    @Column(name = "CODIGO_PAGO_TARJETA")
    private String codigoPagoTarjeta;

    @Column(name = "CODIGO_PAGO_PASARELA")
    private String codigoPagoPasarela;

    @Column(name = "PAGADA")
    private Boolean pagada;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "RESERVA")
    private Boolean reserva;

    @Column(name = "DESDE")
    private Timestamp desde;

    @Column(name = "HASTA")
    private Timestamp hasta;

    @Column(name = "OBSERVACIONES_RESERVA")
    private String observacionesReserva;

    @Column(name = "ANULADA")
    private Boolean anulada;

    @Column(name = "RECIBO_PINPAD")
    private String reciboPinpad;

    @Column(name = "CADUCADA")
    private Boolean caducada;

    @Column(name = "ABONADO_ID")
    private Long abonadoId;

    public CompraBorradaDTO() {
    }

    public CompraBorradaDTO(CompraDTO compra) {
        this.compraId = compra.getId();
        this.nombre = compra.getNombre();
        this.apellidos = compra.getApellidos();
        this.direccion = compra.getDireccion();
        this.poblacion = compra.getPoblacion();
        this.cp = compra.getCp();
        this.provincia = compra.getProvincia();
        this.telefono = compra.getTelefono();
        this.email = compra.getEmail();
        this.infoPeriodica = compra.getInfoPeriodica();
        this.fecha = compra.getFecha();
        this.taquilla = compra.getTaquilla();
        this.importe = compra.getImporte();
        this.referenciaPago = compra.getReferenciaPago();
        this.tipoPago = compra.getTipoPago();
        this.porcentajeIva = compra.getPorcentajeIva();
        this.parSesion = compra.getParSesion();
        this.codigoPagoTarjeta = compra.getCodigoPagoTarjeta();
        this.codigoPagoPasarela = compra.getCodigoPagoPasarela();
        this.pagada = compra.getPagada();
        this.uuid = compra.getUuid();
        this.reserva = compra.getReserva();
        this.desde = compra.getDesde();
        this.hasta = compra.getHasta();
        this.observacionesReserva = compra.getObservacionesReserva();
        this.anulada = compra.getAnulada();
        this.reciboPinpad = compra.getReciboPinpad();
        this.caducada = compra.getCaducada();
        this.abonadoId = compra.getParAbonado() != null ? compra.getParAbonado().getId() : null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
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

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Boolean getTaquilla() {
        return taquilla;
    }

    public void setTaquilla(Boolean taquilla) {
        this.taquilla = taquilla;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getReferenciaPago() {
        return referenciaPago;
    }

    public void setReferenciaPago(String referenciaPago) {
        this.referenciaPago = referenciaPago;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public BigDecimal getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(BigDecimal porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public String getCodigoPagoTarjeta() {
        return codigoPagoTarjeta;
    }

    public void setCodigoPagoTarjeta(String codigoPagoTarjeta) {
        this.codigoPagoTarjeta = codigoPagoTarjeta;
    }

    public String getCodigoPagoPasarela() {
        return codigoPagoPasarela;
    }

    public void setCodigoPagoPasarela(String codigoPagoPasarela) {
        this.codigoPagoPasarela = codigoPagoPasarela;
    }

    public Boolean getPagada() {
        return pagada;
    }

    public void setPagada(Boolean pagada) {
        this.pagada = pagada;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getReserva() {
        return reserva;
    }

    public void setReserva(Boolean reserva) {
        this.reserva = reserva;
    }

    public Timestamp getDesde() {
        return desde;
    }

    public void setDesde(Timestamp desde) {
        this.desde = desde;
    }

    public Timestamp getHasta() {
        return hasta;
    }

    public void setHasta(Timestamp hasta) {
        this.hasta = hasta;
    }

    public String getObservacionesReserva() {
        return observacionesReserva;
    }

    public void setObservacionesReserva(String observacionesReserva) {
        this.observacionesReserva = observacionesReserva;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    public String getReciboPinpad() {
        return reciboPinpad;
    }

    public void setReciboPinpad(String reciboPinpad) {
        this.reciboPinpad = reciboPinpad;
    }

    public Boolean getCaducada() {
        return caducada;
    }

    public void setCaducada(Boolean caducada) {
        this.caducada = caducada;
    }

    public Long getAbonadoId() {
        return abonadoId;
    }

    public void setAbonadoId(Long abonadoId) {
        this.abonadoId = abonadoId;
    }

    public SesionDTO getParSesion() {
        return parSesion;
    }

    public void setParSesion(SesionDTO parSesion) {
        this.parSesion = parSesion;
    }
}