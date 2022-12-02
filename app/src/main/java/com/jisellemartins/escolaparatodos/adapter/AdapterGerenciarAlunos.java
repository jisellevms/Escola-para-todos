package com.jisellemartins.escolaparatodos.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.Utils.Utils.TAG_EPT;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jisellemartins.escolaparatodos.GerenciarAlunosScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Aluno;

import java.util.ArrayList;
import java.util.List;

public class AdapterGerenciarAlunos extends RecyclerView.Adapter {
    private List<Aluno> alunos;
    private Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GerenciarAlunosScreen gerenciarAlunosScreen;

    public AdapterGerenciarAlunos(Context context, List<Aluno> alunos, GerenciarAlunosScreen gerenciarAlunosScreen) {
        this.context = context;
        this.alunos = alunos;
        this.gerenciarAlunosScreen = gerenciarAlunosScreen;

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

        viewHolder.excluirP.setOnClickListener(view -> {
            deletarAluno(aluno);
        });
    }

    public void deletarAluno(Aluno aluno) {

        alunos.remove(aluno);
        Gson gson = new Gson();
        String newList = gson.toJson(alunos);
        SharedPreferences sharedPref = context.getSharedPreferences("chaves", MODE_PRIVATE);
        String disciplinaTime = sharedPref.getString("disciplina", "");

        db.collection("Disciplina")
                .whereEqualTo("dataCriacao", disciplinaTime)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot d : task.getResult()) {
                            db.collection("Disciplina")
                                    .document(d.getId())
                                    .update("alunos", newList);
                            gerenciarAlunosScreen.listarAlunos();
                        }
                    } else {
                        Toast.makeText(context, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomeP, telefoneP;
        ImageView excluirP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeP = itemView.findViewById(R.id.nomeP);
            telefoneP = itemView.findViewById(R.id.telefoneP);
            excluirP = itemView.findViewById(R.id.excluirP);
        }
    }
}