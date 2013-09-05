package com.fourtic.paranimf.entradas.data;

import com.google.gson.annotations.SerializedName;

public class Evento
{
    private int id;

    @SerializedName("tituloEs")
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
