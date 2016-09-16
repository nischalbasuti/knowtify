package com.hydratech19gmail.notify;

/**
 * Created by nischal on 16/9/16.
 */
public class Attachment {
    private String type;
    private String size;
    private String name;
    private String url;

    public Attachment(){

    }

    //getters and setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
