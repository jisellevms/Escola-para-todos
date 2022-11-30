package com.jisellemartins.escolaparatodos.model;

public class Questao {
    private String desc;
    private String itemA;
    private String itemB;
    private String itemC;
    private String itemD;
    private String itemE;
    private int qtdItens;
    private String itemCorreto;
    private boolean questaoSalva;
    private int numeroQuestao;
    private String itemSelecionado;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemA() {
        return itemA;
    }

    public void setItemA(String itemA) {
        this.itemA = itemA;
    }

    public String getItemB() {
        return itemB;
    }

    public void setItemB(String itemB) {
        this.itemB = itemB;
    }

    public String getItemC() {
        return itemC;
    }

    public void setItemC(String itemC) {
        this.itemC = itemC;
    }

    public String getItemD() {
        return itemD;
    }

    public void setItemD(String itemD) {
        this.itemD = itemD;
    }

    public String getItemE() {
        return itemE;
    }

    public void setItemE(String itemE) {
        this.itemE = itemE;
    }

    public int getQtdItens() {
        return qtdItens;
    }

    public void setQtdItens(int qtdItens) {
        this.qtdItens = qtdItens;
    }

    public String getItemCorreto() {
        return itemCorreto;
    }

    public void setItemCorreto(String itemCorreto) {
        this.itemCorreto = itemCorreto;
    }

    public boolean isQuestaoSalva() {
        return questaoSalva;
    }

    public void setQuestaoSalva(boolean questaoSalva) {
        this.questaoSalva = questaoSalva;
    }

    public int getNumeroQuestao() {
        return numeroQuestao;
    }

    public void setNumeroQuestao(int numeroQuestao) {
        this.numeroQuestao = numeroQuestao;
    }

    public String getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(String itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }
}
