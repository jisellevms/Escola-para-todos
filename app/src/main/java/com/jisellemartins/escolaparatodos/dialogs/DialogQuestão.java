package com.jisellemartins.escolaparatodos.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.Utils.Utils;
import com.jisellemartins.escolaparatodos.adapter.AdapterItemQuestao;
import com.jisellemartins.escolaparatodos.model.Questao;

import java.util.ArrayList;

public class DialogQuestão implements View.OnClickListener {

    Button btnSalvar;
    ImageView cancelar;
    EditText descricaoQuestao;
    EditText opcaoA;
    EditText opcaoB;
    EditText opcaoC;
    EditText opcaoD;
    EditText opcaoE;
    ImageView lixeiraA;
    ImageView lixeiraB;
    ImageView lixeiraC;
    ImageView lixeiraD;
    ImageView lixeiraE;
    RadioGroup group;
    RadioButton radioA;
    RadioButton radioB;
    RadioButton radioC;
    RadioButton radioD;
    RadioButton radioE;

    Questao questao;
    String itemCorreto;

    AdapterItemQuestao adapterItemQuestao;

    public DialogQuestão(Questao questao, AdapterItemQuestao adapterItemQuestao) {
        this.questao = questao;
        this.adapterItemQuestao = adapterItemQuestao;
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_questao);

        instancias(dialog);

        lixeiraA.setOnClickListener(this);
        lixeiraB.setOnClickListener(this);
        lixeiraC.setOnClickListener(this);
        lixeiraD.setOnClickListener(this);
        lixeiraE.setOnClickListener(this);

        descricaoQuestao.setText(questao.getDesc());
        opcaoA.setText(questao.getItemA());
        opcaoB.setText(questao.getItemB());
        opcaoC.setText(questao.getItemC());
        opcaoD.setText(questao.getItemD());
        opcaoE.setText(questao.getItemE());

        itemCorreto = "A";

        group.setOnCheckedChangeListener((radioGroup, i) -> {
            int id = group.getCheckedRadioButtonId();
            switch (id) {
                case R.id.radioA:
                    itemCorreto = "A";
                    break;
                case R.id.radioB:
                    itemCorreto = "B";
                    break;
                case R.id.radioC:
                    itemCorreto = "C";
                    break;
                case R.id.radioD:
                    itemCorreto = "D";
                    break;
                case R.id.radioE:
                    itemCorreto = "E";
                    break;
                default:
                    itemCorreto = "A";
                    break;
            }
        });

        cancelar.setOnClickListener(v -> dialog.dismiss());


        btnSalvar.setOnClickListener(view -> {
            if (!descricaoQuestao.getText().toString().isEmpty()) {
                questao.setDesc(descricaoQuestao.getText().toString());
                questao.setItemA(opcaoA.getText().toString());
                questao.setItemB(opcaoB.getText().toString());
                if(!questao.getItemC().isEmpty()) questao.setItemC(opcaoC.getText().toString());
                if(!questao.getItemD().isEmpty())questao.setItemD(opcaoD.getText().toString());
                if(!questao.getItemE().isEmpty())questao.setItemE(opcaoE.getText().toString());
                questao.setItemCorreto(itemCorreto);
                questao.setQuestaoSalva(true);
                Utils.listQuestoes.add(questao);
                adapterItemQuestao.notifyDataSetChanged();
                dialog.dismiss();

            } else {

            }
        });

        dialog.show();

    }

    public void instancias(Dialog dialog) {
        btnSalvar = dialog.findViewById(R.id.btnSalvar);
        cancelar = dialog.findViewById(R.id.cancelar);
        descricaoQuestao = dialog.findViewById(R.id.descricaoQuestao);
        opcaoA = dialog.findViewById(R.id.opcaoA);
        opcaoB = dialog.findViewById(R.id.opcaoB);
        opcaoC = dialog.findViewById(R.id.opcaoC);
        opcaoD = dialog.findViewById(R.id.opcaoD);
        opcaoE = dialog.findViewById(R.id.opcaoE);
        lixeiraA = dialog.findViewById(R.id.lixeiraA);
        lixeiraB = dialog.findViewById(R.id.lixeiraB);
        lixeiraC = dialog.findViewById(R.id.lixeiraC);
        lixeiraD = dialog.findViewById(R.id.lixeiraD);
        lixeiraE = dialog.findViewById(R.id.lixeiraE);
        group = dialog.findViewById(R.id.group);
        radioA = dialog.findViewById(R.id.radioA);
        radioB = dialog.findViewById(R.id.radioB);
        radioC = dialog.findViewById(R.id.radioC);
        radioD = dialog.findViewById(R.id.radioD);
        radioE = dialog.findViewById(R.id.radioE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lixeiraA:

                break;

            case R.id.lixeiraB:

                break;

            case R.id.lixeiraC:
                radioC.setVisibility(View.GONE);
                opcaoC.setVisibility(View.GONE);
                lixeiraC.setVisibility(View.GONE);
                questao.setItemC("");
                break;

            case R.id.lixeiraD:
                radioD.setVisibility(View.GONE);
                opcaoD.setVisibility(View.GONE);
                lixeiraD.setVisibility(View.GONE);
                questao.setItemD("");
                break;
            case R.id.lixeiraE:
                radioE.setVisibility(View.GONE);
                opcaoE.setVisibility(View.GONE);
                lixeiraE.setVisibility(View.GONE);
                questao.setItemE("");
                break;

        }
    }
}
