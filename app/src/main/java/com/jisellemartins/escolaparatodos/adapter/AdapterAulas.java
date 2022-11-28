package com.jisellemartins.escolaparatodos.adapter;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.jisellemartins.escolaparatodos.BibliotecaScreen.bytesIntoHumanReadable;
import static com.jisellemartins.escolaparatodos.Utils.Utils.aluno;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Aula;

import java.util.List;

public class AdapterAulas extends RecyclerView.Adapter {
    private List<Aula> aulas;
    private Context context;
    private String usuario;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    SharedPreferences sharedPref;
    String disciplinaTime;


    public AdapterAulas(Context context, List<Aula> aulas, String usuario) {
        this.context = context;
        this.aulas = aulas;
        this.usuario = usuario;
        sharedPref = context.getSharedPreferences("chaves", MODE_PRIVATE);
        disciplinaTime = sharedPref.getString("disciplina", "");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_aula, parent, false);
        AdapterAulas.ViewHolder holder = new AdapterAulas.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterAulas.ViewHolder viewHolder = (AdapterAulas.ViewHolder) holder;
        Aula aula = aulas.get(position);
        viewHolder.descricaoAula.setText(aula.getDescricao());

        StorageReference listRefAudio = storage.getReference().child(disciplinaTime).child(aula.getDescricao() + "/audio");
        listRefAudio.getMetadata().addOnSuccessListener(storageMetadata -> {
            aula.setTamanhoAudio(bytesIntoHumanReadable(storageMetadata.getSizeBytes()));
            viewHolder.btnAudio.setText("Baixar áudio (" + aula.getTamanhoAudio() + ")");
        }).addOnFailureListener(exception -> {
            Log.i("testexx", exception.getMessage());
            viewHolder.btnAudio.setText("Baixar áudio");
        });

       /* StorageReference listRefTexto = storage.getReference().child(disciplinaTime).child(aula.getDescricao() + "/texto");
        listRefTexto.getMetadata().addOnSuccessListener(storageMetadata -> {
            aula.setTamanhoTexto(bytesIntoHumanReadable(storageMetadata.getSizeBytes()));
            viewHolder.btnTexto.setText("Baixar texto (" + aula.getTamanhoTexto() + ")");
        }).addOnFailureListener(exception -> {
            Log.i("testexx", exception.getMessage());
            viewHolder.btnTexto.setText("Baixar texto");
        });*/

        viewHolder.btnTexto.setText("Baixar texto");
        viewHolder.btnTexto.setEnabled(false);



        if (usuario.equals(aluno)) {
            viewHolder.btnLixeira.setVisibility(View.GONE);
        } else {
            viewHolder.btnLixeira.setVisibility(View.VISIBLE);
        }
        viewHolder.btnAudio.setOnClickListener(view -> {
            download(aula.getAudio());
        });
        viewHolder.btnTexto.setOnClickListener(view -> {
            download(aula.getTexto());
        });

    }

    private void download(String localizaoArquivo) {

        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child(localizaoArquivo);

        islandRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Log.e("testexx", ";local tem file created  created ");

            String url = uri.toString();

            downloadFile(context, localizaoArquivo, DIRECTORY_DOWNLOADS, url);
        }).addOnFailureListener(exception -> Log.e("testexx", ";local tem file not created  created " + exception));
    }

    public void downloadFile(Context context, String filename, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, filename);

        downloadManager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return aulas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descricaoAula;
        Button btnAudio, btnTexto;
        ImageView btnLixeira;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricaoAula = itemView.findViewById(R.id.descricaoAula);
            btnAudio = itemView.findViewById(R.id.btnAudio);
            btnTexto = itemView.findViewById(R.id.btnTexto);
            btnLixeira = itemView.findViewById(R.id.btnLixeira);
        }
    }
}



