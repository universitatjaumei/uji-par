package es.uji.apps.par.report;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import es.uji.apps.fopreports.serialization.ReportSerializationException;
import es.uji.apps.par.SinIvaException;
import es.uji.apps.par.model.Cine;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Sala;
import es.uji.apps.par.model.Sesion;

public interface InformeInterface {
	public InformeInterface create(Locale locale);
	public void serialize(OutputStream output) throws ReportSerializationException;
	public void genera(String inicio, String fin, List<InformeModelReport> compras, BigDecimal totalTaquillaTPV,
            BigDecimal totalTaquillaEfectivo, BigDecimal totalOnline);
	public void genera(String inicio, String fin, List<InformeModelReport> compras, String cargoInformeEfectivo, 
    		String firmanteInformeEfectivo) throws SinIvaException;
	public void genera(String cargo, String firmante, Cine cine, Sala sala, Evento evento, Sesion sesion, 
			Integer vendidas, Integer anuladas, BigDecimal total) throws SinIvaException;
	public void genera(String inicio, String fin, List<InformeModelReport> compras) throws SinIvaException;
}
