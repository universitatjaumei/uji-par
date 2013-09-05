package com.fourtic.paranimf.entradas.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ResponseEventos
{
    private boolean success;
    private int total;

    @SerializedName("data")
    private List<Evento> eventos;

    public ResponseEventos()
    {
        eventos = new ArrayList<Evento>();
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public List<Evento> getEventos()
    {
        return eventos;
    }

    public void setEventos(List<Evento> eventos)
    {
        this.eventos = eventos;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

}
