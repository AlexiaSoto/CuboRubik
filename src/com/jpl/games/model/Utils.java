package com.jpl.games.model;
import com.jpl.games.math.Rotations;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Utils {
    private static final List<String> movements = Arrays.asList("F", "Fi", "F2", "R", "Ri", "R2", "B", "Bi", "B2", "L", "Li", "L2",
            "U", "Ui", "U2", "D", "Di", "D2");
    private static final List<String> orientations=Arrays.asList("V-V","V-Y","V-Yi","V-Y2", "X-V","X-Z","X-Zi","X-Z2", "Xi-V","Xi-Z","Xi-Zi",
            "X2-V","X2-Z","X2-Zi", "X-Y","X-Yi","X-Y2", "Xi-Y","Xi-Yi","X2-Y","X2-Yi", "Z-V","Zi-V","Z2-V");
    public static final List<String> getMovements() { return movements; }
    public static final List<String> getOrientations() { return orientations; }
    public static Affine getAffine(double dimCube, double d0, boolean bFaceArrow, String face){
        Affine aff;
        double d=2d*dimCube/3d;
        if(!bFaceArrow){
            aff=new Affine(new Scale(80,80,50));
            aff.append(new Translate(-d0,-d0,d0));
        } else {
            aff=new Affine(new Scale(3,3,3));
            aff.append(new Translate(0,-d0,0));
        }
        switch(face){
            case "F":
            case "Fi":  aff.prepend(new Rotate(face.equals("F")?90:-90,Rotate.X_AXIS));
                aff.prepend(new Rotate(face.equals("F")?45:-45,Rotate.Z_AXIS));
                aff.prepend(new Translate(0,0,dimCube/2d));
                break;
            case "B":
            case "Bi":  aff.prepend(new Rotate(face.equals("Bi")?90:-90,Rotate.X_AXIS));
                aff.prepend(new Rotate(face.equals("Bi")?45:-45,Rotate.Z_AXIS));
                aff.prepend(new Translate(0,0,-dimCube/2d));
                break;
            case "R":
            case "Ri":  aff.prepend(new Rotate(face.equals("Ri")?90:-90,Rotate.Z_AXIS));
                aff.prepend(new Rotate(face.equals("Ri")?45:-45,Rotate.X_AXIS));
                aff.prepend(new Translate(dimCube/2d,0,0));
                break;
            case "L":
            case "Li":  aff.prepend(new Rotate(face.equals("L")?90:-90,Rotate.Z_AXIS));
                aff.prepend(new Rotate(face.equals("L")?45:-45,Rotate.X_AXIS));
                aff.prepend(new Translate(-dimCube/2d,0,0));
                break;
            case "U":
            case "Ui":  aff.prepend(new Rotate(face.equals("Ui")?180:0,Rotate.Z_AXIS));
                aff.prepend(new Rotate(face.equals("Ui")?45:-45,Rotate.Y_AXIS));
                aff.prepend(new Translate(0,dimCube/2d,0));
                break;
            case "D":
            case "Di":  aff.prepend(new Rotate(face.equals("D")?180:0,Rotate.Z_AXIS));
                aff.prepend(new Rotate(face.equals("D")?45:-45,Rotate.Y_AXIS));
                aff.prepend(new Translate(0,-dimCube/2d,0));
                break;
            case "Z":
            case "Zi":  aff.prepend(new Rotate(face.equals("Zi")?180:0,Rotate.Y_AXIS));
                aff.prepend(new Rotate(face.equals("Zi")?45:-45,Rotate.Z_AXIS));
                aff.prepend(new Translate(0,0,d));
                break;
            case "X":
            case "Xi":  aff.prepend(new Rotate(face.equals("X")?90:-90,Rotate.Y_AXIS));
                aff.prepend(new Rotate(face.equals("Xi")?45:-45,Rotate.X_AXIS));
                aff.prepend(new Translate(d,0,0));
                break;
            case "Y":
            case "Yi":  aff.prepend(new Rotate(face.equals("Yi")?90:-90,Rotate.X_AXIS));
                aff.prepend(new Rotate(face.equals("Yi")?45:-45,Rotate.Y_AXIS));
                aff.prepend(new Translate(0,d,0));
                break;
        }
        return aff;
    }
    public static PhongMaterial getMaterial(String face){
        PhongMaterial arrowMat = new PhongMaterial();
        arrowMat.setSpecularColor(Color.BLACK);
        Color color=Color.BLACK;
        switch(face){
            case "F":
            case "Fi":  color=Color.BLUE.brighter();
                break;
            case "B":
            case "Bi":  color=Color.BLUE.brighter();
                break;
            case "R":
            case "Ri":  color=Color.RED.brighter();
                break;
            case "L":
            case "Li":  color=Color.RED.brighter();
                break;
            case "U":
            case "Ui":  color=Color.FORESTGREEN.brighter();
                break;
            case "D":
            case "Di":  color=Color.FORESTGREEN.brighter();
                break;
            case "Z":
            case "Zi":  color=Color.BLUE.brighter();
                break;
            case "X":
            case "Xi":  color=Color.RED.brighter();
                break;
            case "Y":
            case "Yi":  color=Color.FORESTGREEN.brighter();
                break;
        }
        arrowMat.setDiffuseColor(color);
        return arrowMat;
    }
    public static Point3D getAxis(String face){
        Point3D p=new Point3D(0,0,0);
        switch(face.substring(0,1)){
            case "L":
            case "M":  p=new Point3D(-1,0,0);
                break;
            case "R":  p=new Point3D(1,0,0);
                break;
            case "U":  p=new Point3D(0,1,0);
                break;
            case "E":
            case "D":  p=new Point3D(0,-1,0);
                break;
            case "F":
            case "S":  p=new Point3D(0,0,1);
                break;
            case "B":  p=new Point3D(0,0,-1);
                break;
            case "X":  p=new Point3D(1,0,0);
                break;
            case "Y":  p=new Point3D(0,1,0);
                break;
            case "Z":  p=new Point3D(0,0,1);
                break;
        }
        return p;
    }
    public static int getCenter(String face){
        int c=0;
        switch(face.substring(0,1)){
            case "L":  c=12; break;
            case "M":  c=13; break;
            case "R":  c=14; break;
            case "U":  c=10; break;
            case "E":  c=13; break;
            case "D":  c=16; break;
            case "F":  c=4;  break;
            case "S":  c=13; break;
            case "B":  c=22; break;
        }
        return c;
    }
    public static String reverseRotation(String rot){
        if(rot.endsWith("i")){
            return rot.substring(0,1);
        }
        return rot.concat("i");
    }
    public static boolean checkOrientation(String r, List<Integer> order){
        Rotations rot=new Rotations();
        for(String s:r.split("-")){
            if(s.contains("2")){
                rot.turn(s.substring(0,1));
                rot.turn(s.substring(0,1));
            } else {
                rot.turn(s);
            }
        }
        return order.equals(rot.getCube());
    }
    public static boolean checkSolution(List<Integer> order) {
        return Utils.getOrientations().parallelStream()
                .filter(r->Utils.checkOrientation(r,order)).findAny().isPresent();
    }
    public static ArrayList<String> unifyNotation(String list) {
        List<String> asList = Arrays.asList(list.replaceAll("’", "i").replaceAll("'", "i").split(" "));
        ArrayList<String> sequence = new ArrayList<>();
        asList.stream().forEach(s -> {
            if (s.contains("2")) {
                sequence.add(s.substring(0, 1));
                sequence.add(s.substring(0, 1));
            } else if (s.length() == 1 && s.matches("[a-z]")) {
                sequence.add(s.toUpperCase().concat("i"));
            } else {
                sequence.add(s);
            }
        });
        return sequence;
    }
}
