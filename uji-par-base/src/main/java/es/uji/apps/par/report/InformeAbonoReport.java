package es.uji.apps.par.report;

import java.math.BigDecimal;

public class InformeAbonoReport {
    private String nombre;
    private int abonados;
    private BigDecimal total;

    public InformeAbonoReport() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAbonados() {
        return abonados;
    }

    public void setAbonados(int abonados) {
        this.abonados = abonados;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
