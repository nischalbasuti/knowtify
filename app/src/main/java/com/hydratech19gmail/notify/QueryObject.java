package com.hydratech19gmail.notify;

/*
 * Created by nischal on 15/9/16.
 */
public class QueryObject {
    private String queryName;
    private String queryContent;
    private String queryUserId;

    public QueryObject(){

    }

    //getters and setters
    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getQueryUserId() {
        return queryUserId;
    }

    public void setQueryUserId(String queryUserId) {
        this.queryUserId = queryUserId;
    }
}
