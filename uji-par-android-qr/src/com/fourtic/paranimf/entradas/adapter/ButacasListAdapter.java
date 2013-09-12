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
import com.fourtic.paranimf.entradas.data.Butaca;

public class ButacasListAdapter extends BaseAdapter
{
    private List<Butaca> butacas;
    private LayoutInflater inflater;

    public ButacasListAdapter(BaseNormalActivity activity)
    {
        this.butacas = new ArrayList<Butaca>();
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(List<Butaca> butacas)
    {
        this.butacas = butacas;
        notifyDataSetChanged();
    }

    public int getCount()
    {
        return butacas.size();
    }

    public Object getItem(int position)
    {
        return butacas.get(position);
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
            convertView = inflater.inflate(R.layout.butaca_list_item, null);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Butaca butaca = (Butaca) getItem(position);

        viewHolder.idButaca.setText(butaca.getUltimoBloqueUuid());

        return convertView;
    }

    private ViewHolder createViewHolder(View convertView)
    {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.idButaca = (TextView) convertView.findViewById(R.id.idButaca);

        return viewHolder;
    }

    class ViewHolder
    {
        public TextView idButaca;
    }

}