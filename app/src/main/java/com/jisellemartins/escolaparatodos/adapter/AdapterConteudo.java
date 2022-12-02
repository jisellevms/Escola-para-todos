package com.jisellemartins.escolaparatodos.adapter;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.jisellemartins.escolaparatodos.Utils.Utils.TAG_EPT;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jisellemartins.escolaparatodos.BibliotecaScreen;
import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Conteudo;

import java.util.List;

public class AdapterConteudo extends RecyclerView.Adapter{
    private List<Conteudo> conteudos;
    private Context context;
    private String usuario;
    FirebaseStorage storage;
    String disciplina;
    BibliotecaScreen bs;


    public AdapterConteudo(Context context, List<Conteudo> conteudos, String usuario, FirebaseStorage storage, String disciplina, BibliotecaScreen bs) {
        this.context = context;
        this.conteudos = conteudos;
        this.usuario = usuario;
        this.storage = storage;
        this.disciplina = disciplina;
        this.bs = bs;

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

        viewHolder.btnLixeira.setOnClickListener(view -> {
            String url = disciplina + "/" + conteudo.getDescricao().substring(0,conteudo.getDescricao().lastIndexOf("."));

            StorageReference storageReference =
                    FirebaseStorage.getInstance().getReference().child(url);

            storageReference.delete().addOnSuccessListener(aVoid -> {
                Toast.makeText(context, conteudo.getDescricao() + " foi excluído com sucesso!", Toast.LENGTH_LONG).show();
               bs.buscarConteudos();

                Log.d(TAG_EPT, "onSuccess: deleted file successfully");
            }).addOnFailureListener(exception -> Log.d(TAG_EPT, "onFailure: File is not delete! " + exception));
        });

        viewHolder.btnDownload.setOnClickListener(view -> {
            download(conteudo.getDescricao().substring(0,conteudo.getDescricao().lastIndexOf(".")));
        });
    }

    private void download(String descArquivo) {
        SharedPreferences sharedPref = context.getSharedPreferences("chaves", MODE_PRIVATE);
        String disciplinaTime = sharedPref.getString("disciplina", "");

        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child(disciplinaTime+"/"+descArquivo);

        islandRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Log.e(TAG_EPT,";local tem file created  created ");

            String url = uri.toString();

            downloadFile(context, descArquivo, DIRECTORY_DOWNLOADS, url);
        }).addOnFailureListener(exception -> Log.e(TAG_EPT,";local tem file not created  created " +exception));
    }

    public void downloadFile(Context context, String filename, String destinationDirectory, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory,filename);

        downloadManager.enqueue(request);
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
            btnDownload = itemView.findViewById(R.id.btnEntrarAula);
            descricao = itemView.findViewById(R.id.descricaoAula);
            tamanho = itemView.findViewById(R.id.tamanho);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            btnLixeira = itemView.findViewById(R.id.btnLixeira);
        }
    }
}