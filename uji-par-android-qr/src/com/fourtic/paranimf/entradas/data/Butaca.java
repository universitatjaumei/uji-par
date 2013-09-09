package com.fourtic.paranimf.entradas.data;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Butaca
{
    @DatabaseField(columnName = "id", id = true)
    private int id;

    @SerializedName("fila")
    @DatabaseField(columnName = "fila")
    private String fila;

    @SerializedName("numero")
    @DatabaseField(columnName = "numero")
    private String numero;

    @SerializedName("uuid")
    @DatabaseField(columnName = "uuid")
    private String uuid;

    @SerializedName("presentada")
    private long fechaPresentadaEpoch;

    @DatabaseField(columnName = "presentada")
    private Date fechaPresentada;

    @DatabaseField(columnName = "modificada")
    private boolean modificada;

    @DatabaseField(foreign = true)
    private Sesion sesion;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFila()
    {
        return fila;
    }

    public void setFila(String fila)
    {
        this.fila = fila;
    }

    public String getNumero()
    {
        return numero;
    }

    public void setNumero(String numero)
    {
        this.numero = numero;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public long getFechaPresentadaEpoch()
    {
        return fechaPresentadaEpoch;
    }

    public void setFechaPresentadaEpoch(long fechaPresentadaEpoch)
    {
        this.fechaPresentadaEpoch = fechaPresentadaEpoch;
    }

    public Date getFechaPresentada()
    {
        return fechaPresentada;
    }

    public void setFechaPresentada(Date fechaPresentada)
    {
        this.fechaPresentada = fechaPresentada;
    }

    public Sesion getSesion()
    {
        return sesion;
    }

    public void setSesion(Sesion sesion)
    {
        this.sesion = sesion;
    }

    public boolean isModificada()
    {
        return modificada;
    }

    public void setModificada(boolean modificada)
    {
        this.modificada = modificada;
    }
}
