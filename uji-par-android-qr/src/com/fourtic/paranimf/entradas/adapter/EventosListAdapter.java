package com.fourtic.paranimf.entradas.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.data.Evento;

public class EventosListAdapter extends BaseAdapter
{
    private List<Evento> eventos;
    private LayoutInflater inflater;

    public EventosListAdapter(BaseNormalActivity activity)
    {
        this.eventos = new ArrayList<Evento>();
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(List<Evento> eventos)
    {
        this.eventos = eventos;
        notifyDataSetChanged();
    }

    public int getCount()
    {
        return eventos.size();
    }

    public Object getItem(int position)
    {
        return eventos.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.evento_list_item, null);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Evento evento = (Evento) getItem(position);

        viewHolder.titulo.setText(evento.getTitulo());
        //ImageLoader.getInstance().displayImage(puntoInteres.getUrlImagen(), viewHolder.imagen);

        return convertView;
    }

    private ViewHolder createViewHolder(View convertView)
    {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.titulo = (TextView) convertView.findViewById(R.id.eventoNombre);
        //viewHolder.imagen = (ImageView) convertView.findViewById(R.id.puntoInteresImagen);

        return viewHolder;
    }

    class ViewHolder
    {
        public TextView titulo;
        //public ImageView imagen;
    }

}