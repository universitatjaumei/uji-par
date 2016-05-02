package es.uji.apps.par.tpv;

import javax.ws.rs.core.Response;

public interface TpvInterface {

	public Response testTPV(long identificadorCompra) throws Exception;

	public Response compraGratuita(long identificadorCompra) throws Exception;
}

