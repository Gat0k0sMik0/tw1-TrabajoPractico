package com.tallerwebi.dominio;

public class MensajeDTO {

    private String idUsuario1;
    private String idUsuario2;
    private String content;

    public MensajeDTO() {
    }

    public String getIdUsuario1() {
        return idUsuario1;
    }

    public void setIdUsuario1(String idUsuario1) {
        this.idUsuario1 = idUsuario1;
    }

    public String getIdUsuario2() {
        return idUsuario2;
    }

    public void setIdUsuario2(String idUsuario2) {
        this.idUsuario2 = idUsuario2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MensajeDTO{" +
                "idUsuario1='" + idUsuario1 + '\'' +
                ", idUsuario2='" + idUsuario2 + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
