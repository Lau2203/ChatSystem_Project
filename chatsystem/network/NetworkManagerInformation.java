package chatsystem.network;

import java.lang.Thread;

import java.net.InetAddress;

import chatsystem.User;
import chatsystem.NotifyInformation;

import chatsystem.Message;

public class NetworkManagerInformation {

    private Thread toBeNotified;

    private User recipient;

    private NotifyInformation info;
    private String fingerprint;
    private String username;
    private InetAddress address;

    private Message msg;

    public NetworkManagerInformation() {
        this.toBeNotified = null;

        this.info = null;
        this.fingerprint = null;
        this.username = null;
        this.address = null;
    }

    public NetworkManagerInformation(Thread toBeNotified, User recipient, NotifyInformation info, String fingerprint, String username, InetAddress address, Message msg) {
        this.toBeNotified = toBeNotified;

        this.recipient = recipient;

        this.info = info;
        this.fingerprint = fingerprint;
        this.username = username;
        this.address = address;

        this.msg = msg;
    }

    public Thread getToBeNotified() {
        return this.toBeNotified;
    }

    public User getRecipientUser() {
        return this.recipient;
    }

    public NotifyInformation getNotifyInformation() {
        return this.info;
    }

    public String getFingerprint() {
        return this.fingerprint;
    }

    public String getUsername() {
        return this.username;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public Message getMessage() {
        return this.msg;
    }

    public void setToBeNotified(Thread t) {
        this.toBeNotified = t;
    }

    public void setRecipientUser(User recipient) {
        this.recipient = recipient;
    }

    public void setNotifyInformation(NotifyInformation info) {
        this.info = info;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint ;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public void setAddress(InetAddress inetAddress) {
        this.address = inetAddress;
    }

    public void setMessage(Message msg) {
        this.msg = msg;
    }
}

