package com.pfe.projet.notifications;

import java.util.Date;

public class NotificationResponse {

    private Long id;
    private String message;
    private Date dateEnvoi;
    private UserDTO user;
    public NotificationResponse(Notification notification, UserDTO user) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.dateEnvoi = notification.getDateEnvoi();
        this.user = user;
    }

    public NotificationResponse(Long id, String message, Date dateEnvoi, UserDTO user) {
        this.id = id;
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.user = user;
    }


    public static class Builder {
        private Long id;
        private String message;
        private Date dateEnvoi;
        private UserDTO user;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder dateEnvoi(Date dateEnvoi) {
            this.dateEnvoi = dateEnvoi;
            return this;
        }

        public Builder user(UserDTO user) {
            this.user = user;
            return this;
        }

        public NotificationResponse build() {
            return new NotificationResponse(id, message, dateEnvoi, user);
        }
    }


    public static NotificationResponse createNotification(Long id, String message, Date dateEnvoi, UserDTO user) {
        return new Builder()
                .id(id)
                .message(message)
                .dateEnvoi(dateEnvoi)
                .user(user)
                .build();
    }
    @Override
    public String toString() {
        return "NotificationResponse{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", user=" + user +
                '}';
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

}
