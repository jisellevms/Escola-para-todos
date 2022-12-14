package com.jisellemartins.escolaparatodos.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Questao;

import java.util.List;

public class AdapterQuestao extends RecyclerView.Adapter {
    private List<Questao> questoes;
    private Context context;
    String usuario;

    public AdapterQuestao(Context context, List<Questao> questoes) {
        this.context = context;
        this.questoes = questoes;

        SharedPreferences sharedPref = context.getSharedPreferences("chaves", MODE_PRIVATE);
        usuario = sharedPref.getString("usuario", aluno);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_atv_questao, parent, false);
        AdapterQuestao.ViewHolder holder = new AdapterQuestao.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterQuestao.ViewHolder viewHolder = (AdapterQuestao.ViewHolder) holder;
        Questao questao = questoes.get(position);
        viewHolder.descQuestao.setText(questao.getDesc());


        if (questao.getItemC().isEmpty()) {
            viewHolder.itemC.setVisibility(View.GONE);
            viewHolder.radioC.setVisibility(View.GONE);
        }
        if (questao.getItemD().isEmpty()) {
            viewHolder.itemD.setVisibility(View.GONE);
            viewHolder.radioD.setVisibility(View.GONE);
        }
        if (questao.getItemE().isEmpty()) {
            viewHolder.itemE.setVisibility(View.GONE);
            viewHolder.radioE.setVisibility(View.GONE);
        }
        viewHolder.itemA.setText(questao.getItemA());
        viewHolder.itemB.setText(questao.getItemB());
        viewHolder.itemC.setText(questao.getItemC());
        viewHolder.itemD.setText(questao.getItemD());
        viewHolder.itemE.setText(questao.getItemE());


        viewHolder.group.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioA:
                    questao.setItemSelecionado("A");
                    break;
                case R.id.radioB:
                    questao.setItemSelecionado("B");
                    break;
                case R.id.radioC:
                    questao.setItemSelecionado("C");
                    break;
                case R.id.radioD:
                    questao.setItemSelecionado("D");
                    break;
                case R.id.radioE:
                    questao.setItemSelecionado("E");
                    break;

            }
        });
    }


    @Override
    public int getItemCount() {
        return questoes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descQuestao, itemA, itemB, itemC, itemD, itemE;
        RadioButton radioA, radioB, radioC, radioD, radioE;
        RadioGroup group;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descQuestao = itemView.findViewById(R.id.descQuestao);
            itemA = itemView.findViewById(R.id.itemA);
            itemB = itemView.findViewById(R.id.itemB);
            itemC = itemView.findViewById(R.id.itemC);
            itemD = itemView.findViewById(R.id.itemD);
            itemE = itemView.findViewById(R.id.itemE);
            group = itemView.findViewById(R.id.group);
            radioA = itemView.findViewById(R.id.radioA);
            radioB = itemView.findViewById(R.id.radioB);
            radioC = itemView.findViewById(R.id.radioC);
            radioD = itemView.findViewById(R.id.radioD);
            radioE = itemView.findViewById(R.id.radioE);


        }
    }
}
