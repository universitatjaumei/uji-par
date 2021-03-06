package es.uji.apps.par.ficheros.registros;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.uji.apps.par.exceptions.RegistroSerializaException;
import es.uji.apps.par.model.Cine;
import es.uji.apps.par.model.Sesion;

public class RegistroBuzon
{
    private String codigo;
    private String tipo;
    private Date fechaEnvioHabitualAnterior;
    private Date fechaEnvio;
    private int lineas;
    private int sesiones;
    private int espectadores;
    private BigDecimal recaudacion;

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public Date getFechaEnvioHabitualAnterior()
    {
        return fechaEnvioHabitualAnterior;
    }

    public void setFechaEnvioHabitualAnterior(Date fechaEnvioHabitualAnterior)
    {
        this.fechaEnvioHabitualAnterior = fechaEnvioHabitualAnterior;
    }

    public Date getFechaEnvio()
    {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio)
    {
        this.fechaEnvio = fechaEnvio;
    }

    public int getLineas()
    {
        return lineas;
    }

    public void setLineas(int lineas)
    {
        this.lineas = lineas;
    }

    public int getSesiones()
    {
        return sesiones;
    }

    public void setSesiones(int sesiones)
    {
        this.sesiones = sesiones;
    }

    public int getEspectadores()
    {
        return espectadores;
    }

    public void setEspectadores(int espectadores)
    {
        this.espectadores = espectadores;
    }

    public BigDecimal getRecaudacion()
    {
        return recaudacion;
    }

    public void setRecaudacion(BigDecimal recaudacion)
    {
        this.recaudacion = recaudacion;
    }

    public String serializa() throws RegistroSerializaException
    {
    	Cine.checkValidity(codigo);
    	Sesion.checkTipoEnvio(tipo);

    	Calendar calDiaAnterior = Calendar.getInstance();
        
        if (fechaEnvioHabitualAnterior == null)
            calDiaAnterior.setTime(fechaEnvio);
        else
            calDiaAnterior.setTime(fechaEnvioHabitualAnterior);

        Calendar calDia = Calendar.getInstance();
        calDia.setTime(fechaEnvio);

        String result = String.format(Locale.ENGLISH, "0%s%s%03d%03d%011d%011d%011d%011.2f", codigo, tipo,
                calDiaAnterior.get(Calendar.DAY_OF_YEAR), calDia.get(Calendar.DAY_OF_YEAR), lineas, sesiones,
                espectadores, recaudacion);

        return result;
    }

}
