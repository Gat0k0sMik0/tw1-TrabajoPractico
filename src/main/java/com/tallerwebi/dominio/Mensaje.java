package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private Usuario fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private Usuario toUser;

    private String content;

    private LocalDateTime timestamp;

    public Mensaje() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getFromUser() {
        return fromUser;
    }

    public void setFromUser(Usuario fromUser) {
        this.fromUser = fromUser;
    }

    public Usuario getToUser() {
        return toUser;
    }

    public void setToUser(Usuario toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "id=" + id +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
