package es.uji.apps.par.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class CompraAbonado {

    private Abonado abonado;
    private List<Butaca> butacasSeleccionadas;

    public CompraAbonado() {
    }

    public CompraAbonado(Abonado abonado, List<Butaca> butacasSeleccionadas) {
        this.abonado = abonado;
        this.butacasSeleccionadas = butacasSeleccionadas;
    }

    public Abonado getAbonado() {
        return abonado;
    }

    public void setAbonado(Abonado abonado) {
        this.abonado = abonado;
    }

    public List<Butaca> getButacasSeleccionadas() {
        return butacasSeleccionadas;
    }

    public void setButacasSeleccionadas(String jsonButacas) {
        Gson gson = new Gson();
        List<Butaca> butacasSeleccionadas = gson.fromJson(jsonButacas, new TypeToken<List<Butaca>>(){}.getType());
        this.butacasSeleccionadas = butacasSeleccionadas;
    }
}
