package com.webdev.jobify._aux;

public class Picture {

    private String type;
    private byte[] bytes;

    public Picture(String type, byte[] bytes) {
        this.type = type;
        this.bytes = bytes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
