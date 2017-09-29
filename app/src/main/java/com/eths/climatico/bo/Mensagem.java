package com.eths.climatico.bo;


public class Mensagem {
    private int idMensagem;
    private String mensagem;
    private String data;

    public Mensagem(int idMensagem, String data, String mensagem) {
        this.idMensagem = idMensagem;
        this.data = data;
        this.mensagem = mensagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(int idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
