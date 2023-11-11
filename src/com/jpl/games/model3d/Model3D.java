package com.jpl.games.model3d;

import com.javafx.experiments.importers.obj.ObjImporter;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public class Model3D {
    private Set<String> meshes;
    private final Map<String,MeshView> mapMeshes=new HashMap<>();
    private MeshView faceArrow;
    private MeshView axisArrow;
    public Model3D(){
    }
    public void importObj(){
        try {
            ObjImporter reader = new ObjImporter(getClass().getResource("Cube.obj").toExternalForm());
            meshes=reader.getMeshes();

            Affine affineIni=new Affine();
            affineIni.prepend(new Rotate(-90, Rotate.X_AXIS));
            affineIni.prepend(new Rotate(90, Rotate.Z_AXIS));
            meshes.stream().forEach(s-> {
                MeshView cubiePart = reader.buildMeshView(s);
                cubiePart.getTransforms().add(affineIni);
                PhongMaterial material = (PhongMaterial) cubiePart.getMaterial();
                material.setSpecularPower(1);
                cubiePart.setMaterial(material);
                mapMeshes.put(s,cubiePart);
            });
        } catch (IOException e) {
            System.out.println("Error loading model "+e.toString());
        }
        try {
            ObjImporter reader = new ObjImporter(getClass().getResource("arrow.obj").toExternalForm());
            String mesh = reader.getMeshes().iterator().next();
            faceArrow = reader.buildMeshView(mesh);
        } catch (IOException e) {
            System.out.println("Error loading arrow "+e.toString());
        }
        try {
            ObjImporter reader = new ObjImporter(getClass().getResource("axis.obj").toExternalForm());
            String mesh = reader.getMeshes().iterator().next();

            axisArrow = reader.buildMeshView(mesh);
        } catch (IOException e) {
            System.out.println("Error loading arrow "+e.toString());
        }
    }
    public Map<String, MeshView> getMapMeshes() {
        return mapMeshes;
    }
    public MeshView getFaceArrow() {
        return faceArrow;
    }

    public MeshView getAxisArrow() {
        return axisArrow;
    }
}