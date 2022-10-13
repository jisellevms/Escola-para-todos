package com.jisellemartins.escolaparatodos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Boletim;

import java.util.List;

public class AdapterBoletim extends RecyclerView.Adapter{
    private List<Boletim> boletim;
    private Context context;

    public AdapterBoletim(Context context, List<Boletim> boletim) {
        this.context = context;
        this.boletim = boletim;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_boletim, parent, false);
        AdapterBoletim.ViewHolder holder = new AdapterBoletim.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterBoletim.ViewHolder viewHolder = (AdapterBoletim.ViewHolder) holder;
        Boletim disciplina = boletim.get(position);
        viewHolder.descricao.setText(disciplina.getDescricao());
        viewHolder.nota.setText(disciplina.getAcertos() + "/" + disciplina.getTotal());

        if (disciplina.getDescricao().length() > 15){
            viewHolder.descricao.setTextSize(Dimension.SP, 19);
        }
    }

    @Override
    public int getItemCount() {
        return boletim.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descricao, nota;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.descricao);
            nota = itemView.findViewById(R.id.telefoneP);
        }
    }
}



