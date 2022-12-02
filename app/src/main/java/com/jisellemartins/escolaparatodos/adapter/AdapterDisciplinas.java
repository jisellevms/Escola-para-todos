package com.jisellemartins.escolaparatodos.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.Utils.Utils.TAG_EPT;
import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;
import static com.jisellemartins.escolaparatodos.Utils.Utils.nomeDisciplina;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jisellemartins.escolaparatodos.DisciplineScreen;
import com.jisellemartins.escolaparatodos.DisciplinesScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Disciplina;

import java.util.List;

public class AdapterDisciplinas extends RecyclerView.Adapter{
    private List<Disciplina> disciplinas;
    private Context context;
    private String usuario;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DisciplinesScreen ds;

    public AdapterDisciplinas(Context context, List<Disciplina> disciplinas, String usuario, DisciplinesScreen ds) {
        this.context = context;
        this.disciplinas = disciplinas;
        this.usuario = usuario;
        this.ds = ds;

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

        viewHolder.lixeira.setOnClickListener(view -> {
            deletarDisciplina(disciplina.getTimestamp());
        });

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

    public void deletarDisciplina(String disciplina) {

        //SharedPreferences sharedPref = context.getSharedPreferences("chaves", MODE_PRIVATE);
        //String disciplinaTime = sharedPref.getString("disciplina", "");

        // PROCURAR A Disciplina
        db.collection("Disciplina").whereEqualTo("dataCriacao", disciplina).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().size() > 0) {

                // DELETAR A Disciplina
                db.collection("Disciplina").document(task.getResult().getDocuments().get(0).getId()).delete().addOnSuccessListener(aVoid -> {
                    Log.d(TAG_EPT, "A Disciplina foi apagada!");
                    ds.showDisciplinas();

                }).addOnFailureListener(e -> {
                    Log.w(TAG_EPT, "Error ao apagar disciplina ", e);
                });

            } else if (task.getResult().size() == 0) {
                Log.i(TAG_EPT, "A disciplina não foi encontrada");
            } else {
                Log.i(TAG_EPT, "ERRO: " + task.getException());
            }
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



