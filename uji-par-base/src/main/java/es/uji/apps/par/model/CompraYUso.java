package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompraYUso {
    private String email;
    private long compras;
    private long presentadas;

    public CompraYUso() {
    }

    public CompraYUso(String email, long compras, long presentadas) {
        this.email = email;
        this.compras = compras;
        this.presentadas = presentadas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCompras() {
        return compras;
    }

    public void setCompras(long compras) {
        this.compras = compras;
    }

    public long getPresentadas() {
        return presentadas;
    }

    public void setPresentadas(long presentadas) {
        this.presentadas = presentadas;
    }
}
