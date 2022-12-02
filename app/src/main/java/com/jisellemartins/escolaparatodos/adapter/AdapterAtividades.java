package com.jisellemartins.escolaparatodos.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.Utils.Utils.TAG_EPT;
import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.jisellemartins.escolaparatodos.AtividadeQuestaoScreen;
import com.jisellemartins.escolaparatodos.AtividadesScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.Utils.Utils;
import com.jisellemartins.escolaparatodos.model.Aluno;
import com.jisellemartins.escolaparatodos.model.Atividade;

import java.util.List;

public class AdapterAtividades extends RecyclerView.Adapter {
    private List<Atividade> atividades;
    private Context context;
    private String usuario;
    SharedPreferences sharedPref;
    String disciplinaTime;
    String numeroUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AtividadesScreen atividadesScreen;


    public AdapterAtividades(Context context, List<Atividade> atividades, String usuario, AtividadesScreen atividadesScreen) {
        this.context = context;
        this.atividades = atividades;
        this.usuario = usuario;
        this.atividadesScreen = atividadesScreen;
        sharedPref = context.getSharedPreferences("chaves", MODE_PRIVATE);
        disciplinaTime = sharedPref.getString("disciplina", "");
        numeroUser = sharedPref.getString("numero", aluno);
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
        if (usuario.equals(aluno)) {
            db.collection("RelacaoADA")
                    .whereEqualTo("aluno", numeroUser)
                    .whereEqualTo("atividade", atividade.getCodigoAtv())
                    .whereEqualTo("disciplina", disciplinaTime)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().size() > 0) {
                            atividade.setStatus("Concluido");
                            viewHolder.statusAtv.setText("Status da atividade: " + atividade.getStatus());
                            viewHolder.iconStatus.setImageResource(R.drawable.check);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                viewHolder.iconStatus.setTooltipText("Concluido");
                            }
                            viewHolder.item_atividade.setEnabled(false);
                        } else {
                            atividade.setStatus("Pendente");
                            viewHolder.statusAtv.setText("Status da atividade: " + atividade.getStatus());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                viewHolder.iconStatus.setTooltipText("Pendente");
                            }
                            viewHolder.iconStatus.setImageResource(R.drawable.aviso);
                        }
                    });


        } else {
            viewHolder.statusAtv.setText("Status da atividade: " + atividade.getStatus());
            viewHolder.iconStatus.setImageResource(R.drawable.lixeira_braca);
            viewHolder.iconStatus.setOnClickListener(view -> {
                deletarAtividade(atividade);
            });

        }

        viewHolder.item_atividade.setOnClickListener(view -> {
            Utils.atividade = atividade;
            Intent i = new Intent(context,
                    AtividadeQuestaoScreen.class);
            context.startActivity(i);
        });

    }

    public void deletarAtividade(Atividade atividade) {


        SharedPreferences sharedPref = context.getSharedPreferences("chaves", MODE_PRIVATE);
        String disciplinaTime = sharedPref.getString("disciplina", "");

        db.collection("Atividade")
                .whereEqualTo("disciplina", disciplinaTime)
                .whereEqualTo("atividade", atividade.getCodigoAtv())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {

                        db.collection("Atividade").document(task.getResult().getDocuments().get(0).getId()).delete().addOnSuccessListener(aVoid -> {
                            Log.d(TAG_EPT, "A atividade foi apagada!");
                            Toast.makeText(context, "Atividade excluida com sucesso", Toast.LENGTH_LONG).show();
                            atividadesScreen.listarAtividades();
                        }).addOnFailureListener(e -> {
                            Log.w(TAG_EPT, "Error ao apagar atividade ", e);
                        });

                    } else {
                        Toast.makeText(context, "Erro: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
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

