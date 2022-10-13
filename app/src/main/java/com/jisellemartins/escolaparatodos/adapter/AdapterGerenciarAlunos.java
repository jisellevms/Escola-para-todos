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
import com.jisellemartins.escolaparatodos.model.Aluno;

import java.util.List;

public class AdapterGerenciarAlunos extends RecyclerView.Adapter{
    private List<Aluno> alunos;
    private Context context;

    public AdapterGerenciarAlunos(Context context, List<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_aluno, parent, false);
        AdapterGerenciarAlunos.ViewHolder holder = new AdapterGerenciarAlunos.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterGerenciarAlunos.ViewHolder viewHolder = (AdapterGerenciarAlunos.ViewHolder) holder;
        Aluno aluno = alunos.get(position);
        viewHolder.nomeP.setText(aluno.getNome());
        viewHolder.telefoneP.setText(aluno.getTelefone());
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomeP, telefoneP;
        Button excluirP, editarP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeP = itemView.findViewById(R.id.nomeP);
            telefoneP = itemView.findViewById(R.id.telefoneP);
            excluirP = itemView.findViewById(R.id.excluirP);
            editarP = itemView.findViewById(R.id.editarP);
        }
    }
}