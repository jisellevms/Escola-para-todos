package com.jisellemartins.escolaparatodos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.DisciplineScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Conteudo;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.List;

public class AdapterConteudo extends RecyclerView.Adapter{
    private List<Conteudo> conteudos;
    private Context context;

    public AdapterConteudo(Context context, List<Conteudo> conteudos) {
        this.context = context;
        this.conteudos = conteudos;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conteudo, parent, false);
        AdapterConteudo.ViewHolder holder = new AdapterConteudo.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterConteudo.ViewHolder viewHolder = (AdapterConteudo.ViewHolder) holder;
        Conteudo conteudo = conteudos.get(position);
        viewHolder.descricao.setText(conteudo.getDescricao());
        viewHolder.tamanho.setText("Tamanho: " + conteudo.getTamanho());
        viewHolder.imgIcon.setImageResource(conteudo.getIcon());
    }

    @Override
    public int getItemCount() {
        return conteudos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnDownload;
        TextView descricao, tamanho;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            descricao = itemView.findViewById(R.id.descricao);
            tamanho = itemView.findViewById(R.id.tamanho);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}