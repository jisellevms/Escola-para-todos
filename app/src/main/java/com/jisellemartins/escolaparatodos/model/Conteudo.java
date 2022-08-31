package com.jisellemartins.escolaparatodos.model;

import android.graphics.drawable.Drawable;

public class Conteudo {
    private String descricao;
    private String tamanho;
    private int icon;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
