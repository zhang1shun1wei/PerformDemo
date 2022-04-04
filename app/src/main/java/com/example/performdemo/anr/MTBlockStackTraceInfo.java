package com.example.performdemo.anr;

public class MTBlockStackTraceInfo {
    private String stackTrace;
    public int collectCount;

    public MTBlockStackTraceInfo(String stackTrace) {
        this.stackTrace = stackTrace;
        this.collectCount = 1;
    }

    @Override
    public int hashCode() {
        return stackTrace.hashCode();
    }

    public String getMapKey() {
        return hashCode() + "";
    }
}
