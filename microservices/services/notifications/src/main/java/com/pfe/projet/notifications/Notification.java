package com.pfe.projet.notifications;



import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(columnDefinition = "TEXT")

    private String message;

    private Date dateEnvoi;

    private Long userId;

    public Notification() {}
    public Notification(Long userId, String message) {
        this.userId = userId;
        this.message = message;
        this.dateEnvoi = new Date();
    }


    public Notification(Long id, String message, Date dateEnvoi, Long userId) {
        this.id = id;
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                '}';
    }
}
