package com.jisellemartins.escolaparatodos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Aula;

import java.util.List;

public class AdapterAulas extends RecyclerView.Adapter{
    private List<Aula> aulas;
    private Context context;

    public AdapterAulas(Context context, List<Aula> aulas) {
        this.context = context;
        this.aulas = aulas;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_aula, parent, false);
        AdapterAulas.ViewHolder holder = new AdapterAulas.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterAulas.ViewHolder viewHolder = (AdapterAulas.ViewHolder) holder;
        Aula aula = aulas.get(position);
        viewHolder.descricaoAula.setText(aula.getDescricao());


    }

    @Override
    public int getItemCount() {
        return aulas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descricaoAula;
        Button btnAudio, btnTexto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricaoAula = itemView.findViewById(R.id.descricaoAula);
            btnAudio = itemView.findViewById(R.id.btnAudio);
            btnTexto = itemView.findViewById(R.id.btnTexto);
        }
    }
}



