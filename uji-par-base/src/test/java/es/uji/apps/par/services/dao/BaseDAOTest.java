package es.uji.apps.par.services.dao;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Localizacion;

public class BaseDAOTest
{
    @Autowired
    SesionesDAO sesionesDao;

    @Autowired
    LocalizacionesDAO localizacionesDao;

    protected SesionDTO preparaSesion(LocalizacionDTO localizacion)
    {
        SesionDTO sesion = sesionesDao.persistSesion(new SesionDTO());

        PreciosSesionDTO precioSesion = new PreciosSesionDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(5),
                BigDecimal.valueOf(10), localizacion);

        sesion.setParPreciosSesions(Arrays.asList(precioSesion));

        return sesion;
    }

    protected Localizacion preparaLocalizacion(String codigoLocalizacion)
    {
        Localizacion localizacion = new Localizacion();
        localizacion.setCodigo(codigoLocalizacion);
        localizacion.setTotalEntradas(10);
        
        return localizacionesDao.add(localizacion);
    }

    protected ButacaDTO preparaButaca(SesionDTO sesion, LocalizacionDTO localizacion, String fila, String numero,
            BigDecimal precio)
    {
        ButacaDTO butacaDTO = new ButacaDTO();

        butacaDTO.setFila(fila);
        butacaDTO.setNumero(numero);
        butacaDTO.setPrecio(precio);
        butacaDTO.setParSesion(sesion);
        butacaDTO.setParLocalizacion(localizacion);

        return butacaDTO;
    }

}
