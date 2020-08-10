package com.bruno.cursomc.dto;

import com.bruno.cursomc.dominio.Cidade;

import java.io.Serializable;

public class CidadeDTO implements Serializable {
    private static final long SerialVersionUID = 1L;

    private Integer id;
    private String nome;

    public CidadeDTO(){
    }

    public CidadeDTO(Cidade obj){
        id = obj.getId();
        nome = obj.getNome();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
