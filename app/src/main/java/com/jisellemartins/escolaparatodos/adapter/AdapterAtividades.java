package com.jisellemartins.escolaparatodos.adapter;

import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.AtividadeQuestaoScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Atividade;

import java.util.List;

public class AdapterAtividades extends RecyclerView.Adapter {
    private List<Atividade> atividades;
    private Context context;
    private String usuario;


    public AdapterAtividades(Context context, List<Atividade> atividades, String usuario) {
        this.context = context;
        this.atividades = atividades;
        this.usuario = usuario;

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
        viewHolder.qtdQuestoesAtv.setText("Quantidade de questões: " + atividade.getQtdQuestoes());
        viewHolder.statusAtv.setText("Status da atividade: " + atividade.getStatus());

        if (usuario.equals(aluno)) {
            if (atividade.getStatus().equals("Concluido")) {
                viewHolder.iconStatus.setImageResource(R.drawable.check);
            } else {
                viewHolder.iconStatus.setImageResource(R.drawable.aviso);
            }
        } else {
            viewHolder.iconStatus.setImageResource(R.drawable.lixeira_braca);
        }

        viewHolder.item_atividade.setOnClickListener(view -> {
            Intent i = new Intent(context,
                    AtividadeQuestaoScreen.class);
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descricaoAtv, dataAtv, qtdQuestoesAtv, statusAtv;
        ImageView iconStatus;
        ConstraintLayout item_atividade;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricaoAtv = itemView.findViewById(R.id.descricaoAtv);
            dataAtv = itemView.findViewById(R.id.dataAtv);
            qtdQuestoesAtv = itemView.findViewById(R.id.qtdQuestoesAtv);
            statusAtv = itemView.findViewById(R.id.statusAtv);
            iconStatus = itemView.findViewById(R.id.iconStatus);
            item_atividade = itemView.findViewById(R.id.item_atividade);
        }
    }
}

