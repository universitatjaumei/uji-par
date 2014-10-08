package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.exceptions.CampoRequeridoException;
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.model.TipoEvento;

@Service
public class TiposEventosService
{
    @Autowired
    private TiposEventosDAO tiposEventosDAO;

    public List<TipoEvento> getTiposEventos(String sortParameter, int start, int limit)
    {
        return tiposEventosDAO.getTiposEventos(sortParameter, start, limit);
    }

    public void removeTipoEvento(Integer id)
    {
        tiposEventosDAO.removeTipoEvento(id);
    }

    public TipoEvento addTipoEvento(TipoEvento tipoEvento) throws CampoRequeridoException
    {
        checkRequiredFields(tipoEvento);
        return tiposEventosDAO.addTipoEvento(tipoEvento);
    }

    public void updateTipoEvento(TipoEvento tipoEvento) throws CampoRequeridoException
    {
        checkRequiredFields(tipoEvento);
        tiposEventosDAO.updateTipoEvento(tipoEvento);
    }

    private void checkRequiredFields(TipoEvento tipoEvento) throws CampoRequeridoException
    {
        if (tipoEvento.getNombreEs() == null || tipoEvento.getNombreEs().isEmpty())
            throw new CampoRequeridoException("Nombre");
        
        if (tipoEvento.getExportarICAA() == null)
        	tipoEvento.setExportarICAA(false);
    }

	public int getTotalTipusEventos() {
		return tiposEventosDAO.getTotalTipusEventos();
	}
}
