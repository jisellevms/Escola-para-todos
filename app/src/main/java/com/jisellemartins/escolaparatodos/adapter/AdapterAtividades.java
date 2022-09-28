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

import java.util.List;

public class AdapterAtividades extends RecyclerView.Adapter{
    private List<Atividade> atividades;
    private Context context;

    public AdapterAtividades(Context context, List<Atividade> atividades) {
        this.context = context;
        this.atividades = atividades;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_atividade, parent, false);
        AdapterAtividades.ViewHolder holder = new AdapterAtividades.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Atividade atividade = atividades.get(position);
        viewHolder.descricaoAtv.setText(atividade.getDescricao());
        viewHolder.dataAtv.setText("Data final: " + atividade.getData());
        viewHolder.qtdQuestoesAtv.setText("Quantidade de questões: " +atividade.getQtdQuestoes());
        viewHolder.statusAtv.setText("Status da atividade: " + atividade.getStatus());

        if (atividade.getStatus().equals("Pendente")){
            viewHolder.iconStatus.setImageResource(R.drawable.aviso);
        }else if(atividade.getStatus().equals("Concluido")){
            viewHolder.iconStatus.setImageResource(R.drawable.check);
        }else{
            viewHolder.iconStatus.setImageResource(R.drawable.lixeira_braca);
        }


    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descricaoAtv, dataAtv, qtdQuestoesAtv, statusAtv;
        ImageView iconStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricaoAtv = itemView.findViewById(R.id.descricaoAtv);
            dataAtv = itemView.findViewById(R.id.dataAtv);
            qtdQuestoesAtv = itemView.findViewById(R.id.qtdQuestoesAtv);
            statusAtv = itemView.findViewById(R.id.statusAtv);
            iconStatus = itemView.findViewById(R.id.iconStatus);
        }
    }
}

