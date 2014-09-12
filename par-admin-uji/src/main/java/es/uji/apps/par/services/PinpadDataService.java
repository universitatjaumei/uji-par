package es.uji.apps.par.services;

import java.math.BigDecimal;

public interface PinpadDataService
{

    public String consultaEstado(String id);

    public String realizaPago(String id, BigDecimal importe, String concepto);

}