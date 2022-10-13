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
import com.jisellemartins.escolaparatodos.model.Aluno;

import java.util.List;

public class AdapterBoletimProfessor extends RecyclerView.Adapter{
    private List<Aluno> alunos;
    private Context context;

    public AdapterBoletimProfessor(Context context, List<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_boletim_professor, parent, false);
        AdapterBoletimProfessor.ViewHolder holder = new AdapterBoletimProfessor.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterBoletimProfessor.ViewHolder viewHolder = (AdapterBoletimProfessor.ViewHolder) holder;
        Aluno aluno = alunos.get(position);
        viewHolder.nome.setText(aluno.getNome());
        viewHolder.telefone.setText(aluno.getTelefone());
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome, telefone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome);
            telefone = itemView.findViewById(R.id.telefone);
        }
    }
}



