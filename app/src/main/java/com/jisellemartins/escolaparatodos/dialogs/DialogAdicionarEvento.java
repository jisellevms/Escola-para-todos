package com.jisellemartins.escolaparatodos.dialogs;

import static android.content.Context.MODE_PRIVATE;
import static com.jisellemartins.escolaparatodos.CadastroScreen.getRandomNonRepeatingIntegers;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jisellemartins.escolaparatodos.CalendarioScreen;
import com.jisellemartins.escolaparatodos.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DialogAdicionarEvento {

    String dataSelecionada = "";
    String disciplinaTime = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    public void showDialog(Activity activity, CalendarioScreen calendarioScreen){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_adicionar_evento);

        ImageView cancelar = (ImageView) dialog.findViewById(R.id.cancelar);
        Button btnSalvar = (Button) dialog.findViewById(R.id.btnSalvar);
        CalendarView calendar = (CalendarView) dialog.findViewById(R.id.calendar);
        EditText campoDesc = (EditText) dialog.findViewById(R.id.campoDesc);

        SharedPreferences sharedPref = activity.getSharedPreferences("chaves", MODE_PRIVATE);
        disciplinaTime = sharedPref.getString("disciplina", "");

        cancelar.setOnClickListener(v -> dialog.dismiss());

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            dataSelecionada = year + "/" + (month+1) + "/" + dayOfMonth;
        });

        btnSalvar.setOnClickListener(view -> {
            if (dataSelecionada.equals("") || campoDesc.getText().toString().isEmpty()){
                Toast.makeText(activity, "É necesssário selecionar uma data e preencher o campo.", Toast.LENGTH_LONG).show();
            }else{
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date date = format.parse(dataSelecionada);

                    Map<String, Object> professor = new HashMap<>();
                    professor.put("dateEvento", String.valueOf(date.getTime()));
                    professor.put("descricao", campoDesc.getText().toString());
                    professor.put("disciplina", disciplinaTime);

                    db.collection("Evento").document(getRandomNonRepeatingIntegers(6,0,1000).toString())
                            .set(professor)
                            .addOnSuccessListener(aVoid -> {
                                calendarioScreen.buscarEventosHoje();
                                Toast.makeText(activity, "Evento adicionado com sucesso!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();

                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(activity, "Erro: " + e, Toast.LENGTH_LONG).show();

                            });


                } catch (ParseException e) {
                    Toast.makeText(activity, "Erro: " + e, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });



        dialog.show();

    }
}
