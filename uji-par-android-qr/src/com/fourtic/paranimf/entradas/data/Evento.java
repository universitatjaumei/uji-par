package com.fourtic.paranimf.entradas.data;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Evento
{
    @DatabaseField(columnName = "id")
    private int id;

    @SerializedName("tituloEs")
    @DatabaseField(columnName = "titulo")
    private String titulo;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }
}
