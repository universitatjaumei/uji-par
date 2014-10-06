package es.uji.apps.par.services.dao;

import es.uji.apps.par.dao.*;
import es.uji.apps.par.db.*;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.TipoEvento;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;

public class BaseDAOTest
{
    @Autowired
    EventosDAO eventosDao;
    
    @Autowired
    SesionesDAO sesionesDao;

    @Autowired
    LocalizacionesDAO localizacionesDao;

    @Autowired
    TarifasDAO tarifasDAO;

    @Autowired
    TiposEventosDAO tiposEventosDAO;

    protected SesionDTO preparaSesion(LocalizacionDTO localizacion)
    {
        TipoEvento tipoEvento = new TipoEvento();
        tipoEvento.setNombreEs("Tipo Es");
        tipoEvento.setNombreVa("Tipo Va");

        tiposEventosDAO.addTipoEvento(tipoEvento);

        Evento evento = new Evento();
        evento.setAsientosNumerados(true);
        evento.setParTipoEvento(tipoEvento);
        evento = eventosDao.addEvento(evento);
        
        SesionDTO sesionDTO = new SesionDTO();
        sesionDTO.setParEvento(Evento.eventoToEventoDTO(evento));
        sesionDTO.setFechaCelebracion(new Timestamp(100));
        sesionDTO.setFechaInicioVentaOnline(new Timestamp(0));
        sesionDTO.setFechaFinVentaOnline(new Timestamp(1));
        
        SesionDTO sesion = sesionesDao.persistSesion(sesionDTO);

        PreciosSesionDTO precioSesion = new PreciosSesionDTO(BigDecimal.valueOf(1), BigDecimal.valueOf(5),
                BigDecimal.valueOf(10), localizacion);

        TarifaDTO tarifaDTO = new TarifaDTO();
        tarifaDTO.setNombre("tarifa1");
        tarifaDTO.setIsPublica(false);
        tarifasDAO.persistTarifa(tarifaDTO);

        precioSesion.setParTarifa(tarifaDTO);
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
    
    protected void setSesionNoNumerada(SesionDTO sesion)
    {
        EventoDTO evento = sesion.getParEvento();
        evento.setAsientosNumerados(false);
        
        eventosDao.updateEvento(Evento.eventoDTOtoEvento(evento));        
    }

    protected ButacaDTO preparaButaca(SesionDTO sesion, LocalizacionDTO localizacion, String fila, String numero,
            BigDecimal precio)
    {
        ButacaDTO butacaDTO = new ButacaDTO();

        butacaDTO.setAnulada(false);
        butacaDTO.setFila(fila);
        butacaDTO.setNumero(numero);
        butacaDTO.setPrecio(precio);
        butacaDTO.setParSesion(sesion);
        butacaDTO.setParLocalizacion(localizacion);

        return butacaDTO;
    }

}
