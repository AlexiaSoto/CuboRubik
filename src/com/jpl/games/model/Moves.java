package com.jpl.games.model;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
public class Moves {
    private final List<Move> moves=new ArrayList<>();
    private long timePlay;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
    public void addMove(Move m){ moves.add(m); }
    public int getNumMoves() { return moves.size(); }


    public void setTimePlay(long timePlay) { this.timePlay = timePlay; }

    public Moves(){
        moves.clear();
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder( "Moves:\n");
        sb.append("Number of moves: ").append(moves.size()).append("\n");
        sb.append("Time of Play: ").append(LocalTime.ofNanoOfDay(timePlay).format(fmt)).append("\n\n");
        AtomicInteger ind=new AtomicInteger();
        moves.forEach(m->sb.append("Move ").append(ind.getAndIncrement()).append(": ")
                .append(m.getFace()).append(" at ")
                .append(LocalTime.ofNanoOfDay(m.getTimestamp()).format(fmt)).append("\n"));
        return sb.toString();
    }
}
