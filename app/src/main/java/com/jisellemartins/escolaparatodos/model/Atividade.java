package com.jisellemartins.escolaparatodos.model;

public class Atividade {
    private String descricao;
    private String data;
    private String qtdQuestoes;
    private String status;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQtdQuestoes() {
        return qtdQuestoes;
    }

    public void setQtdQuestoes(String qtdQuestoes) {
        this.qtdQuestoes = qtdQuestoes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
