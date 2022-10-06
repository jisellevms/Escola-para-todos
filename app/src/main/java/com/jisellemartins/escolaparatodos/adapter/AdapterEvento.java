package com.jisellemartins.escolaparatodos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Atividade;
import com.jisellemartins.escolaparatodos.model.Evento;

import java.util.List;

public class AdapterEvento extends RecyclerView.Adapter{
    private List<Evento> eventos;
    private Context context;

    public AdapterEvento(Context context, List<Evento> eventos) {
        this.context = context;
        this.eventos = eventos;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false);
        AdapterEvento.ViewHolder holder = new AdapterEvento.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterEvento.ViewHolder viewHolder = (AdapterEvento.ViewHolder) holder;
        Evento evento = eventos.get(position);
        viewHolder.dataEvento.setText("DIA\n" + evento.getData());
        viewHolder.descEvento.setText(evento.getDescricao());
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dataEvento, descEvento;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dataEvento = itemView.findViewById(R.id.dataEvento);
            descEvento = itemView.findViewById(R.id.descEvento);
        }
    }
}


