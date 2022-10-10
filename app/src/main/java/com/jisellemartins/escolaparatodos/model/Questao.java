package com.jisellemartins.escolaparatodos.model;

public class Questao {
    private String desc;
    private String itemA;
    private String itemB;
    private String itemC;
    private String itemD;
    private String itemE;
    private int qtdItens;
    private int itemCorreto;
    private boolean questaoSalva;

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

    public int getItemCorreto() {
        return itemCorreto;
    }

    public void setItemCorreto(int itemCorreto) {
        this.itemCorreto = itemCorreto;
    }

    public boolean isQuestaoSalva() {
        return questaoSalva;
    }

    public void setQuestaoSalva(boolean questaoSalva) {
        this.questaoSalva = questaoSalva;
    }
}
