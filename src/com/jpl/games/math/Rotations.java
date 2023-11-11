package com.jpl.games.math;

import java.util.ArrayList;
import java.util.List;
public class Rotations {
    private final int[][][] cube={{{50,51,52},{49,54,53},{59,48,46}}, {{58,55,60},{57,62,61},{47,56,63}}, {{67,64,69},{66,71,70},{68,65,72}}};
    private final int[][][] tempCube=new int[3][3][3];
    public Rotations(){
        for(int a = 0; a < 3; a++){
            for(int b = 0; b < 3; b++){
                System.arraycopy(cube[a][b], 0, tempCube[a][b], 0, 3);
            }
        }
    }
    public List<Integer> getCube(){
        List<Integer> newArray = new ArrayList<>(27);
        for(int a = 0; a < 3; a++){
            for(int b = 0; b < 3; b++){
                for(int c = 0; c < 3; c++){
                    newArray.add(cube[a][b][c]);
                }
            }
        }
        return newArray;
    }
    public void setCube(List<Integer> order){
        int index=0;
        for(int a = 0; a < 3; a++){
            for(int b = 0; b < 3; b++){
                for(int c = 0; c < 3; c++){
                    cube[a][b][c]=order.get(index++);
                    tempCube[a][b][c]=cube[a][b][c];
                }
            }
        }
    }
    public void save(){
        for(int a = 0; a < 3; a++){
            for(int b = 0; b < 3; b++){
                System.arraycopy(tempCube[a][b], 0, cube[a][b], 0, 3);
            }
        }
    }
    public void turn(String rot){
        if(rot.contains("X") || rot.contains("Y") || rot.contains("Z")){
            for(int z=0; z<3; z++){
                int t = 0;
                for(int y = 2; y >= 0; --y){
                    for(int x = 0; x < 3; x++){
                        switch(rot){
                            case "X":  tempCube[t][x][z] = cube[x][y][z]; break;
                            case "Xi": tempCube[x][t][z] = cube[y][x][z]; break;
                            case "Y":  tempCube[t][z][x] = cube[x][z][y]; break;
                            case "Yi": tempCube[x][z][t] = cube[y][z][x]; break;
                            case "Z":  tempCube[z][x][t] = cube[z][y][x]; break;
                            case "Zi": tempCube[z][t][x] = cube[z][x][y]; break;
                        }
                    }
                    t++;
                }
            }
        } else {
            int t = 0;
            for(int y = 2; y >= 0; --y){
                for(int x = 0; x < 3; x++){
                    switch(rot){
                        case "L":  tempCube[x][t][0] = cube[y][x][0]; break;
                        case "Li": tempCube[t][x][0] = cube[x][y][0]; break;
                        case "M":  tempCube[x][t][1] = cube[y][x][1]; break;
                        case "Mi": tempCube[t][x][1] = cube[x][y][1]; break;
                        case "R":  tempCube[t][x][2] = cube[x][y][2]; break;
                        case "Ri": tempCube[x][t][2] = cube[y][x][2]; break;
                        case "U":  tempCube[t][0][x] = cube[x][0][y]; break;
                        case "Ui": tempCube[x][0][t] = cube[y][0][x]; break;
                        case "E":  tempCube[x][1][t] = cube[y][1][x]; break;
                        case "Ei": tempCube[t][1][x] = cube[x][1][y]; break;
                        case "D":  tempCube[x][2][t] = cube[y][2][x]; break;
                        case "Di": tempCube[t][2][x] = cube[x][2][y]; break;
                        case "F":  tempCube[0][x][t] = cube[0][y][x]; break;
                        case "Fi": tempCube[0][t][x] = cube[0][x][y]; break;
                        case "S":  tempCube[1][x][t] = cube[1][y][x]; break;
                        case "Si": tempCube[1][t][x] = cube[1][x][y]; break;
                        case "B":  tempCube[2][t][x] = cube[2][x][y]; break;
                        case "Bi": tempCube[2][x][t] = cube[2][y][x]; break;
                    }
                }
                t++;
            }
        }
        save();
    }
}
