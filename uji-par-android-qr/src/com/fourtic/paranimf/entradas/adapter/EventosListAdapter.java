package com.fourtic.paranimf.entradas.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.data.Evento;
import com.fourtic.paranimf.entradas.data.Sesion;

public class EventosListAdapter extends BaseAdapter implements Filterable {
	private List<Evento> totalEventos;
	private List<Evento> eventos;
	private LayoutInflater inflater;

	public EventosListAdapter(BaseNormalActivity activity) {
		this.eventos = new ArrayList<Evento>();
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void update(List<Evento> eventos) {
		this.eventos = eventos;
		this.totalEventos = eventos;
		notifyDataSetChanged();
	}

	public int getCount() {
		return eventos.size();
	}

	public Object getItem(int position) {
		return eventos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.evento_list_item, null);
			viewHolder = createViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Evento evento = (Evento) getItem(position);

		viewHolder.titulo.setText(evento.getTitulo());
		viewHolder.imagenModificado
				.setVisibility(evento.getModificado() ? View.VISIBLE
						: View.INVISIBLE);

		return convertView;
	}

	private ViewHolder createViewHolder(View convertView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.titulo = (TextView) convertView
				.findViewById(R.id.eventoNombre);
		viewHolder.imagenModificado = (ImageView) convertView
				.findViewById(R.id.eventoImagenModificado);

		return viewHolder;
	}

	class ViewHolder {
		public ImageView imagenModificado;
		public TextView titulo;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				eventos = (List<Evento>) results.values;
				notifyDataSetChanged();
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				ArrayList<Evento> filteredEventos = new ArrayList<Evento>();
				
				Calendar cal = Calendar.getInstance();
				
				cal.setTime(new Date());
				cal.add(Calendar.HOUR, -12);
			    Date min = cal.getTime();
			    
			    cal.setTime(new Date());
				cal.add(Calendar.HOUR, +12);
			    Date max = cal.getTime();

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < totalEventos.size(); i++) {
					Evento evento = totalEventos.get(i);
					for (Sesion sesion: evento.getSesionesLazy())
					{
						if (sesion.getFecha().after(min) && sesion.getFecha().before(max))
						{
							filteredEventos.add(evento);
							break;
						}
					}
				}

				results.count = filteredEventos.size();
				results.values = filteredEventos;

				return results;
			}
		};

		return filter;
	}
}