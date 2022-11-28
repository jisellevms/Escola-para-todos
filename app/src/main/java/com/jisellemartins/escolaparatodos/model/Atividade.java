package com.jisellemartins.escolaparatodos.model;

import java.util.List;

public class Atividade {
    private String descricao;
    private String data;
    private String qtdQuestoes;
    private String status;
    private List<Questao> questoes;

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

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }
}
