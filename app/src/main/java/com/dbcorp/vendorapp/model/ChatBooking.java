package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Bhupesh Sen on 25-03-2021.
 */
public class ChatBooking  implements Serializable {
    @SerializedName("chat_id")
    @Expose
    private String chatId;
    @SerializedName("chat_by")
    @Expose
    private String chatBy;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("chat_to")
    @Expose
    private String chatTo;
    @SerializedName("chat_status")
    @Expose
    private String chatStatus;
    @SerializedName("chat_time")
    @Expose
    private String chatTime;
    @SerializedName("chat_doccument")
    @Expose
    private String chatDoccument;

    public ChatBooking(String userId, String toString, String timeStamp, String name) {
        this.chatBy=userId;
        this.message=toString;
        this.chatTime=timeStamp;
        this.chatDoccument=name;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatBy() {
        return chatBy;
    }

    public void setChatBy(String chatBy) {
        this.chatBy = chatBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatTo() {
        return chatTo;
    }

    public void setChatTo(String chatTo) {
        this.chatTo = chatTo;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getChatDoccument() {
        return chatDoccument;
    }

    public void setChatDoccument(String chatDoccument) {
        this.chatDoccument = chatDoccument;
    }
    public static Comparator<ChatBooking> byChatId = new Comparator<ChatBooking>() {



        @Override
        public int compare(ChatBooking e1, ChatBooking e2) {
            int chatIdOne= Integer.parseInt(e1.getChatId());
            int twoChatId= Integer.parseInt(e2.getChatId());
            return (int) (chatIdOne - twoChatId);
        }
    };
}
