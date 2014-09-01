package es.uji.apps.par.report;

import es.uji.apps.par.config.Configuration;

public class EntradaReportFactory
{
    public static EntradaReportTaquillaInterface newInstanceTaquilla()
    {
    	try {
    		return (EntradaReportTaquillaInterface) Class.forName(Configuration.getEntradaTaquillaReport()).newInstance();
    	} catch(Exception e) {
    		throw new RuntimeException("Imposible instanciar la clase del pdf de entrada de taquilla: " + Configuration.getEntradaTaquillaReport());
    	}
    }
    
    public static EntradaReportOnlineInterface newInstanceOnline()
    {
    	try {
    		return (EntradaReportOnlineInterface) Class.forName(Configuration.getEntradaOnlineReport()).newInstance();
    	} catch(Exception e) {
    		throw new RuntimeException("Imposible instanciar la clase del pdf de entrada de venta online: " + Configuration.getEntradaOnlineReport());
    	}
    }

	public static InformeInterface newInstanceInformeTaquilla() {
		try {
			return (InformeInterface) Class.forName(Configuration.getInformeTaquillaReport()).newInstance();
    	} catch(Exception e) {
    		throw new RuntimeException("Imposible instanciar la clase del pdf de informe de taquilla: " + Configuration.getInformeTaquillaReport());
    	}
	}

	public static InformeInterface newInstanceInformeEfectivo() {
		try {
    		return (InformeInterface) Class.forName(Configuration.getInformeEfectivoReport()).newInstance();
    	} catch(Exception e) {
    		throw new RuntimeException("Imposible instanciar la clase del pdf de informe de efectivo: " + Configuration.getInformeEfectivoReport());
    	}
	}

	public static InformeInterface newInstanceInformeTaquillaTpvSubtotalesReport() {
		try {
    		return (InformeInterface) Class.forName(Configuration.getInformeTaquillaTpvSubtotalesReport()).newInstance();
    	} catch(Exception e) {
    		throw new RuntimeException("Imposible instanciar la clase del pdf de informe de taquilla tpv subtotales: " + 
    				Configuration.getInformeTaquillaTpvSubtotalesReport());
    	}
	}

	public static InformeInterface newInstanceInformeEventosReport() {
		try {
    		return (InformeInterface) Class.forName(Configuration.getInformeEventosReport()).newInstance();
    	} catch(Exception e) {
    		throw new RuntimeException("Imposible instanciar la clase del pdf de informe de eventos: " + 
    				Configuration.getInformeEventosReport());
    	}
	}

	public static InformeInterface newInstanceInformeSesionReport() {
		try {
    		return (InformeInterface) Class.forName(Configuration.getInformeSesionReport()).newInstance();
    	} catch(Exception e) {
    		throw new RuntimeException("Imposible instanciar la clase del pdf de informe de sesión: " + 
    				Configuration.getInformeSesionReport());
    	}
	}
}
