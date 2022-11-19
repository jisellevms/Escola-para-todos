package com.jisellemartins.escolaparatodos.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.aluno;
import static com.jisellemartins.escolaparatodos.Utils.UtilAutenticacao.nomeDisciplina;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.DisciplineScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.List;

public class AdapterDisciplinas extends RecyclerView.Adapter{
    private List<Disciplina> disciplinas;
    private Context context;
    private String usuario;

    public AdapterDisciplinas(Context context, List<Disciplina> disciplinas, String usuario) {
        this.context = context;
        this.disciplinas = disciplinas;
        this.usuario = usuario;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_disciplina, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Disciplina disciplina = disciplinas.get(position);
        viewHolder.btnDisciplina.setText(disciplina.getNomeDisciplina());
        if (usuario.equals(aluno)){
            viewHolder.lixeira.setVisibility(View.GONE);
        }else{
            viewHolder.lixeira.setVisibility(View.VISIBLE);
        }

        viewHolder.btnDisciplina.setOnClickListener(view -> {
            SharedPreferences sharedPref = context.getSharedPreferences("chaves", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("disciplina",disciplina.getTimestamp());
            editor.commit();
            nomeDisciplina = disciplina.getNomeDisciplina();
            Intent i = new Intent(context,
                    DisciplineScreen.class);
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return disciplinas.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnDisciplina;
        ImageView lixeira;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDisciplina = itemView.findViewById(R.id.btnDisciplina);
            lixeira = itemView.findViewById(R.id.lixeira);
        }
    }
}



