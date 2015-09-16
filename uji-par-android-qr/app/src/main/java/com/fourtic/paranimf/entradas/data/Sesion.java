package com.fourtic.paranimf.entradas.data;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Sesion
{
    @DatabaseField(columnName = "id", id = true)
    private int id;

    @SerializedName("fechaCelebracion")
    private long fechaCelebracionEpoch;

    @DatabaseField(columnName = "fecha")
    private Date fecha;

    @DatabaseField(foreign = true)
    private Evento evento;

    @DatabaseField(columnName = "fecha_sync")
    private Date fechaSync;

    @DatabaseField(columnName = "hora")
    private String horaCelebracion;

    private boolean modificado;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public long getFechaCelebracionEpoch()
    {
        return fechaCelebracionEpoch;
    }

    public void setFechaCelebracionEpoch(long fechaCelebracionEpoch)
    {
        this.fechaCelebracionEpoch = fechaCelebracionEpoch;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public Evento getEvento()
    {
        return evento;
    }

    public void setEvento(Evento evento)
    {
        this.evento = evento;
    }

    public Date getFechaSync()
    {
        return fechaSync;
    }

    public void setFechaSync(Date fechaSync)
    {
        this.fechaSync = fechaSync;
    }

    public String getHoraCelebracion()
    {
        return horaCelebracion;
    }

    public void setHoraCelebracion(String horaCelebracion)
    {
        this.horaCelebracion = horaCelebracion;
    }

    public boolean getModificado()
    {
        return modificado;
    }

    public void setModificado(boolean modificado)
    {
        this.modificado = modificado;
    }
}
