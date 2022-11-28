package com.jisellemartins.escolaparatodos.model;

public class Aula {
    private String descricao;
    private String audio;
    private String tamanhoAudio;
    private String texto;
    private String tamanhoTexto;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTamanhoAudio() {
        return tamanhoAudio;
    }

    public void setTamanhoAudio(String tamanhoAudio) {
        this.tamanhoAudio = tamanhoAudio;
    }

    public String getTamanhoTexto() {
        return tamanhoTexto;
    }

    public void setTamanhoTexto(String tamanhoTexto) {
        this.tamanhoTexto = tamanhoTexto;
    }
}
