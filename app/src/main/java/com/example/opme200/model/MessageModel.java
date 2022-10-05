package com.example.opme200.model;

public class MessageModel {

    String smsUId,message;
    Long timestamp;

    public MessageModel(String smsUId, String message, Long timestamp) {
        this.smsUId = smsUId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel(String smsUId, String message) {
        this.smsUId = smsUId;
        this.message = message;
    }

    public MessageModel() {
    }

    public String getSmsUId() {
        return smsUId;
    }

    public void setSmsUId(String smsUId) {
        this.smsUId = smsUId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
