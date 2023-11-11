package com.jpl.games.model;
public class Move{
    private String imagenface;
    private long timestamp;
    public Move(String imagenface, long timestamp){
        this.imagenface=imagenface;
        this.timestamp=timestamp;
    }
    public String getFace() {
        return imagenface;
    }
    public long getTimestamp() {
        return timestamp;
    }
    @Override
    public String toString() {
        return "Move{" + "face=" + imagenface + ", timestamp=" + timestamp + '}';
    }
}



