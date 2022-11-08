package com.jisellemartins.escolaparatodos.adapter;

import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Conteudo;

import java.util.List;

public class AdapterConteudo extends RecyclerView.Adapter{
    private List<Conteudo> conteudos;
    private Context context;
    private String usuario;


    public AdapterConteudo(Context context, List<Conteudo> conteudos, String usuario) {
        this.context = context;
        this.conteudos = conteudos;
        this.usuario = usuario;

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

        if (usuario.equals(aluno)){
            viewHolder.btnLixeira.setVisibility(View.GONE);
        }else{
            viewHolder.btnLixeira.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return conteudos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnDownload;
        TextView descricao, tamanho;
        ImageView imgIcon, btnLixeira;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDownload = itemView.findViewById(R.id.btnAudio);
            descricao = itemView.findViewById(R.id.descricaoAula);
            tamanho = itemView.findViewById(R.id.tamanho);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            btnLixeira = itemView.findViewById(R.id.btnLixeira);
        }
    }
}