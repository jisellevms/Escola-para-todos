package com.jisellemartins.escolaparatodos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jisellemartins.escolaparatodos.R;
import com.jisellemartins.escolaparatodos.model.Evento;
import com.jisellemartins.escolaparatodos.model.Numero;

import java.util.List;

public class AdapterNumeros extends RecyclerView.Adapter{
    private List<Numero> numeros;
    private Context context;

    public AdapterNumeros(Context context, List<Numero> numeros) {
        this.context = context;
        this.numeros = numeros;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_numero, parent, false);
        AdapterNumeros.ViewHolder holder = new AdapterNumeros.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterNumeros.ViewHolder viewHolder = (AdapterNumeros.ViewHolder) holder;
        Numero numero = numeros.get(position);
        viewHolder.numero.setText(numero.getNumero());
    }

    @Override
    public int getItemCount() {
        return numeros.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numero;
        ImageView editar, excluir;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numero = itemView.findViewById(R.id.numero);
            editar = itemView.findViewById(R.id.editar);
            excluir = itemView.findViewById(R.id.excluir);
        }
    }
}