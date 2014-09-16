package es.uji.apps.par.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.model.TipoInforme;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class InformesService
{
    public List<TipoInforme> getTiposInforme()
    {
        String tiposInforme = Configuration.getTiposInforme();
        Type listType = new TypeToken<ArrayList<TipoInforme>>() {}.getType();
        List<TipoInforme> tiposInformeDisponibles = new Gson().fromJson(tiposInforme, listType);

        for (TipoInforme tipoInformeDisponible:tiposInformeDisponibles) {
            tipoInformeDisponible.setNombre(tipoInformeDisponible.getNombreCA());
        }

        return tiposInformeDisponibles;
    }
}
