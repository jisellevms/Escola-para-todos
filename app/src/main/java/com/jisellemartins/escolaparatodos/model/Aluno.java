package com.jisellemartins.escolaparatodos.model;

import java.util.List;

public class Aluno {
    private String nome;
    private String telefone;
    private List<Boletim> boletim;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Boletim> getBoletim() {
        return boletim;
    }

    public void setBoletim(List<Boletim> boletim) {
        this.boletim = boletim;
    }
}
