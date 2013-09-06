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

        // Cuando se rellena fechaCelebracionEpoch por deserialización REST se actualiza fechaCelebracion
        this.fecha = new Date(fechaCelebracionEpoch);
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
    
}
