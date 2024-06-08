package com.example.eventplanner.model;

import java.util.Date;

public class Message {
    public Long Id;
    public String SenderId;
    public String RecipientId;
    public Date DateOfSending;
    public String Content;
    public boolean status;

    public Message(Long id, String senderId, String recipientId, Date dateOfSending, String content, boolean status) {
        Id = id;
        SenderId = senderId;
        RecipientId = recipientId;
        DateOfSending = dateOfSending;
        Content = content;
        this.status = status;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getRecipientId() {
        return RecipientId;
    }

    public void setRecipientId(String recipientId) {
        RecipientId = recipientId;
    }

    public Date getDateOfSending() {
        return DateOfSending;
    }

    public void setDateOfSending(Date dateOfSending) {
        DateOfSending = dateOfSending;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
