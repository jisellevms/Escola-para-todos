package com.jisellemartins.escolaparatodos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.dialogs.DialogAula;
import com.jisellemartins.escolaparatodos.dialogs.DialogQuestão;
import com.jisellemartins.escolaparatodos.model.Numero;
import com.jisellemartins.escolaparatodos.model.Questao;

import java.util.List;

public class AdapterItemQuestao extends RecyclerView.Adapter {
    private List<Questao> questoes;
    private Context context;
    int count = 1;

    public AdapterItemQuestao(Context context, List<Questao> questoes) {
        this.context = context;
        this.questoes = questoes;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_questao, parent, false);
        AdapterItemQuestao.ViewHolder holder = new AdapterItemQuestao.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterItemQuestao.ViewHolder viewHolder = (AdapterItemQuestao.ViewHolder) holder;
        if (count >= 10) {
            viewHolder.questao.setText("Questão " + count);
        } else {
            viewHolder.questao.setText("Questão 0" + count);
        }
        count++;

        viewHolder.editar.setOnClickListener(view -> {
            DialogQuestão alert = new DialogQuestão();
            alert.showDialog((Activity) context);
        });
}

    @Override
    public int getItemCount() {
        return questoes.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView questao;
    ImageView status, editar, excluir;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        questao = itemView.findViewById(R.id.questao);
        status = itemView.findViewById(R.id.status);
        editar = itemView.findViewById(R.id.editarQ);
        excluir = itemView.findViewById(R.id.excluirQ);
    }
}
}